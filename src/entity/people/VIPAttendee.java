package entity.people;

import java.util.UUID;

/**
 * Attendee represents the vip visitors at a conference.
 */
public class VIPAttendee extends Person{
	
	/**
	 * Construct a VIPAttendee
	 *
	 * @param name the display name of this VIPAttendee
	 */
	public VIPAttendee(UUID id, String name){
		super(id, name);
	}
	
	@Override
	public void accept(PeopleVisitor visitor){
		visitor.visitVIPAttendee(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always true.
	 */
	@Override
	public boolean canAttend(){
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false.
	 */
	@Override
	public boolean canSpeak(){
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false.
	 */
	@Override
	public boolean canOrganize(){
		return false;
	}
}
