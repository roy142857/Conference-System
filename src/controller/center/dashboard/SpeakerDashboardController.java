package controller.center.dashboard;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import presenter.center.dashboard.SpeakerDashboardPresent;
import usecase.dto.PersonDTO;

public class SpeakerDashboardController extends Controller{
	
	@FXML private Label welcome;
	
	private SpeakerDashboardPresent presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new SpeakerDashboardPresenter();
		fetchView();
	}
	
	private void fetchView(){
		session.getPM().updateDashboardView(session.getLoggedIn(), presenter);
	}
	
	private class SpeakerDashboardPresenter implements SpeakerDashboardPresent{
		
		@Override
		public void updateWelcomeView(PersonDTO personDTO){
			welcome.setText(String.format(
					"Welcome %s! You are signed in as %s!",
					personDTO.getName(), personDTO.getType()
			));
		}
	}
}
