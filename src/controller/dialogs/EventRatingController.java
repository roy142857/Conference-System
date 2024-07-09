package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.controlsfx.control.Rating;
import presenter.event.ScorePresent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class EventRatingController extends Controller{
	
	@FXML private Rating rating;
	@FXML private Label  respond;
	
	@FXML private Button submit;
	
	private UUID         event;
	private ScorePresent presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new ScorePresenter();
	}
	
	public void initEvent(UUID event){
		this.event = event;
		fetchView();
	}
	
	private void fetchView(){
		session.getSM().updateView(event, session.getLoggedIn(), presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		setUpRating();
		submit.setOnAction(actionEvent -> submitRating());
	}
	
	private void setUpRating(){
		rating.setMax(5);
		rating.setPartialRating(true);
	}
	
	private void submitRating(){
		double ratingScore = rating.getRating();
		if(session.getSM().scoreEvent(event, session.getLoggedIn(), ratingScore, presenter)){
			stageHandler.close();
		}else{
			fetchView();
		}
	}
	
	private class ScorePresenter implements ScorePresent{
		
		@Override
		public void respondEventNotFinished(){
			respond.setText("Wait for the event to finishes to submit evaluation!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void updateView(double score){
			rating.setRating(score);
		}
		
		@Override
		public void respondIsNotSignedUp(){
			respond.setText("You wasn't signed up so you can't rate!");
		}
	}
}
