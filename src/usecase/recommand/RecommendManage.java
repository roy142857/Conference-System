package usecase.recommand;

import presenter.center.dashboard.AttendeeDashboardPresent;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.UUID;

public interface RecommendManage extends Serializable {
	
	void initialize(EventManage em, PeopleManage pm, ScoreManage sm, FriendsManage fm);
	
	/**
	 * Return a list of person that has the most similar preference as person.
	 *
	 * Postcondition, the friends recommend to p:
	 * - p itself must be excluded
	 * - all person returned must not already be friend with p.
	 *
	 * @param p the person to recommend friend with.
	 */
	void updateRecommendFriends(UUID p, AttendeeDashboardPresent presenter);

	/**
	 * Return a list of events that has the most similar preference as person.
	 *
	 * Postcondition, the event recommend to p:
	 * - all events returned must not be the event that p signed up.
	 *
	 * @param p the person to recommend event with.
	 */
	void updateRecommendEvents(UUID p, AttendeeDashboardPresent presenter);
}
