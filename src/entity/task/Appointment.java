package entity.task;

import entity.interfaces.OccupancyLevel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Appointment is a task for people that indicates they have signed up to that event.
 */
public class Appointment extends Task{
	
	/**
	 * Construct a Appointment.
	 *
	 * @param eventID the id of the Event that this Task is constructed from
	 * @param location the location of this Task.
	 * @param startTime the start time of this Task.
	 * @param endTime the end time of this Task.
	 */
	public Appointment(UUID eventID,
	                   String location, LocalDateTime startTime, LocalDateTime endTime){
		super(eventID, location, startTime, endTime);
	}
	
	@Override
	public void accept(TaskVisitor visitor){
		visitor.visitAppointment(this);
	}
	
	/**
	 * An Appointment occupies absolutely on an Schedule.
	 *
	 * @return {@code OCCUPANCY_ABSOLUTE}.
	 * @see OccupancyLevel
	 */
	@Override
	public OccupancyLevel getOccupancyLevel(){
		return OccupancyLevel.OCCUPANCY_ABSOLUTE;
	}
}
