package usecase.commu.stragergy;

import java.io.Serializable;
import java.util.UUID;

public interface AnnouncementNameStrategy extends Serializable{
	
	String generateName(UUID event);
}
