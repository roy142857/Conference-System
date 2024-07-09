package presenter.center.dashboard;

import usecase.dto.EventRecommendDTO;
import usecase.dto.PersonRecommendDTO;

import java.util.List;

public interface AttendeeDashboardPresent extends DashboardPresent{
	
	void updateRecommendFriendView(List<PersonRecommendDTO> recommended);
	
	void updateRecommendEventView(List<EventRecommendDTO> recommended);
}
