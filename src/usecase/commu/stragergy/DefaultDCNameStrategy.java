package usecase.commu.stragergy;

import usecase.people.PeopleManage;

import java.util.UUID;

public class DefaultDCNameStrategy implements DirectChatNameStrategy{
	
	private final PeopleManage pm;
	
	public DefaultDCNameStrategy(PeopleManage pm){
		this.pm = pm;
	}
	
	@Override
	public String generateName(UUID p1, UUID p2){
		return String.format(
				"Direct Chat for %s, %s",
				pm.getName(p1), pm.getName(p2)
		);
	}
}
