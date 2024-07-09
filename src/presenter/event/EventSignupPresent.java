package presenter.event;

import presenter.people.VIPPromotePresent;
import presenter.task.TaskEditPresent;

public interface EventSignupPresent extends TaskEditPresent, VIPPromotePresent{
	
	void respondEventFull();
	
	void respondAlreadySignedUp();
	
	void respondNotYetSignedUp();
	
	void respondRequirementNotMeet();
	
	void respondEventStarted();
}
