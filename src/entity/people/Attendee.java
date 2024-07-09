package entity.people;

import java.util.UUID;

/**
 * Attendee represents the visitors at a conference.
 */
public class Attendee extends Person{
	
	/**
	 * Construct a Attendee
	 *
	 * @param name the display name of this Person
	 */
	public Attendee(String name){
		super(name);
	}

	public Attendee(UUID id, String name){
		super(id, name);
	}

	@Override
	public void accept(PeopleVisitor visitor){
		visitor.visitAttendee(this);
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
