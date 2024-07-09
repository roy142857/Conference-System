package usecase.event.facade;

import entity.event.*;
import entity.people.Person;
import presenter.event.EventSignupPresent;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.commu.MessagingManage;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.UUID;

public class EventSignup implements Serializable{
	
	private final PeopleManage    pm;
	private final EventManage     em;
	private final TaskManage      tm;
	private final MessagingManage mm;
	private final FriendsManage   fm;
	
	public EventSignup(PeopleManage pm, EventManage em, TaskManage tm, MessagingManage mm,
	                   FriendsManage fm){
		this.pm = pm;
		this.em = em;
		this.tm = tm;
		this.mm = mm;
		this.fm = fm;
	}
	
	public boolean signUp(UUID event, UUID person, EventSignupPresent presenter){
		Event e = em.get(event);
		Person p = pm.get(person);
		if(em.isStarted(e)){
			presenter.respondEventStarted();
		}else if(! haveEnoughSpace(e)){
			presenter.respondEventFull();
		}else if(isSignedUp(e, person)){
			presenter.respondAlreadySignedUp();
		}else if(! checkRequirement(e, p)){
			presenter.respondRequirementNotMeet();
		}else{
			if(tm.createTask("appointment", person, e, presenter)){
				mm.enrollAnnouncementFor(event, person, AccessLevel.SYNC);
				e.accept(new SpeakerWorkRelationUpdate(person, true));
				// call add points on peopleManage to trigger VIP Promote, see people.facade.VIPPromote
				pm.addPoints(person, e.getReward(), presenter);
				addSignUp(e, person);
				return true;
			}
		}
		return false;
	}
	
	public boolean cancelSignUp(UUID event, UUID person, EventSignupPresent presenter){
		Event e = em.get(event);
		if(em.isStarted(e)){
			presenter.respondEventStarted();
		}else if(! isSignedUp(e, person)){
			presenter.respondNotYetSignedUp();
		}else{
			tm.removeTask(person, e, presenter);
			mm.unEnrollAnnouncementFor(event, person);
			e.accept(new SpeakerWorkRelationUpdate(person, false));
			// call add points on peopleManage to trigger VIP Demote, see people.facade.VIPPromote
			pm.deductPoints(person, e.getReward(), presenter);
			removeSignUp(e, person);
			return true;
		}
		return false;
	}
	
	private boolean checkRequirement(Event event, Person person){
		return person.getPoints() >= event.getRequirement();
	}
	
	private boolean isSignedUp(Event event, UUID person){
		return event.isSignedUp(person);
	}
	
	private void addSignUp(Event event, UUID person){
		event.signUp(person);
	}
	
	private void removeSignUp(Event event, UUID person){
		event.removeSignUp(person);
	}
	
	private boolean haveEnoughSpace(Event e){
		return e.getSignedUpCount() <= e.getCapacity() - 1;
	}
	
	private class SpeakerWorkRelationUpdate implements EventVisitor{
		
		private final UUID    person;
		private final boolean add;
		
		private SpeakerWorkRelationUpdate(UUID person, boolean add){
			this.person = person;
			this.add = add;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() == null)
				return;
			if(add)
				fm.addWorkRelation(person, talk.getSpeaker());
			else
				fm.removeWorkRelation(person, talk.getSpeaker());
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			// do nothing
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			if(mse.getSpeakers().size() == 0)
				return;
			if(add)
				fm.updateWorkRelation(person, mse.getSpeakers(), uuid -> + 1);
			else
				fm.updateWorkRelation(person, mse.getSpeakers(), uuid -> - 1);
		}
	}
}
