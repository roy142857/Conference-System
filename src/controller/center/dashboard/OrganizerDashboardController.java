package controller.center.dashboard;

import controller.session.Controller;
import controller.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import presenter.center.dashboard.OrganizerDashboardPresent;
import usecase.dto.PersonDTO;
import usecase.dto.SpeakerScoreDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizerDashboardController extends Controller{
	
	@FXML private Label                    welcome;
	@FXML private BarChart<String, Number> topSpeakerBarChat;
	
	private ObservableList<XYChart.Series<String, Number>>       topSpeakers;
	private presenter.center.dashboard.OrganizerDashboardPresent presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new OrganizerDashboardPresenter();
		fetchView();
	}
	
	private void fetchView(){
		session.getPM().updateDashboardView(session.getLoggedIn(), presenter);
		session.getSTA().updateTopSpeakerView(presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		topSpeakers = FXCollections.observableArrayList();
		topSpeakerBarChat.setData(topSpeakers);
		topSpeakerBarChat.setLegendVisible(false);
	}
	
	private class OrganizerDashboardPresenter implements OrganizerDashboardPresent{
		
		@Override
		public void updateWelcomeView(PersonDTO personDTO){
			welcome.setText(String.format(
					"Welcome %s! You are signed in as %s!",
					personDTO.getName(), personDTO.getType()
			));
		}
		
		@Override
		public void updateTopSpeakerView(List<SpeakerScoreDTO> speakerScoreDTOS){
			XYChart.Series<String, Number> speakerReport = new XYChart.Series<>();
			for(SpeakerScoreDTO scoreDTO : speakerScoreDTOS){
				speakerReport.getData().add(new XYChart.Data<>(scoreDTO.getName(), scoreDTO.getScore()));
			}
			topSpeakers.clear();
			topSpeakers.add(speakerReport);
		}
	}
}
