package entity.task;

import entity.interfaces.IEvent;
import usecase.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Task is "wrapper" for event, meaning every task correspond to an event, and
 * they should contains the same information.
 *
 * <p>
 * Tasks can only be added to {@link Schedule} of person,
 * </p>
 *
 * An example use of Task is when the organizer assigning speakers:
 * Every time a speaker is assigned to some event a {@link SpeakerDuty} for that event
 * is added to their {@link Schedule}, this way we can ensure no
 * double booking of speakers.
 */
public abstract class Task extends IEvent{
	
	private final UUID          eventID;
	private       String        location;
	private       LocalDateTime startTime;
	private       LocalDateTime endTime;
	
	/**
	 * Construct a AbstractTask.
	 *
	 * @param eventID the id of the Event that this Task is constructed from
	 * @param location the location of this Task.
	 * @param startTime the start time of this Task.
	 * @param endTime the end time of this Task.
	 */
	public Task(UUID eventID,
	            String location, LocalDateTime startTime, LocalDateTime endTime){
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventID = eventID;
	}
	
	/**
	 * Visitor Design Patten.
	 *
	 * @param visitor a {@code TaskVisitor}.
	 * @see TaskVisitor
	 */
	public abstract void accept(TaskVisitor visitor);
	
	/**
	 * Return the Id of the Event that this Task is constructed from.
	 *
	 * @return the task for some event.
	 */
	public UUID getTaskFor(){
		return eventID;
	}
	
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
	 * Set the location of this {@code Task}.
	 */
	public void setLocation(String location){
		this.location = location;
	}
	
	/**
	 * Set the start time of this {@code Task}.
	 */
	public void setStartTime(LocalDateTime startTime){
		this.startTime = startTime;
	}
	
	/**
	 * Set the end time of this {@code Task}.
	 */
	public void setEndTime(LocalDateTime endTime){
		this.endTime = endTime;
	}
}
