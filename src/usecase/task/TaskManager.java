package usecase.task;

import entity.event.Event;
import entity.task.Task;
import presenter.center.TaskCenterPresent;
import presenter.task.TaskEditPresent;
import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.schedule.Schedule;
import usecase.schedule.Scheduler;
import usecase.task.facade.TaskConvert;
import usecase.task.facade.TaskCreate;
import usecase.task.facade.TaskView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskManager implements TaskManage{
	
	private final Map<UUID, Schedule<Task>> peopleScheduleMap;
	
	private TaskCreate taskCreate;
	private TaskView   taskView;
	
	//constructor
	public TaskManager(){
		this.peopleScheduleMap = new HashMap<>();
	}
	
	@Override
	public void initialize(EventManage em, PeopleManage pm){
		TaskConvert taskConvert = new TaskConvert(this, pm, em);
		taskCreate = new TaskCreate(this, taskConvert);
		taskView = new TaskView(this, taskConvert);
	}
	
	@Override
	public void updateView(UUID people, TaskCenterPresent presenter){
		taskView.updateView(people, presenter);
	}
	
	@Override
	public boolean createTasks(String type, Collection<UUID> people, Event e, TaskEditPresent presenter){
		return taskCreate.createTasks(type, people, e, presenter);
	}
	
	@Override
	public boolean createTask(String type, UUID person, Event event, TaskEditPresent presenter){
		return taskCreate.createTask(type, person, event, presenter);
	}
	
	@Override
	public void removeTask(UUID person, Event event, TaskEditPresent presenter){
		taskCreate.removeTask(person, event, presenter);
	}
	
	@Override
	public void removeTasks(Collection<UUID> people, Event e, TaskEditPresent presenter){
		taskCreate.removeTasks(people, e, presenter);
	}
	
	@Override
	public Task getTask(UUID person, UUID task){
		return getScheduleFor(person).get(task);
	}
	
	@Override
	public Schedule<Task> getScheduleFor(UUID person){
		if(! peopleScheduleMap.containsKey(person))
			peopleScheduleMap.put(person, new Scheduler<>());
		return peopleScheduleMap.get(person);
	}
}
