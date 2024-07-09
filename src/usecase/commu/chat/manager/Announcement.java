package usecase.commu.chat.manager;

import exception.social.SocialException;
import usecase.commu.chat.manager.access.AccessLevel;

import java.util.UUID;

/**
 * Announcement group, for messaging all attendee at an announcement.
 */
public class Announcement extends ChatManager{
	
	private final UUID event;
	
	public Announcement(UUID event, String name){
		super(name);
		this.event = event;
	}
	
	public void enrollFor(UUID person, AccessLevel initialAccess){
		if(! hasAccess(person, AccessLevel.VIEW)){
			createFor(person, initialAccess);
		}else{
			setAccess(person, initialAccess);
		}
	}
	
	public void unEnrollFor(UUID person){
		if(! hasAccess(person, AccessLevel.VIEW))
			throw new SocialException("Not Yet Enrolled!");
		setAccess(person, AccessLevel.VIEW);
	}
	
	public boolean isAnnouncementFor(UUID event){
		return this.event.equals(event);
	}
}
