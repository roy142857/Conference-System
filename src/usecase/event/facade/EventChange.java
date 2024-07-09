package usecase.event.facade;

import entity.event.*;
import presenter.event.EventEditPresent;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.commu.MessagingManage;
import usecase.dto.builder.EventBuilder;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventChange implements Serializable{
	
	private final PeopleManage    pm;
	private final EventManage     em;
	private final TaskManage      tm;
	private final MessagingManage mm;
	private final FriendsManage   fm;
	
	public EventChange(PeopleManage pm, EventManage em, TaskManage tm, MessagingManage mm,
	                   FriendsManage fm){
		this.pm = pm;
		this.em = em;
		this.tm = tm;
		this.mm = mm;
		this.fm = fm;
	}
	
	public boolean changeEvent(UUID originalEvent, EventBuilder builder, EventEditPresent presenter){
		Event original = em.get(originalEvent);
		Event current = builder.build();
		if(! validateChange(original, current, presenter)){
			return false;
		}else if(em.isStarted(original)){
			presenter.respondAlreadyStarted();
		}else if(em.isStarted(current)){
			presenter.respondInvalidTime();
		}else{
			SpeakerUpdater updater = new SpeakerUpdater(current, presenter);
			original.accept(updater);
			if(updater.isSuccess()){
				applyChange(original, current);
				return true;
			}
		}
		return false;
	}
	
	private boolean validateChange(Event original, Event current, EventEditPresent presenter){
		// currently there are a few things that is not allowed to change:
		if(! current.getLocation().equals(original.getLocation())){
			presenter.respondChangeNotSupported("Location");
		}else if(! current.getStartTime().equals(original.getStartTime())){
			presenter.respondChangeNotSupported("Start time");
		}else if(! current.getEndTime().equals(original.getEndTime())){
			presenter.respondChangeNotSupported("Duration");
		}else if(current.getReward() != original.getReward()){
			presenter.respondChangeNotSupported("Reward");
		}else if(current.getRequirement() != original.getRequirement()){
			presenter.respondChangeNotSupported("Requirement");
		}else if(current.getCapacity() < original.getSignedUpCount()){
			presenter.respondCapacityTooSmall();
		}else{
			return true;
		}
		return false;
	}
	
	private void applyChange(Event original, Event current){
		original.setTitle(current.getTitle());
		original.setCapacity(current.getCapacity());
		original.accept(new SpeakerChanger(current));
	}
	
	private static class SpeakerChanger implements EventVisitor{
		
		private final Event current;
		
		private SpeakerChanger(Event current){
			this.current = current;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(! (current instanceof Talk)) throw new IllegalArgumentException("Can't change Talk to non Talk");
			Talk currentTalk = (Talk) current;
			talk.setSpeaker(currentTalk.getSpeaker());
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			if(! (current instanceof NonSpeakerEvent))
				throw new IllegalArgumentException("Can't change NonSpeakerEvent to non NonSpeakerEvent");
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			if(! (current instanceof MultiSpeakerEvent))
				throw new IllegalArgumentException("Can't change MultiSpeakerEvent to non MultiSpeakerEvent");
			MultiSpeakerEvent currentMSE = (MultiSpeakerEvent) current;
			mse.setSpeakers(currentMSE.getSpeakers());
		}
	}
	
	private class SpeakerUpdater implements EventVisitor{
		
		private final Event            current;
		private final EventEditPresent presenter;
		private       boolean          success = true;
		
		public SpeakerUpdater(Event current, EventEditPresent presenter){
			this.current = current;
			this.presenter = presenter;
		}
		
		public boolean isSuccess(){
			return success;
		}
		
		@Override
		public void visitTalk(Talk original){
			if(! (current instanceof Talk)) throw new IllegalArgumentException("Can't change Talk to non Talk");
			Talk currentTalk = (Talk) current;
			// now start the check
			if(currentTalk.getSpeaker() != null && original.getSpeaker() != null){
				// the speaker has changed
				if(! currentTalk.getSpeaker().equals(original.getSpeaker())){
					// if the new speaker is able to add task, remove the old speaker's task
					if(tm.createTask("speakerduty", currentTalk.getSpeaker(), original, presenter)){
						tm.removeTask(original.getSpeaker(), original, presenter);
						// we also update the announcement status
						mm.enrollAnnouncementFor(original.getID(), currentTalk.getSpeaker(), AccessLevel.MANAGE);
						mm.unEnrollAnnouncementFor(original.getID(), original.getSpeaker());
						// also the attendee's relations
						fm.updateWorkRelation(currentTalk.getSpeaker(), currentTalk.getSignedUp(), uuid -> + 1);
						fm.updateWorkRelation(original.getSpeaker(), currentTalk.getSignedUp(), uuid -> - 1);
					}else{
						success = false;
					}
				}
			}else if(currentTalk.getSpeaker() != null && original.getSpeaker() == null){
				if(tm.createTask("speakerduty", currentTalk.getSpeaker(), original, presenter)){
					mm.enrollAnnouncementFor(original.getID(), currentTalk.getSpeaker(), AccessLevel.MANAGE);
					fm.updateWorkRelation(currentTalk.getSpeaker(), currentTalk.getSignedUp(), uuid -> + 1);
				}else{
					success = false;
				}
			}else if(currentTalk.getSpeaker() == null && original.getSpeaker() != null){
				tm.removeTask(original.getSpeaker(), original, presenter);
				mm.unEnrollAnnouncementFor(original.getID(), original.getSpeaker());
				fm.updateWorkRelation(original.getSpeaker(), currentTalk.getSignedUp(), uuid -> - 1);
			}
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			if(! (current instanceof NonSpeakerEvent))
				throw new IllegalArgumentException("Can't change NonSpeakerEvent to non NonSpeakerEvent");
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent original){
			if(! (current instanceof MultiSpeakerEvent))
				throw new IllegalArgumentException("Can't change MultiSpeakerEvent to non MultiSpeakerEvent");
			MultiSpeakerEvent currentMSE = (MultiSpeakerEvent) current;
			// now start the check
			if(currentMSE.getSpeakers().size() != 0 && original.getSpeakers().size() != 0){
				// the speaker has changed
				if(! currentMSE.getSpeakers().equals(original.getSpeakers())){
					Set<UUID> removed = original.getSpeakers().stream()
					                            .filter(s -> ! currentMSE.getSpeakers().contains(s))
					                            .collect(Collectors.toSet());
					Set<UUID> added = currentMSE.getSpeakers().stream()
					                            .filter(s -> ! original.getSpeakers().contains(s))
					                            .collect(Collectors.toSet());
					// if the new speakers can add tasks, remove the removed speaker's tasks
					if(tm.createTasks("speakerduty", added, original, presenter)){
						tm.removeTasks(removed, original, presenter);
						// update the announcement access
						mm.enrollAnnouncementFor(original.getID(), added, AccessLevel.MANAGE);
						mm.unEnrollAnnouncementFor(original.getID(), removed);
						// update the attendee's relation
						added.forEach(newSpk -> fm.updateWorkRelation(newSpk, currentMSE.getSignedUp(), uuid -> + 1));
						removed.forEach(rmvSpk -> fm.updateWorkRelation(rmvSpk, currentMSE.getSignedUp(), uuid -> - 1));
					}else{
						success = false;
					}
				}
			}else if(currentMSE.getSpeakers().size() != 0 && original.getSpeakers().size() == 0){
				if(tm.createTasks("speakerduty", currentMSE.getSpeakers(), original, presenter)){
					mm.enrollAnnouncementFor(original.getID(), currentMSE.getSpeakers(), AccessLevel.MANAGE);
					currentMSE.getSpeakers()
					          .forEach(newSpk -> fm.updateWorkRelation(newSpk, currentMSE.getSignedUp(), uuid -> + 1));
				}else{
					success = false;
				}
			}else if(currentMSE.getSpeakers().size() == 0 && original.getSpeakers().size() != 0){
				tm.removeTasks(original.getSpeakers(), original, presenter);
				mm.unEnrollAnnouncementFor(original.getID(), original.getSpeakers());
				original.getSpeakers()
				        .forEach(rmvSpk -> fm.updateWorkRelation(rmvSpk, currentMSE.getSignedUp(), uuid -> - 1));
			}
		}
	}
}
