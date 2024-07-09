package usecase.recommand;

import presenter.center.dashboard.AttendeeDashboardPresent;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.recommand.facade.CollaborativeFiltering;
import usecase.recommand.facade.EventRecommendConvert;
import usecase.recommand.facade.PersonRecommendConvert;
import usecase.recommand.facade.SimilarityCalculate;
import usecase.score.ScoreManage;

import java.util.LinkedHashMap;
import java.util.UUID;

public class RecommendManager implements RecommendManage{
	
	private PersonRecommendConvert personRecommendConvert;
	private EventRecommendConvert  eventRecommendConvert;
	private SimilarityCalculate    similarityCalculate;
	private CollaborativeFiltering collaborativeFiltering;
	
	@Override
	public void initialize(EventManage em, PeopleManage pm, ScoreManage sm, FriendsManage fm){
		personRecommendConvert = new PersonRecommendConvert(pm);
		eventRecommendConvert = new EventRecommendConvert(em);
		similarityCalculate = new SimilarityCalculate(em, pm, sm, fm);
		collaborativeFiltering = new CollaborativeFiltering(em, pm, sm);
	}
	
	@Override
	public void updateRecommendFriends(UUID p, AttendeeDashboardPresent presenter){
		LinkedHashMap<UUID, Double> recommend = getRecommendedFriends(p);
		// update the view
		presenter.updateRecommendFriendView(personRecommendConvert.convert(recommend));
	}
	
	private LinkedHashMap<UUID, Double> getRecommendedFriends(UUID p){
		return similarityCalculate.getPeople_prompt(p);
	}
	
	@Override
	public void updateRecommendEvents(UUID p, AttendeeDashboardPresent presenter){
		LinkedHashMap<UUID, Double> recommend = getRecommendedEvents(p);
		// update the view
		presenter.updateRecommendEventView(eventRecommendConvert.convert(recommend));
	}
	
	private LinkedHashMap<UUID, Double> getRecommendedEvents(UUID p){
		return collaborativeFiltering.getEvent_prompt(p);
	}
}
