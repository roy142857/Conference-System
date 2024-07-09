package usecase.people.facade;

import entity.people.*;
import entity.people.factory.PersonFactory;
import presenter.people.PeopleCreatePresent;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.people.PeopleManager;
import usecase.people.PersonType;

import java.io.Serializable;
import java.util.UUID;

public class PeopleCreate implements PeopleVisitor, Serializable{
	
	private final PeopleManage  pm;
	private final FriendsManage fm;
	
	public PeopleCreate(PeopleManager pm, FriendsManage fm){
		this.pm = pm;
		this.fm = fm;
	}
	
	/**
	 * Create a person by type, name and  password, it might raise ItHasSameNameAlready
	 * if name is already existed.
	 *
	 * @param type the type
	 * @param username the username
	 * @param password the password
	 */
	public UUID create(PersonType type, String username, char[] password, PeopleCreatePresent presenter){
		if(username.equals("")){
			presenter.respondEmtpyUsername();
		}else if(password.length == 0){
			presenter.respondEmtpyPassword();
		}else if(pm.hasSameUsername(username)){
			presenter.respondUsernameExist();
		}else{
			Person p = create(type, username, password);
			p.accept(this);
			return p.getID();
		}
		return null;
	}
	
	/**
	 * Create a person by type, name and  password, it might raise ItHasSameNameAlready
	 * if name is already existed.
	 *
	 * @param type the type
	 * @param username the username
	 * @param password the password
	 */
	public UUID debugCreate(PersonType type, String username, char[] password){
		if(pm.hasSameUsername(username)){
			return null;
		}else{
			Person p = create(type, username, password);
			p.accept(this);
			return p.getID();
		}
	}
	
	private Person create(PersonType type, String username, char[] password){
		Person p = new PersonFactory().create(type, username);
		pm.put(p);
		pm.putPassword(p.getName(), password);
		return p;
	}
	
	@Override
	public void visitOrganizer(Organizer organizer){
		// link organizer to speakers
		fm.updateWorkRelation(
				organizer.getID(),
				pm.getAllSpeaker(),
				uuid -> + 1
		);
		// link organizer to attendees
		fm.updateWorkRelation(
				organizer.getID(),
				pm.getAllAttendee(),
				uuid -> + 1
		);
	}
	
	@Override
	public void visitSpeaker(Speaker speaker){
		// link speaker to organizers
		fm.updateWorkRelation(
				speaker.getID(),
				pm.getAllOrganizer(),
				uuid -> + 1
		);
	}
	
	@Override
	public void visitAttendee(Attendee attendee){
		// link attendee to organizers
		fm.updateWorkRelation(
				attendee.getID(),
				pm.getAllOrganizer(),
				uuid -> + 1
		);
	}
	
	@Override
	public void visitVIPAttendee(VIPAttendee vipAttendee){
		// link attendee to organizers
		fm.updateWorkRelation(
				vipAttendee.getID(),
				pm.getAllOrganizer(),
				uuid -> + 1
		);
	}
}
