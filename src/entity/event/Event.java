package entity.event;

import entity.interfaces.IEvent;
import usecase.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * An Event is a subclass of {@code IEvent} meaning it occupies a time and location
 * for an {@link Schedule}, specifically, an Event can only be added
 * to {@link Schedule} of the conference.
 *
 * This way when the organizer plans the event, there won't be double booking of the time
 * or location, this is enforced in {@link Schedule}.
 */
public abstract class Event extends IEvent{
	
	private       String        title;
	private       int           reward;
	private       int           requirement;
	// Organizer and Speaker
	private final Set<UUID>     signedUp;
	private       UUID          organizer;
	// signup control
	private       int           capacity;
	// IEvent
	private       String        location;
	private       LocalDateTime startTime;
	private       LocalDateTime endTime;
	
	/**
	 * Construct an Event.
	 */
	public Event(){
		this.title = "";
		this.organizer = null;
		this.signedUp = new HashSet<>();
		this.capacity = 0;
		this.location = "";
		this.startTime = null;
		this.endTime = null;
		this.reward = 0;
	}
	
	/**
	 * Visitor Design Patten.
	 *
	 * @param visitor a {@code EventVisitor}.
	 * @see EventVisitor
	 */
	public abstract void accept(EventVisitor visitor);
	
	@Override
	public String getLocation(){
		return location;
	}
	
	@Override
	public LocalDateTime getStartTime(){
		return startTime;
	}
	
	@Override
	public LocalDateTime getEndTime(){
		return endTime;
	}
	
	/**
	 * Set the location of this {@code Event}.
	 */
	public void setLocation(String location){
		this.location = location;
	}
	
	/**
	 * Set the start time of this {@code Event}.
	 */
	public void setStartTime(LocalDateTime startTime){
		this.startTime = startTime;
	}
	
	/**
	 * Set the end time of this {@code Event}.
	 */
	public void setEndTime(LocalDateTime endTime){
		this.endTime = endTime;
	}
	
	/**
	 * Get the title of this Event
	 *
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Set tht title of this {@code Event}
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Get the capacity of this Event.
	 *
	 * @return the capacity of this Event.
	 */
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * Set the capacity of this Event.
	 *
	 * @throws IllegalArgumentException if the capacity is less than 0.
	 */
	public void setCapacity(int capacity){
		if(capacity < 0)
			throw new IllegalArgumentException("Capacity can't be negative!");
		this.capacity = capacity;
	}
	
	/**
	 * Sign up a people, by adding it to signed up list.
	 *
	 * @param person a people who wants to sign up.
	 * @throws IllegalArgumentException if there isn't enough capacity for the person
	 * or the person is already in the list of signed up.
	 */
	public void signUp(UUID person){
		// core business logic, the signup can't exceed the capacity
		if(signedUp.size() > capacity - 1)
			throw new IllegalArgumentException("Signup count can't exceed Capacity!");
		if(signedUp.contains(person))
			throw new IllegalArgumentException("Same People can't signup twice!");
		this.signedUp.add(person);
	}
	
	/**
	 * Remove the people from signed up list.
	 *
	 * @param person the Person.
	 * @throws IllegalArgumentException if the person hasn't signedUp yet.
	 */
	public void removeSignUp(UUID person){
		if(! signedUp.contains(person))
			throw new IllegalArgumentException("The person hasn't signed up yet!");
		this.signedUp.remove(person);
	}
	
	/**
	 * Get the set of id of the people who is signed up for the event.
	 *
	 * @return a set of id.
	 */
	public Set<UUID> getSignedUp(){
		return signedUp;
	}
	
	/**
	 * Check is the people signed up.
	 *
	 * @param people a people's ID
	 * @return true if the people is signed up already, otherwise false.
	 */
	public boolean isSignedUp(UUID people){
		return signedUp.contains(people);
	}
	
	/**
	 * Get the number of people that is already signed up.
	 *
	 * @return the size of the signup list.
	 */
	public int getSignedUpCount(){
		return signedUp.size();
	}
	
	/**
	 * Get the ID of the organizer of this Event.
	 *
	 * @return the organizer
	 */
	public UUID getOrganizer(){
		return organizer;
	}
	
	/**
	 * Set the Organizer of the Event.
	 */
	public void setOrganizer(UUID organizer){
		this.organizer = organizer;
	}
	
	/**
	 * Get the reward of this Event
	 *
	 * @return the points
	 */
	public int getReward(){ return reward; }
	
	/**
	 * Set the reward of this Event
	 *
	 * @param reward the points a user attend this Event could get
	 */
	public void setReward(int reward){ this.reward = reward; }
	
	/**
	 * Get the requirement of this Event
	 *
	 * @return the requirement
	 */
	public int getRequirement(){ return requirement; }
	
	/**
	 * Set the requirement of this Event
	 *
	 * @param requirement the requirement a user that attend this Event needs
	 */
	public void setRequirement(int requirement){ this.requirement = requirement; }
}
