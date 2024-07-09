package presenter.chat;

import usecase.dto.MessageDTO;

import java.util.List;
import java.util.UUID;

public interface ChatPresent{
	
	void updateView(List<MessageDTO> message, UUID owner);
	
	void respondNoAccess(String needingAccess);
	
	void respondMessageEmpty();
}
