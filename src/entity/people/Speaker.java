package entity.people;

/**
 * Speaker represents the people who speak at a conference.
 *
 * They can add announcement, and do a series of things.
 */
public class Speaker extends Person{
	
	/**
	 * Construct a Speaker
	 *
	 * @param name the display name of this Person
	 */
	public Speaker(String name){
		super(name);
	}
	
	@Override
	public void accept(PeopleVisitor visitor){
		visitor.visitSpeaker(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false. In Phase 1, speaker can't attend.
	 */
	@Override
	public boolean canAttend(){
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always true.
	 */
	@Override
	public boolean canSpeak(){
		return true;
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
