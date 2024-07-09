package usecase.commu.stragergy;

import java.io.Serializable;
import java.util.UUID;

public interface DirectChatNameStrategy extends Serializable{
	
	String generateName(UUID p1, UUID p2);
}
