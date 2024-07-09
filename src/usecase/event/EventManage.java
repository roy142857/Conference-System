package usecase.event;

import entity.event.Event;
import presenter.center.EventCenterPresent;
import presenter.event.EventEditPresent;
import presenter.event.EventSignupPresent;
import presenter.event.EventViewPresent;
import usecase.commu.MessagingManage;
import usecase.dto.builder.EventBuilder;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.schedule.Schedule;
import usecase.score.ScoreManage;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * EventManage contains all {@link Event} for a conference.
 * EventManage uses {@link Schedule} to ensure no double booking of time and location.
 *
 * @see Schedule
 */
public interface EventManage extends Serializable{
	
	void initialize(PeopleManage pm, TaskManage tm, MessagingManage mm, FriendsManage fm,
	                ScoreManage sm);
	
	/**
	 * Update the view applying the search property with the keyword.
	 *
	 * @param searchProperty the property the keyword is applied on
	 * @param keyword the keyword
	 */
	void updateView(EventSearchProperty searchProperty, String keyword, EventCenterPresent presenter);
	
	void updateView(UUID event, EventViewPresent presenter);
	
	/**
	 * Create and add an Event, this method should use EventFactory.
	 * Returns a list of conflicting events, the add is performed only if
	 * the size of conflicting is zero.
	 */
	boolean createEvent(EventBuilder event, EventEditPresent presenter);
	
	/**
	 * Apply the change in DTO to the original
	 *
	 * @param originalEvent an event.
	 * @param current an changed.
	 */
	boolean changeEvent(UUID originalEvent, EventBuilder current, EventEditPresent presenter);
	
	/**
	 * Remove the Event.
	 *
	 * @param e an event to remove.
	 */
	boolean removeEvent(UUID e, EventEditPresent presenter);
	
	/**
	 * Add people to the sign up list.
	 *
	 * @param event the Event.
	 * @param person a Person.
	 * @return
	 */
	boolean signUp(UUID event, UUID person, EventSignupPresent presenter);
	
	/**
	 * Remove the people from signed up list.
	 *
	 * @param event the Event.
	 * @param person a Person.
	 * @return
	 */
	boolean cancelSignUp(UUID event, UUID person, EventSignupPresent presenter);
	
	/**
	 * Check if the people is managing the event.
	 *
	 * @param event an Event
	 * @param person a person
	 * @return true iff the event is managed by person, otherwise false.
	 */
	boolean isManagedBy(UUID event, UUID person);
	
	/**
	 * Get the event with corresponding ID.
	 *
	 * @param id the id
	 * @return an event with the id
	 */
	Event get(UUID id);
	
	boolean isStarted(Event event);
	
	boolean isFinished(Event event);
	
	boolean isFinished(UUID event);
	
	Set<UUID> getSignedUp(UUID event);
	
	boolean isSignedUp(UUID person, UUID event);
	
	String getTitle(UUID event);
	
	void add(Event event);
	
	void remove(UUID event);
	
	List<UUID> getConflicting(Event event);
	
	List<UUID> getAll();
	
	EventBuilder getEventBuilder(UUID e);
	
	EventBuilder getEventBuilder(EventType e);
	
	void acceptTypeVisitor(UUID event, EventTypeVisitor visitor);
	
	void acceptTypeVisitor(EventType type, EventTypeVisitor visitor);
}
