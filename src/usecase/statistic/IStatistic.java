package usecase.statistic;

import presenter.center.dashboard.OrganizerDashboardPresent;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;

public interface IStatistic extends Serializable{
	
	void initialize(ScoreManage sm, PeopleManage pm);
	
	void updateTopSpeakerView(OrganizerDashboardPresent presenter);
}
