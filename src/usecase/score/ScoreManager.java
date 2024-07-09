package usecase.score;

import presenter.event.ScorePresent;
import usecase.event.EventManage;
import usecase.score.facade.SpeakerScoreCalculate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreManager implements ScoreManage{
	
	private final Map<UUID, Map<UUID, Double>> score_data;
	
	private EventManage           em;
	private SpeakerScoreCalculate speakerScoreCalculate;
	
	public ScoreManager(){
		score_data = new HashMap<>();
	}
	
	@Override
	public void initialize(EventManage em){
		this.em = em;
		this.speakerScoreCalculate = new SpeakerScoreCalculate(this, em);
	}
	
	@Override
	public void createEventScoring(UUID event){
		score_data.put(event, new HashMap<>());
	}
	
	@Override
	public void removeEventScoring(UUID event){
		score_data.remove(event);
	}
	
	@Override
	public void updateView(UUID event, UUID person, ScorePresent presenter){
		presenter.updateView(getScoreOf(score_data.get(event), person));
	}
	
	@Override
	public boolean scoreEvent(UUID event, UUID person, double score, ScorePresent presenter){
		Map<UUID, Double> scores = score_data.get(event);
		if(scores == null){
			throw new IllegalArgumentException("Event Scoring not created!");
		}else if(score > 5 || score < 0){
			throw new IllegalArgumentException("Score out of bound!");
		}else if(! em.isFinished(event)){
			presenter.respondEventNotFinished();
		}else if(! em.isSignedUp(person, event)){
			presenter.respondIsNotSignedUp();
		}else{
			scores.put(person, score);
			return true;
		}
		return false;
	}
	
	@Override
	public double getEventScore(UUID event){
		Map<UUID, Double> scores = score_data.get(event);
		int size = scores.keySet().size();
		double values = 0d;
		for(Double value : scores.values()){
			values += value;
		}
		if(size == 0)
			return 0;
		return (values / size);
	}
	
	@Override
	public double getSpeakerScore(UUID speaker){
		return speakerScoreCalculate.getSpeakerScore(speaker);
	}
	
	
	@Override
	public Map<UUID, Double> getPreference(UUID person){
		// convert "{event: {person: score}}" to "{event: score}"
		return score_data.entrySet().stream()
		                 .collect(Collectors.toMap(Map.Entry::getKey, e -> getScoreOf(e.getValue(), person)));
	}
	
	private double getScoreOf(Map<UUID, Double> scoreMap, UUID person){
		return scoreMap.getOrDefault(person, 0d);
	}
}
