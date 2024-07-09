package presenter.task;

import usecase.dto.TaskDTO;

import java.util.List;

public interface TaskEditPresent{
	
	void respondConflictingTasks(List<TaskDTO> conflictingTasks);
	
	void respondAlreadyOwnTask();
	
}
