package usecase.task.facade;

import entity.event.Event;
import entity.task.Task;
import entity.task.factory.TaskFactory;
import presenter.task.TaskEditPresent;
import usecase.schedule.Schedule;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.*;

public class TaskCreate implements Serializable{
	
	private final TaskManage  tm;
	private final TaskConvert taskConvert;
	
	public TaskCreate(TaskManage tm, TaskConvert taskConvert){
		this.tm = tm;
		this.taskConvert = taskConvert;
	}
	
	public boolean createTask(String type, UUID person, Event event, TaskEditPresent presenter){
		Task t = new TaskFactory().create(type, event);
		if(validateTask(t, person, event, presenter)){
			tm.getScheduleFor(person).add(t);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean createTasks(String type, Collection<UUID> people, Event event, TaskEditPresent presenter){
		TaskFactory factory = new TaskFactory();
		// we want to check if all change can be made, if any of them can't, we abort
		Map<UUID, Task> taskMap = new LinkedHashMap<>();
		for(UUID person : people){
			Task t = factory.create(type, event);
			if(validateTask(t, person, event, presenter)){
				taskMap.put(person, t);
			}else{
				return false;
			}
		}
		taskMap.forEach((person, t) -> tm.getScheduleFor(person).add(t));
		return true;
	}
	
	private boolean validateTask(Task task, UUID person, Event event, TaskEditPresent presenter){
		if(hasTaskFor(person, event.getID())){
			presenter.respondAlreadyOwnTask();
			return false;
		}else{
			Schedule<Task> schedule = tm.getScheduleFor(person);
			List<UUID> conflicting = schedule.getConflicting(task);
			if(conflicting.size() != 0){
				presenter.respondConflictingTasks(taskConvert.convert(person, conflicting));
				return false;
			}
			return true;
		}
	}
	
	public void removeTask(UUID person, Event event, TaskEditPresent presenter){
		tm.getScheduleFor(person).removeIf(task -> task.getTaskFor().equals(event.getID()));
	}
	
	public void removeTasks(Collection<UUID> people, Event event, TaskEditPresent presenter){
		people.forEach(person -> tm.getScheduleFor(person).removeIf(task -> task.getTaskFor().equals(event.getID())));
	}
	
	public boolean validateCreateTask(String type, UUID person, Event event, TaskEditPresent presenter){
		Task t = new TaskFactory().create(type, event);
		Schedule<Task> schedule = tm.getScheduleFor(person);
		if(hasTaskFor(person, event.getID())){
			presenter.respondAlreadyOwnTask();
			return false;
		}
		List<UUID> conflicting = schedule.getConflicting(t);
		if(conflicting.size() != 0){
			presenter.respondConflictingTasks(taskConvert.convert(person, conflicting));
			return false;
		}else{
			schedule.add(t);
			return true;
		}
	}
	
	private boolean hasTaskFor(UUID person, UUID event){
		return tm.getScheduleFor(person).anyMatch(task -> task.getTaskFor().equals(event));
	}
}
