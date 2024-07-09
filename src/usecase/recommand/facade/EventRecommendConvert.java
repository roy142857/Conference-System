package usecase.recommand.facade;

import entity.event.EventVisitor;
import entity.event.MultiSpeakerEvent;
import entity.event.NonSpeakerEvent;
import entity.event.Talk;
import usecase.dto.EventRecommendDTO;
import usecase.event.EventManage;

import java.io.Serializable;
import java.util.*;

public class EventRecommendConvert implements EventVisitor, Serializable{
	
	private final EventManage em;
	
	private List<EventRecommendDTO> eventRecommendDTOS;
	private double                  estimation;
	
	public EventRecommendConvert(EventManage em){
		this.em = em;
	}
	
	public List<EventRecommendDTO> convert(LinkedHashMap<UUID, Double> recommend){
		eventRecommendDTOS = new ArrayList<>();
		for(Map.Entry<UUID, Double> entry : recommend.entrySet()){
			this.estimation = entry.getValue();
			em.get(entry.getKey()).accept(this);
		}
		return eventRecommendDTOS;
	}
	
	@Override
	public void visitTalk(Talk talk){
		EventRecommendDTO talkRecommendDTO = new EventRecommendDTO();
		talkRecommendDTO.setID(talk.getID());
		talkRecommendDTO.setType((talk.getRequirement() != 0 ? "Vip-" : "") + "Talk");
		talkRecommendDTO.setTitle(talk.getTitle());
		talkRecommendDTO.setEstimation(estimation);
		eventRecommendDTOS.add(talkRecommendDTO);
	}
	
	@Override
	public void visitNonSpeakerEvent(NonSpeakerEvent nse){
		EventRecommendDTO nseRecommendDTO = new EventRecommendDTO();
		nseRecommendDTO.setID(nse.getID());
		nseRecommendDTO.setType((nse.getRequirement() != 0 ? "Vip-" : "") + "Non Speaker");
		nseRecommendDTO.setTitle(nse.getTitle());
		nseRecommendDTO.setEstimation(estimation);
		eventRecommendDTOS.add(nseRecommendDTO);
	}
	
	@Override
	public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
		EventRecommendDTO talkRecommendDTO = new EventRecommendDTO();
		talkRecommendDTO.setID(mse.getID());
		talkRecommendDTO.setType((mse.getRequirement() != 0 ? "Vip-" : "") + "Multi Speaker");
		talkRecommendDTO.setTitle(mse.getTitle());
		talkRecommendDTO.setEstimation(estimation);
		eventRecommendDTOS.add(talkRecommendDTO);
	}
}
