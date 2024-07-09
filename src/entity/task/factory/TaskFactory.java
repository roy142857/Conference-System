package entity.task.factory;

import entity.event.Event;
import entity.task.Appointment;
import entity.task.SpeakerDuty;
import entity.task.Task;
import exception.general.NotSupportedException;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskFactory{
	
	public Task create(String type, Event origin){
		UUID id = origin.getID();
		String location = origin.getLocation();
		LocalDateTime start = origin.getStartTime();
		LocalDateTime end = origin.getEndTime();
		switch(type.toLowerCase()){
			case "appointment":
				return new Appointment(id, location, start, end);
			case "speakerduty":
				return new SpeakerDuty(id, location, start, end);
			default:
				throw new NotSupportedException("Task of type " + type + " is not supported!");
		}
	}
}
