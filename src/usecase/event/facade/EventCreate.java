package usecase.event.facade;

import entity.event.*;
import presenter.event.EventEditPresent;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.commu.MessagingManage;
import usecase.dto.builder.EventBuilder;
import usecase.dto.builder.MultiSpeakerEventBuilder;
import usecase.dto.builder.NonSpeakerEventBuilder;
import usecase.dto.builder.TalkBuilder;
import usecase.event.EventManage;
import usecase.event.EventType;
import usecase.friends.manager.FriendsManage;
import usecase.score.ScoreManage;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class EventCreate implements Serializable{
	
	private final EventManage     em;
	private final TaskManage      tm;
	private final MessagingManage mm;
	private final FriendsManage   fm;
	private final ScoreManage     sm;
	
	private final EventConvert eventConvert;
	
	public EventCreate(EventManage em, TaskManage tm, MessagingManage mm, FriendsManage fm,
	                   ScoreManage sm, EventConvert eventConvert){
		this.em = em;
		this.tm = tm;
		this.mm = mm;
		this.fm = fm;
		this.sm = sm;
		this.eventConvert = eventConvert;
	}
	
	public EventBuilder getEventBuilder(EventType type){
		switch(type){
			case TALK:
				return new TalkBuilder();
			case NON_SPEAKER_EVENT:
				return new NonSpeakerEventBuilder();
			case MULTI_SPEAKER_EVENT:
				return new MultiSpeakerEventBuilder();
			default:
				throw new IllegalArgumentException("Illegal Type of " + type);
		}
	}
	
	public EventBuilder getEventBuilder(UUID event){
		Event e = em.get(event);
		EventBuilderFactory factory = new EventBuilderFactory();
		e.accept(factory);
		return factory.getBuilder();
	}
	
	public boolean createEvent(EventBuilder builder, EventEditPresent presenter){
		if(! validateEventInput(builder, presenter)){
			return false;
		}
		Event e = builder.build();
		if(em.isStarted(e)){
			presenter.respondInvalidTime();
		}else{
			List<UUID> conflictingEvents = em.getConflicting(e);
			if(conflictingEvents.size() != 0){
				presenter.respondConflictingEvents(eventConvert.convert(conflictingEvents));
			}else{
				SpeakerTaskUpdate stu = new SpeakerTaskUpdate(true, presenter);
				e.accept(stu);
				if(stu.isSuccess()){
					em.add(e);
					mm.createAnnouncement(e.getID());
					// enroll the announcement group for speaker and organizer
					AnnouncementEnroll enroll = new AnnouncementEnroll(true);
					e.accept(enroll);
					sm.createEventScoring(e.getID());
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean validateEventInput(EventBuilder builder, EventEditPresent presenter){
		if(builder.getTitle() == null || builder.getTitle().equals("")){
			presenter.respondPleaseSet("Title");
		}else if(builder.getLocation() == null || builder.getLocation().equals("")){
			presenter.respondPleaseSet("Location");
		}else if(builder.getStartTime() == null){
			presenter.respondPleaseSet("StartTime");
		}else if(builder.getEndTime() == null){
			presenter.respondPleaseSet("EndTime");
		}else{
			return true;
		}
		return false;
	}
	
	public boolean removeEvent(UUID event, EventEditPresent presenter){
		Event e = em.get(event);
		// remove task for speaker
		SpeakerTaskUpdate stu = new SpeakerTaskUpdate(false, presenter);
		e.accept(stu);
		// remove task for attendees
		tm.removeTasks(e.getSignedUp(), e, presenter);
		// un enroll the announcement group for speaker and organizer,
		// although this is technically not required, because
		// the announcement is going to get deleted anyways
		AnnouncementEnroll enroll = new AnnouncementEnroll(false);
		e.accept(enroll);
		// remove announcement group
		mm.removeAnnouncement(event);
		// remove scoring
		sm.removeEventScoring(event);
		// update attendee relations
		AttendeeRelationUpdate aru = new AttendeeRelationUpdate();
		e.accept(aru);
		// remove event
		em.remove(event);
		return true;
	}
	
	private static class EventBuilderFactory implements EventVisitor{
		
		private EventBuilder builder;
		
		public EventBuilder getBuilder(){
			return builder;
		}
		
		@Override
		public void visitTalk(Talk talk){
			TalkBuilder talkBuilder = new TalkBuilder();
			talkBuilder.setTitle(talk.getTitle());
			talkBuilder.setCapacity(talk.getCapacity());
			talkBuilder.setStartTime(talk.getStartTime());
			talkBuilder.setEndTime(talk.getEndTime());
			talkBuilder.setLocation(talk.getLocation());
			talkBuilder.setSpeaker(talk.getSpeaker());
			talkBuilder.setOrganizer(talk.getOrganizer());
			talkBuilder.setReward(talk.getReward());
			talkBuilder.setRequirement(talk.getRequirement());
			builder = talkBuilder;
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			NonSpeakerEventBuilder nseBuilder = new NonSpeakerEventBuilder();
			nseBuilder.setTitle(nse.getTitle());
			nseBuilder.setCapacity(nse.getCapacity());
			nseBuilder.setStartTime(nse.getStartTime());
			nseBuilder.setEndTime(nse.getEndTime());
			nseBuilder.setLocation(nse.getLocation());
			nseBuilder.setOrganizer(nse.getOrganizer());
			nseBuilder.setReward(nse.getReward());
			nseBuilder.setRequirement(nse.getRequirement());
			builder = nseBuilder;
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			MultiSpeakerEventBuilder mseBuilder = new MultiSpeakerEventBuilder();
			mseBuilder.setTitle(mse.getTitle());
			mseBuilder.setCapacity(mse.getCapacity());
			mseBuilder.setStartTime(mse.getStartTime());
			mseBuilder.setEndTime(mse.getEndTime());
			mseBuilder.setLocation(mse.getLocation());
			mseBuilder.setSpeakers(mse.getSpeakers());
			mseBuilder.setOrganizer(mse.getOrganizer());
			mseBuilder.setReward(mse.getReward());
			mseBuilder.setRequirement(mse.getRequirement());
			builder = mseBuilder;
		}
	}
	
	private class AttendeeRelationUpdate implements EventVisitor{
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() == null)
				return;
			fm.updateWorkRelation(talk.getSpeaker(), talk.getSignedUp(), uuid -> - 1);
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			// do nothing
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			if(mse.getSpeakers().size() == 0)
				return;
			mse.getSpeakers().forEach(s -> fm.updateWorkRelation(s, mse.getSignedUp(), uuid -> - 1));
		}
	}
	
	private class AnnouncementEnroll implements EventVisitor{
		
		private final boolean enroll;
		
		private AnnouncementEnroll(boolean enroll){
			this.enroll = enroll;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(enroll){
				if(talk.getSpeaker() != null)
					mm.enrollAnnouncementFor(talk.getID(), talk.getSpeaker(), AccessLevel.MANAGE);
				mm.enrollAnnouncementFor(talk.getID(), talk.getOrganizer(), AccessLevel.OWN);
			}else{
				if(talk.getSpeaker() != null)
					mm.unEnrollAnnouncementFor(talk.getID(), talk.getSpeaker());
				mm.unEnrollAnnouncementFor(talk.getID(), talk.getOrganizer());
			}
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			if(enroll){
				mm.enrollAnnouncementFor(nse.getID(), nse.getOrganizer(), AccessLevel.OWN);
			}else{
				mm.unEnrollAnnouncementFor(nse.getID(), nse.getOrganizer());
			}
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			if(enroll){
				mse.getSpeakers()
				   .forEach(speaker -> mm.enrollAnnouncementFor(mse.getID(), speaker, AccessLevel.MANAGE));
				mm.enrollAnnouncementFor(mse.getID(), mse.getOrganizer(), AccessLevel.OWN);
			}else{
				mse.getSpeakers()
				   .forEach(speaker -> mm.unEnrollAnnouncementFor(mse.getID(), speaker));
				mm.unEnrollAnnouncementFor(mse.getID(), mse.getOrganizer());
			}
		}
	}
	
	private class SpeakerTaskUpdate implements EventVisitor{
		
		private final boolean          add;
		private final EventEditPresent presenter;
		private       boolean          success = true;
		
		public SpeakerTaskUpdate(boolean add, EventEditPresent presenter){
			this.add = add;
			this.presenter = presenter;
		}
		
		public boolean isSuccess(){
			return success;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() == null)
				return;
			if(add)
				success = tm.createTask("speakerduty", talk.getSpeaker(), talk, presenter);
			else
				tm.removeTask(talk.getSpeaker(), talk, presenter);
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			// do nothing
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			if(mse.getSpeakers().size() == 0)
				return;
			if(add){
				success = tm.createTasks("speakerduty", mse.getSpeakers(), mse, presenter);
			}else
				tm.removeTasks(mse.getSpeakers(), mse, presenter);
		}
	}
}
