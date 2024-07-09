package usecase.event.facade;

import entity.event.*;
import usecase.dto.EventDTO;
import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Visitor Design Pattern.
 * Generate a Description of an Event.
 */
public class EventConvert implements EventVisitor, Serializable{
	
	private List<EventDTO> eventDTOS;
	
	private final EventManage  em;
	private final PeopleManage pm;
	private final ScoreManage  sm;
	
	public EventConvert(EventManage em, PeopleManage pm, ScoreManage sm){
		this.em = em;
		this.pm = pm;
		this.sm = sm;
	}
	
	public List<EventDTO> convert(List<UUID> events){
		eventDTOS = new ArrayList<>();
		for(UUID e : events){
			em.get(e).accept(this);
		}
		return eventDTOS;
	}
	
	public EventDTO convert(UUID e){
		eventDTOS = new ArrayList<>();
		em.get(e).accept(this);
		return eventDTOS.get(0);
	}
	
	@Override
	public void visitTalk(Talk talk){
		EventDTO eventDTO = new EventDTO();
		visitEvent(talk, eventDTO);
		eventDTO.setType((talk.getRequirement() != 0 ? "Vip-" : "") + "Talk");
		eventDTO.setSpeaker(pm.getName(talk.getSpeaker()));
		eventDTOS.add(eventDTO);
	}
	
	@Override
	public void visitNonSpeakerEvent(NonSpeakerEvent nse){
		EventDTO eventDTO = new EventDTO();
		visitEvent(nse, eventDTO);
		eventDTO.setType((nse.getRequirement() != 0 ? "Vip-" : "") + "NonSpeakerEvent");
		eventDTOS.add(eventDTO);
	}
	
	@Override
	public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
		EventDTO eventDTO = new EventDTO();
		visitEvent(mse, eventDTO);
		eventDTO.setType((mse.getRequirement() != 0 ? "Vip-" : "") + "MultiSpeakerEvent");
		eventDTO.setSpeaker(mse.getSpeakers().stream().map(pm::getName).collect(Collectors.joining(", ")));
		eventDTOS.add(eventDTO);
	}
	
	private void visitEvent(Event event, EventDTO eventDTO){
		eventDTO.setID(event.getID());
		eventDTO.setID(event.getID());
		eventDTO.setTitle(event.getTitle());
		eventDTO.setCapacity(event.getCapacity());
		eventDTO.setStartTime(event.getStartTime());
		eventDTO.setEndTime(event.getEndTime());
		eventDTO.setLocation(event.getLocation());
		eventDTO.setOrganizer(pm.getName(event.getOrganizer()));
		eventDTO.setSignedUp(event.getSignedUp().stream().map(pm::getName).collect(Collectors.toList()));
		eventDTO.setReward(event.getReward());
		eventDTO.setRequirement(event.getRequirement());
		eventDTO.setScore(sm.getEventScore(event.getID()));
	}
}
