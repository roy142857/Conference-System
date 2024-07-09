package usecase.schedule;

import entity.interfaces.IEvent;
import entity.interfaces.OccupancyLevel;
import exception.schedule.ScheduleException;

import java.util.*;
import java.util.function.Predicate;

public class Scheduler<E extends IEvent> implements Schedule<E>{
	
	public final Map<UUID, E> schedule;
	
	/**
	 * Construct Scheduler as a LinkedHashMap.
	 */
	public Scheduler(){
		this.schedule = new LinkedHashMap<>();
	}
	
	/**
	 * add an event to the schedule if the iEvent is not conflicting with other events.
	 * using {@code getConflicting(Event)}
	 *
	 * @param iEvent the {@code IEvent} to add.
	 * @throws ScheduleException if the event can't be added.
	 */
	@Override
	public void add(E iEvent){
		if(getConflicting(iEvent).size() != 0){
			throw new ScheduleException("Cannot add this event to the schedule since ");
		}
		// call to schedule.add
		schedule.put(iEvent.getID(), iEvent);
	}
	
	@Override
	public E get(UUID id){
		return schedule.get(id);
	}
	
	/**
	 * remove an {@code IEvent} from the schedule.
	 * if such {@code IEvent} not exist, do nothing.
	 *
	 * @param iEvent the {@code IEvent} to remove.
	 */
	@Override
	public void remove(UUID iEvent){
		schedule.remove(iEvent);
	}
	
	/**
	 * remove {@code IEvent} from the schedule.
	 * if the predicate is true.
	 *
	 * @param predicate a predicate.
	 */
	@Override
	public void removeIf(Predicate<E> predicate){
		schedule.entrySet().removeIf(entry -> predicate.test(entry.getValue()));
		
	}
	
	@Override
	public boolean anyMatch(Predicate<E> predicate){
		return schedule.entrySet().stream().anyMatch(entry -> predicate.test(entry.getValue()));
	}
	
	/**
	 * Check whether {@code IEvent} is in this Schedule.
	 *
	 * @param iEvent the {@code IEvent} to check.
	 * @return a {@code boolean} of whether the schedule has this event.
	 */
	@Override
	public boolean has(UUID iEvent){
		return schedule.containsKey(iEvent);
	}
	
	/**
	 * Get all the {@code IEvent} stored inside this Schedule.
	 *
	 * @return all the {@code IEvent} stored inside this Schedule as a List.
	 */
	@Override
	public List<UUID> getAll(){
		return new ArrayList<>(schedule.keySet());
	}
	
	/**
	 * Get an list of {@code IEvent}, that is conflicting with the {@code IEvent} provided.
	 * The example use of this method is to assist organizer to plan out events.
	 *
	 * @param e1 the {@code IEvent} to check.
	 * @return a list of {@code IEvent}.
	 */
	@Override
	public List<UUID> getConflicting(E e1){
		List<UUID> lst = new ArrayList<>();
		// check time conflicting!
		if(e1.getOccupancyLevel() == OccupancyLevel.OCCUPANCY_ABSOLUTE){
			for(Map.Entry<UUID, E> entry : schedule.entrySet()){
				UUID key = entry.getKey();
				E e2 = entry.getValue();
				// we ignore the OCCUPANCY_NO
				if(e2.getOccupancyLevel() == OccupancyLevel.OCCUPANCY_NO)
					continue;
				// if the time overlaps, then e1 and e2 is conflicting
				if(isOverlapping(e1, e2)){
					lst.add(key);
				}
			}
		}else if(e1.getOccupancyLevel() == OccupancyLevel.OCCUPANCY_SPECIFIC){
			for(Map.Entry<UUID, E> entry : schedule.entrySet()){
				UUID key = entry.getKey();
				E e2 = entry.getValue();
				// we ignore the OCCUPANCY_NO
				if(e2.getOccupancyLevel() == OccupancyLevel.OCCUPANCY_NO)
					continue;
				// if the time overlaps, and e2 occupies the same location as e1.
				// then e1 and e2 is conflicting
				if(isOverlapping(e1, e2) && e1.getLocation().equals(e2.getLocation())){
					lst.add(key);
				}
			}
		}
		return lst;
	}
	
	/**
	 * Return true iff e1 and e2 contains an overlapping period.
	 *
	 * @param e1 the event1 to check with event2
	 * @param e2 the event2 to check with event1
	 * @return a {@code boolean} of whether these two events are overlapping.
	 */
	private boolean isOverlapping(E e1, E e2){
		// here is the possible e1 and e2 configuration :
		//         |------|        <- e1
		//  |------|               <- ok
		//      |------|           <- overlap
		//         |------|        <- overlap
		//             |------|    <- overlap
		//                |------| <- ok
		// we can see that three cases overlaps,
		// so the only cases where e1 and e1 is not overlapping is when
		// - e1 is start after e2 ends or
		// - e1 is end before e2 start
		// and any other cases overlaps
		return ! (e1.getStartTime().compareTo(e2.getEndTime()) >= 0 ||
		          e1.getEndTime().compareTo(e2.getStartTime()) <= 0);
	}
	
	@Override
	public Iterator<E> iterator(){
		return schedule.values().iterator();
	}
}
