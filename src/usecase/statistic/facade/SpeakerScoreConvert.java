package usecase.statistic.facade;

import usecase.dto.SpeakerScoreDTO;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpeakerScoreConvert implements Serializable{
	
	private final ScoreManage  sm;
	private final PeopleManage pm;
	
	public SpeakerScoreConvert(ScoreManage sm, PeopleManage pm){
		this.sm = sm;
		this.pm = pm;
	}
	
	public List<SpeakerScoreDTO> convert(List<UUID> speakers){
		List<SpeakerScoreDTO> speakerScores = new ArrayList<>();
		for(UUID id : speakers){
			SpeakerScoreDTO speakerScoreDTO = new SpeakerScoreDTO();
			speakerScoreDTO.setScore(sm.getSpeakerScore(id));
			speakerScoreDTO.setName(pm.getName(id));
			speakerScoreDTO.setID(id);
			speakerScores.add(speakerScoreDTO);
		}
		return speakerScores;
	}
}
