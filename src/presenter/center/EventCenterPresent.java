package presenter.center;

import usecase.dto.EventDTO;

import java.util.List;

public interface EventCenterPresent{
	
	void updateView(List<EventDTO> events);
}
