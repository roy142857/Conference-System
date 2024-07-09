package usecase.task.facade;

import entity.task.Appointment;
import entity.task.SpeakerDuty;
import entity.task.TaskVisitor;
import usecase.dto.TaskDTO;
import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.task.TaskManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Visitor Design Pattern.
 * Generate a Description of Task.
 */
public class TaskConvert implements TaskVisitor, Serializable{
	
	private List<TaskDTO> taskDTOS;
	private String        ownerName;
	
	private final TaskManage   tm;
	private final PeopleManage pm;
	private final EventManage  em;
	
	public TaskConvert(TaskManage tm, PeopleManage pm, EventManage em){
		this.tm = tm;
		this.pm = pm;
		this.em = em;
	}
	
	public List<TaskDTO> convert(UUID person, List<UUID> tasks){
		taskDTOS = new ArrayList<>();
		ownerName = pm.getName(person);
		for(UUID t : tasks){
			tm.getTask(person, t).accept(this);
		}
		return taskDTOS;
	}
	
	@Override
	public void visitAppointment(Appointment appointment){
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setID(appointment.getID());
		taskDTO.setTaskFor(appointment.getTaskFor());
		taskDTO.setType("Appointment");
		taskDTO.setOwner(ownerName);
		taskDTO.setTitle(em.get(appointment.getTaskFor()).getTitle());
		taskDTOS.add(taskDTO);
	}
	
	@Override
	public void visitSpeakerDuty(SpeakerDuty speakerDuty){
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setID(speakerDuty.getID());
		taskDTO.setTaskFor(speakerDuty.getTaskFor());
		taskDTO.setType("Speaker Duty");
		taskDTO.setOwner(ownerName);
		taskDTO.setTitle(em.get(speakerDuty.getTaskFor()).getTitle());
		taskDTOS.add(taskDTO);
	}
}