package usecase.score;

import presenter.event.ScorePresent;
import usecase.event.EventManage;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public interface ScoreManage extends Serializable{
	
	void initialize(EventManage em);
	
	void createEventScoring(UUID event);
	
	void removeEventScoring(UUID event);
	
	void updateView(UUID event, UUID person, ScorePresent presenter);
	
	/**
	 * Give the score of the event by person and update to db.
	 *  @param event an event
	 * @param person a person
	 * @param score a score from 0 to 5
	 */
	boolean scoreEvent(UUID event, UUID person, double score, ScorePresent presenter);
	
	double getEventScore(UUID event);
	
	double getSpeakerScore(UUID speaker);
	
	Map<UUID, Double> getPreference(UUID person);
}
