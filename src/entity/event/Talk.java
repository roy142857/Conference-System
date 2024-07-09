package entity.event;

import entity.interfaces.OccupancyLevel;

import java.util.UUID;

/**
 * Talk represents a one-speaker Talk happens at a Conference, with a {@code OCCUPANCY_SPECIFIC}
 * level of Occupancy to the Schedule.
 */
public class Talk extends Event{

	private UUID speaker;
	
	public Talk(){
		super();
	}
	
	@Override
	public void accept(EventVisitor visitor){
		visitor.visitTalk(this);
	}
	
	/**
	 * Get the occupancy level of this {@code Talk},
	 * which is {@code OccupancyLevel.OCCUPANCY_SPECIFIC}
	 *
	 * @return {@code OccupancyLevel.OCCUPANCY_SPECIFIC}
	 * @see OccupancyLevel
	 */
	@Override
	public OccupancyLevel getOccupancyLevel(){
		return OccupancyLevel.OCCUPANCY_SPECIFIC;
	}

	/**
	 * Get the ID of the speaker of this Event.
	 *
	 * @return the speaker.
	 */
	public UUID getSpeaker(){
		return speaker;
	}

	/**
	 * Set the Speaker of the Event.
	 */
	public void setSpeaker(UUID speaker){
		this.speaker = speaker;
	}
}
