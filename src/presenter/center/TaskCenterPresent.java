package presenter.center;

import usecase.dto.TaskDTO;

import java.util.List;

public interface TaskCenterPresent{
	
	void updateView(List<TaskDTO> tasks);
}
