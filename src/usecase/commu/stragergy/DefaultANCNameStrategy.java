package usecase.commu.stragergy;

import usecase.event.EventManage;

import java.util.UUID;

public class DefaultANCNameStrategy implements AnnouncementNameStrategy{
	
	private final EventManage em;
	
	public DefaultANCNameStrategy(EventManage em){
		this.em = em;
	}
	
	@Override
	public String generateName(UUID event){
		return String.format(
				"Announcement Group for %s", em.get(event).getTitle()
		);
	}
}
