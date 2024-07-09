package entity.interfaces;

import usecase.schedule.Schedule;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The Objects who implements this interface Occupy a time period and
 * location inside a {@code Schedule}, and indicates their OccupancyLevel.
 *
 * @see Schedule
 * @see OccupancyLevel
 */
public abstract class IEvent implements Serializable{
	
	private final UUID id;
	
	public IEvent(){
		this(UUID.randomUUID());
	}
	
	protected IEvent(UUID id){
		this.id = id;
	}
	
	/**
	 * Get the Unique Identifier for this Object.
	 *
	 * @return a {@code UUID} that uniquely identifies this Object.
	 */
	public UUID getID(){
		return id;
	}
	
	/**
	 * Get the location it occupy on {@code Schedule}.
	 *
	 * @return a String represents the location
	 */
	public abstract String getLocation();
	
	/**
	 * Get the start time of it occupy on {@code Schedule}.
	 *
	 * @return a {@code LocalDateTime} represents the start time.
	 */
	public abstract LocalDateTime getStartTime();
	
	/**
	 * Get the end time of it occupy on {@code Schedule}.
	 *
	 * @return a {@code LocalDateTime} represents the end time.
	 */
	public abstract LocalDateTime getEndTime();
	
	/**
	 * Get the occupancy level of it for {@code Schedule}.
	 *
	 * @return a {@code OccupancyLevel}
	 * @see OccupancyLevel
	 */
	public abstract OccupancyLevel getOccupancyLevel();
	
	/**
	 * Get the duration of this.
	 *
	 * @return a {@code Duration} between the start and end time.
	 */
	public Duration getDuration(){
		return Duration.between(getStartTime(), getEndTime());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		IEvent iEvent = (IEvent) o;
		return Objects.equals(id, iEvent.id);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}
