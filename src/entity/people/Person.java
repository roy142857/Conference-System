package entity.people;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Person represents any human beings at this conference.
 */
public abstract class Person implements Serializable{
	
	private final UUID   id;
	private       String name;
	private       int    points;
	
	/**
	 * Construct a Person.
	 *
	 * @param name the display name of this Person.
	 */
	public Person(String name){
		this(UUID.randomUUID(), name);
		this.points = 0;
	}
	
	/**
	 * Construct a Person.
	 *
	 * @param id the id of this Person.
	 * @param name the display name of this Person.
	 */
	public Person(UUID id, String name){
		this.id = id;
		this.name = name;
	}
	
	public abstract void accept(PeopleVisitor visitor);
	
	/**
	 * Get the display name for this Person.
	 *
	 * Display name is the name displayed on the profile
	 *
	 * @return the display name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set a new display name of this Person
	 *
	 * @param displayName the new display name of this Person.
	 */
	public void setName(String displayName){
		this.name = displayName;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int points){
		this.points = points;
	}
	
	/**
	 * Get the Unique Identifier for this Object.
	 *
	 * @return a {@code UUID} that uniquely identifies this Object.
	 */
	public UUID getID(){
		return id;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(id, person.id);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
	
	/**
	 * Check can this person attend some event, in another words, check does this person
	 * have Attendee Privilege
	 *
	 * @return the true if this person have Attendee privilege.
	 */
	public abstract boolean canAttend();
	
	/**
	 * Check can this person speak, in another words, check does this person
	 * have Speaker Privilege
	 *
	 * @return the true if this person have Speaker privilege.
	 */
	public abstract boolean canSpeak();
	
	/**
	 * Check can this person organise, in another words, check does this person
	 * have Organizer Privilege
	 *
	 * @return the true if this person have Organizer privilege.
	 */
	public abstract boolean canOrganize();
	
}
