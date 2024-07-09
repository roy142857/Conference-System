package usecase.task;

import entity.event.Event;
import entity.task.Task;
import presenter.center.TaskCenterPresent;
import presenter.task.TaskEditPresent;
import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.schedule.Schedule;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

public interface TaskManage extends Serializable{
	
	void initialize(EventManage em, PeopleManage pm);
	
	void updateView(UUID people, TaskCenterPresent presenter);
	
	/**
	 * Create and add a Task based on type. This method should use the TaskFactory.
	 *
	 * @param type the type
	 * @param person the person.
	 * @param event the event.
	 * @see entity.task.factory.TaskFactory
	 */
	boolean createTask(String type, UUID person, Event event, TaskEditPresent presenter);
	
	boolean createTasks(String type, Collection<UUID> people, Event e, TaskEditPresent presenter);
	
	/**
	 * Remove the task for the event of the person.
	 * (using {@code Task.isTaskFor(event)})
	 * If no such task, do nothing.
	 *
	 * @param person the person.
	 * @param event the id of the event.
	 */
	void removeTask(UUID person, Event event, TaskEditPresent presenter);
	
	void removeTasks(Collection<UUID> people, Event e, TaskEditPresent presenter);
	
	/**
	 * Get the task for that event for person.
	 */
	Task getTask(UUID person, UUID event);
	
	/**
	 * Return the schedule for that person.
	 */
	Schedule<Task> getScheduleFor(UUID person);
	
	
}
