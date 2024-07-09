package entity.task;

import entity.interfaces.OccupancyLevel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SpeakerDuty is a task for people who talks at some Event (the speakers).
 */
public class SpeakerDuty extends Task{
	
	/**
	 * Construct a SpeakerDuty.
	 *
	 * @param eventID the id of the Event that this Task is constructed from
	 * @param location the location of this Task.
	 * @param startTime the start time of this Task.
	 * @param endTime the end time of this Task.
	 */
	public SpeakerDuty(UUID eventID,
	                   String location, LocalDateTime startTime, LocalDateTime endTime){
		super(eventID, location, startTime, endTime);
	}
	
	@Override
	public void accept(TaskVisitor visitor){
		visitor.visitSpeakerDuty(this);
	}
	
	/**
	 * An SpeakerDuty occupies an absolute level on an Schedule.
	 *
	 * @return {@code OCCUPANCY_ABSOLUTE}
	 * @see OccupancyLevel
	 */
	@Override
	public OccupancyLevel getOccupancyLevel(){
		return OccupancyLevel.OCCUPANCY_ABSOLUTE;
	}
}
