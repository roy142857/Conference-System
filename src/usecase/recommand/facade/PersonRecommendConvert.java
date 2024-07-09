package usecase.recommand.facade;

import entity.people.*;
import usecase.dto.PersonRecommendDTO;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.*;

public class PersonRecommendConvert implements PeopleVisitor, Serializable{
	
	private final PeopleManage pm;
	
	private List<PersonRecommendDTO> personRecommendDTOS;
	private double                   similarity;
	
	public PersonRecommendConvert(PeopleManage pm){
		this.pm = pm;
	}
	
	public List<PersonRecommendDTO> convert(LinkedHashMap<UUID, Double> recommend){
		personRecommendDTOS = new ArrayList<>();
		for(Map.Entry<UUID, Double> entry : recommend.entrySet()){
			this.similarity = entry.getValue();
			pm.get(entry.getKey()).accept(this);
		}
		return personRecommendDTOS;
	}
	
	@Override
	public void visitOrganizer(Organizer organizer){
		PersonRecommendDTO organizerRecommendDTO = new PersonRecommendDTO();
		organizerRecommendDTO.setType("Organizer");
		organizerRecommendDTO.setID(organizer.getID());
		organizerRecommendDTO.setName(organizer.getName());
		organizerRecommendDTO.setSimilarity(similarity);
		personRecommendDTOS.add(organizerRecommendDTO);
	}
	
	@Override
	public void visitSpeaker(Speaker speaker){
		PersonRecommendDTO organizerRecommendDTO = new PersonRecommendDTO();
		organizerRecommendDTO.setType("Speaker");
		organizerRecommendDTO.setID(speaker.getID());
		organizerRecommendDTO.setName(speaker.getName());
		organizerRecommendDTO.setSimilarity(similarity);
		personRecommendDTOS.add(organizerRecommendDTO);
	}
	
	@Override
	public void visitAttendee(Attendee attendee){
		PersonRecommendDTO organizerRecommendDTO = new PersonRecommendDTO();
		organizerRecommendDTO.setType("Attendee");
		organizerRecommendDTO.setID(attendee.getID());
		organizerRecommendDTO.setName(attendee.getName());
		organizerRecommendDTO.setSimilarity(similarity);
		personRecommendDTOS.add(organizerRecommendDTO);
	}
	
	@Override
	public void visitVIPAttendee(VIPAttendee vipAttendee){
		PersonRecommendDTO organizerRecommendDTO = new PersonRecommendDTO();
		organizerRecommendDTO.setType("VIPAttendee");
		organizerRecommendDTO.setID(vipAttendee.getID());
		organizerRecommendDTO.setName(vipAttendee.getName());
		organizerRecommendDTO.setSimilarity(similarity);
		personRecommendDTOS.add(organizerRecommendDTO);
	}
}
