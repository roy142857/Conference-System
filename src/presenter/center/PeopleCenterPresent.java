package presenter.center;

import usecase.dto.PersonDTO;

import java.util.List;

public interface PeopleCenterPresent{
	
	void updateCoworkerView(List<PersonDTO> coworker);
	
	void updateFriendsView(List<PersonDTO> friends);
}
