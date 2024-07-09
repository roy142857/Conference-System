package usecase.task.facade;

import presenter.center.TaskCenterPresent;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.UUID;

public class TaskView implements Serializable{
	
	private final TaskManage  tm;
	private final TaskConvert taskConvert;
	
	public TaskView(TaskManage tm, TaskConvert taskConvert){
		this.tm = tm;
		this.taskConvert = taskConvert;
	}
	
	public void updateView(UUID people, TaskCenterPresent presenter){
		presenter.updateView(taskConvert.convert(people, tm.getScheduleFor(people).getAll()));
	}
}
