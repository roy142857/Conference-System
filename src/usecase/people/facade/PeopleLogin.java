package usecase.people.facade;

import presenter.login.LoginPresent;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.UUID;

public class PeopleLogin implements Serializable{
	
	private final PeopleManage pm;
	
	public PeopleLogin(PeopleManage pm){
		this.pm = pm;
	}
	
	public UUID login(String username, char[] password, LoginPresent presenter){
		if(! pm.hasSameUsername(username)){
			presenter.respondUsernameNotExist();
		}else if(! pm.matchPassword(username, password)){
			presenter.respondIncorrectPassword();
		}else{
			return pm.getID(username);
		}
		return null;
	}
}
