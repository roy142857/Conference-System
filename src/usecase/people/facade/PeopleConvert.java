package usecase.people.facade;

import entity.people.*;
import usecase.dto.PersonDTO;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PeopleConvert implements PeopleVisitor, Serializable{
	
	private List<PersonDTO> personDTOS;
	
	private final PeopleManage pm;
	
	public PeopleConvert(PeopleManage pm){
		this.pm = pm;
	}
	
	public List<PersonDTO> convert(List<UUID> people){
		personDTOS = new ArrayList<>();
		for(UUID p : people){
			pm.get(p).accept(this);
		}
		return personDTOS;
	}
	
	public PersonDTO convert(UUID p){
		personDTOS = new ArrayList<>();
		pm.get(p).accept(this);
		return personDTOS.get(0);
	}
	
	@Override
	public void visitOrganizer(Organizer organizer){
		PersonDTO dto = new PersonDTO();
		dto.setID(organizer.getID());
		dto.setType("Organizer");
		dto.setName(organizer.getName());
		dto.setPoints(organizer.getPoints());
		personDTOS.add(dto);
	}
	
	@Override
	public void visitSpeaker(Speaker speaker){
		PersonDTO dto = new PersonDTO();
		dto.setID(speaker.getID());
		dto.setType("Speaker");
		dto.setName(speaker.getName());
		dto.setPoints(speaker.getPoints());
		personDTOS.add(dto);
	}
	
	@Override
	public void visitAttendee(Attendee attendee){
		PersonDTO dto = new PersonDTO();
		dto.setID(attendee.getID());
		dto.setType("Attendee");
		dto.setName(attendee.getName());
		dto.setPoints(attendee.getPoints());
		personDTOS.add(dto);
	}
	
	@Override
	public void visitVIPAttendee(VIPAttendee vipAttendee){
		PersonDTO dto = new PersonDTO();
		dto.setID(vipAttendee.getID());
		dto.setType("VIPAttendee");
		dto.setName(vipAttendee.getName());
		dto.setPoints(vipAttendee.getPoints());
		personDTOS.add(dto);
	}
}
