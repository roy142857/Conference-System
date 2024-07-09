package presenter.chat;

import java.util.UUID;

public interface GroupMessagePresent{
	
	void respondStarting(int total);
	
	void respondPersonStatus(UUID person, boolean success);
	
	void respondEnding(int success, int total);
}
