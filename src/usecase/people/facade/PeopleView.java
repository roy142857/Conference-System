package usecase.people.facade;

import presenter.people.PeopleViewPresent;
import presenter.center.dashboard.DashboardPresent;
import presenter.center.PeopleCenterPresent;
import usecase.friends.manager.FriendsManage;

import java.io.Serializable;
import java.util.UUID;

public class PeopleView implements Serializable{
	
	private final FriendsManage fm;
	
	private final PeopleConvert peopleConvert;
	
	public PeopleView(PeopleConvert peopleConvert, FriendsManage fm){
		this.peopleConvert = peopleConvert;
		this.fm = fm;
	}
	
	public void updateCenterView(UUID person, PeopleCenterPresent presenter){
		presenter.updateCoworkerView(peopleConvert.convert(fm.getCoWorker(person)));
		presenter.updateFriendsView(peopleConvert.convert(fm.getFriends(person)));
	}
	
	public void updateDashboardView(UUID person, DashboardPresent presenter){
		presenter.updateWelcomeView(peopleConvert.convert(person));
	}
	
	public void updateView(UUID person, PeopleViewPresent presenter){
		presenter.updateView(peopleConvert.convert(person));
	}
}
