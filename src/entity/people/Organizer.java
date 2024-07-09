package entity.people;

/**
 * Organizer represents the employees of a conference.
 *
 * They can manipulate events and add announcement, and do a series of things.
 */
public class Organizer extends Person{
	
	/**
	 * Construct a Organizer
	 *
	 * @param name the display name of this Person
	 */
	public Organizer(String name){
		super(name);
	}
	
	@Override
	public void accept(PeopleVisitor visitor){
		visitor.visitOrganizer(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false.
	 */
	@Override
	public boolean canAttend(){
		return false;
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
	 * @return always true.
	 */
	@Override
	public boolean canOrganize(){
		return true;
	}
}
