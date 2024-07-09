package usecase.schedule;

import entity.interfaces.IEvent;
import entity.interfaces.OccupancyLevel;
import exception.schedule.ScheduleException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * A Schedule manages a collection of {@code IEvent}, ensures no conflicting {@code IEvent}
 * based on the time period and location they occupy and it's occupancy level.
 *
 * <p>
 * Two {@code IEvents} are overlapping if they occupy an overlapping time period.
 * </p>
 *
 * <p>
 * Class Invariant:
 * </br>
 * For every pair IEvents A, B in the Schedule, if they overlaps, then:
 * <ul>
 *     <li>A and B don't have {@code OCCUPANCY_ABSOLUTE}.</li>
 *     <li>If A have {@code OCCUPANCY_SPECIFIC}, then A and B occupy a different location.</li>
 *     <li>If A have {@code OCCUPANCY_NO}, then B can occupy any location.</li>
 * </ul>
 * Some other Class Invariant:
 * <ul>
 *     <li>if {@code getConflicting(e).length == 0} then {@code add(e)} won't throws an exception.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class provides underlying logic for {@link usecase.event.EventManage} and {@link usecase.task.TaskManage}
 * to ensures no double booking of event or task!
 * </p>
 *
 * @param <E> a subclass of IEvent
 * @see OccupancyLevel
 */
public interface Schedule<E extends IEvent> extends Serializable, Iterable<E>{
	
	/**
	 * Add an {@code IEvent} to this Schedule, and ensures no conflicting events that breaks the
	 * class invariant. If an {@code IEvent} failed to add, it shouldn't be added.
	 *
	 * <p>
	 * If the {@code IEvent} failed to add it throws an {@link ScheduleException} contains
	 * the list of conflicting events.
	 * </p>
	 *
	 * @param e the {@code IEvent} to add.
	 * @throws ScheduleException if the {@code IEvent} can't be added.
	 */
	void add(E e);
	
	/**
	 * Get the IEvent with the id.
	 *
	 * @param id the id;
	 * @return an IEvent.
	 */
	E get(UUID id);
	
	/**
	 * Remove an {@code IEvent} from this Schedule.
	 * If such {@code IEvent} not exist, do nothing.
	 *
	 * @param e the {@code IEvent} to remove.
	 */
	void remove(UUID e);
	
	/**
	 * Remove {@code IEvent} from this Schedule.
	 * If the predicate is true.
	 *
	 * @param predicate remove if the predicate is true.
	 */
	void removeIf(Predicate<E> predicate);
	
	/**
	 * Check if any IEvent schedule matches the predicate.
	 *
	 * @param predicate the predicate to check.
	 * @return true if any of the IEvent
	 */
	boolean anyMatch(Predicate<E> predicate);
	
	/**
	 * Check the {@code IEvent} belongs to this Schedule.
	 *
	 * @param e the {@code IEvent} to check.
	 * @return true iff the event belongs here, false otherwise.
	 */
	boolean has(UUID e);
	
	/**
	 * Get all the {@code IEvent} stored inside this Schedule.
	 *
	 * @return all the {@code IEvent} stored inside this Schedule as a List.
	 */
	
	List<UUID> getAll();
	
	/**
	 * Get an list of {@code IEvent}, that is conflicting with the {@code IEvent} provided.
	 * The example use of this method is to assist organizer to plan out events.
	 *
	 * To check the conflicting events, you should first check it's occupancy level:
	 * - if the new event has OCCUPANCY_ABSOLUTE, then iterate through all events
	 * to find any events that has overlapping time to the new event. Those
	 * events are conflicting with the new event, because OCCUPANCY_ABSOLUTE
	 * don't allow anything else with an overlapping time.
	 * - if the new event has OCCUPANCY_SPECIFIC, then iterate through all events
	 * to find any events that has overlapping time and has same location to the new event,
	 * Those events are conflicting with the new event, because OCCUPANCY_SPECIFIC don't
	 * allow anything else that has an overlapping time and at the same location.
	 * - if the new event has OCCUPANCY_NO, then there are no conflicting events,
	 * because OCCUPANCY_NO allows anything else at the same or different location and time.
	 *
	 * @param e the {@code IEvent} to check.
	 * @return a list of {@code IEvent}.
	 */
	List<UUID> getConflicting(E e);
}
