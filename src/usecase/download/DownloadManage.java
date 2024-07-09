package usecase.download;

import usecase.event.EventManage;
import usecase.people.PeopleManage;

import java.io.Serializable;

public interface DownloadManage extends Serializable{
	
	void initialize(EventManage em, PeopleManage pm);
	
	String generateAllEventScheduleHTML();
}
