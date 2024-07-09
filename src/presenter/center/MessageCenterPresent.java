package presenter.center;

import usecase.dto.ChatDTO;

import java.util.List;

public interface MessageCenterPresent{
	
	void updateView(List<ChatDTO> chats);
}
