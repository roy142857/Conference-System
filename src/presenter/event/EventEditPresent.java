package presenter.event;

import presenter.task.TaskEditPresent;
import usecase.dto.EventDTO;

import java.util.List;

public interface EventEditPresent extends TaskEditPresent{
	
	void respondConflictingEvents(List<EventDTO> conflictingEvents);
	
	void respondCapacityTooSmall();
	
	void respondChangeNotSupported(String type);
	
	void respondInvalidTime();
	
	void respondAlreadyStarted();
	
	void respondPleaseSet(String type);
}
