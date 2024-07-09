package presenter.center.dashboard;

import usecase.dto.SpeakerScoreDTO;

import java.util.List;

public interface OrganizerDashboardPresent extends DashboardPresent{
	
	void updateTopSpeakerView(List<SpeakerScoreDTO> speakerScoreDTOS);
}
