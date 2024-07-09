package usecase.people.facade;

import entity.people.Person;
import entity.people.factory.PersonFactory;
import presenter.people.VIPPromotePresent;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.UUID;

public class VIPPromote implements Serializable{
	
	private final int VIP_THRESHOLD = 100;
	
	private final PeopleManage pm;
	
	public VIPPromote(PeopleManage pm){
		this.pm = pm;
	}
	
	public void onPointChanged(UUID person, int prevPoint, VIPPromotePresent present){
		Person p = pm.get(person);
		int currPoint = p.getPoints();
		if(prevPoint < VIP_THRESHOLD && currPoint >= VIP_THRESHOLD){
			// If the user was not VIP, and now becomes a VIP (i.e. do promotion)
			Person vip = new PersonFactory().convertToVIPAttendee(p);
			pm.updatePerson(vip);
			present.notifyPromotionToVIP();
		}else if(prevPoint >= VIP_THRESHOLD && currPoint < VIP_THRESHOLD){
			// If the user was VIP, and now isn't (i,e. do demotion)
			Person regular = new PersonFactory().convertToRegularAttendee(p);
			pm.updatePerson(regular);
			present.notifyDemotionToRegular();
		}
	}
}
