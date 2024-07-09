package controller.center;

import controller.session.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends Controller{
	
	// dashboard
	private final String ORGANIZER_DASHBOARD_FXML = "/javafx/center/dashboard/organizerDashboard.fxml";
	private final String SPEAKER_DASHBOARD_FXML   = "/javafx/center/dashboard/speakerDashboard.fxml";
	private final String ATTENDEE_DASHBOARD_FXML  = "/javafx/center/dashboard/attendeeDashboard.fxml";
	// event
	private final String EVENT_FXML               = "/javafx/center/eventCenter.fxml";
	// task
	private final String TASK_FXML                = "/javafx/center/taskCenter.fxml";
	// friends
	private final String FRIENDS_FXML             = "/javafx/center/friendsCenter.fxml";
	// messaging
	private final String MESSAGING_FXML           = "/javafx/center/messagingCenter.fxml";
	
	@FXML private RadioButton dashboard;
	@FXML private RadioButton event;
	@FXML private RadioButton task;
	@FXML private RadioButton friends;
	@FXML private RadioButton messaging;
	private       RadioButton empty;
	
	@FXML private BorderPane mainPane;
	
	@FXML private Button signOut;
	
	private ToggleGroup menuToggleGroup;
	private Parent      currentParent;
	
	public void onShowing(){
	}
	
	public void onShown(){
		login();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		setUpToggleGroup();
		signOut.setOnAction(actionEvent -> signOut());
		empty.setSelected(true);
	}
	
	private void initSelection(){
		if(dashboard.isSelected()){
			empty.setSelected(true);
		}
		dashboard.setSelected(true);
	}
	
	private void setUpToggleGroup(){
		empty = new RadioButton();
		menuToggleGroup = new ToggleGroup();
		menuToggleGroup.getToggles().addAll(dashboard, event, task, friends, messaging, empty);
		overrideStyle(dashboard);
		overrideStyle(event);
		overrideStyle(task);
		overrideStyle(friends);
		overrideStyle(messaging);
		overrideStyle(empty);
		menuToggleGroup.selectedToggleProperty()
		               .addListener((observableValue, newToggle, oldToggle) -> show());
	}
	
	private void show(){
		Toggle selected = menuToggleGroup.getSelectedToggle();
		if(selected == null || selected.equals(empty))
			return;
		Controller controller;
		if(selected.equals(event)){
			controller = load(EVENT_FXML);
		}else if(selected.equals(task)){
			controller = load(TASK_FXML);
		}else if(selected.equals(friends)){
			controller = load(FRIENDS_FXML);
		}else if(selected.equals(messaging)){
			controller = load(MESSAGING_FXML);
		}else{
			if(session.canOrganize()){
				controller = load(ORGANIZER_DASHBOARD_FXML);
			}else if(session.canSpeak()){
				controller = load(SPEAKER_DASHBOARD_FXML);
			}else{
				controller = load(ATTENDEE_DASHBOARD_FXML);
			}
		}
		controller.initSession(session);
		controller.initStageHandler(stageHandler);
		mainPane.setCenter(currentParent);
	}
	
	private <T> T load(String fxml){
		FXMLLoader loader = stageHandler.getLoader(fxml);
		currentParent = stageHandler.loadRoot(loader);
		return loader.getController();
	}
	
	private void overrideStyle(RadioButton button){
		button.getStyleClass().remove("radio-button");
		button.getStyleClass().add("toggle-button");
	}
	
	private void login(){
		stageHandler.attachLogin("Login");
		stageHandler.showAndWait();
		if(session.getLoggedIn() != null){
			initSelection();
		}else{
			Platform.exit();
		}
	}
	
	private void signOut(){
		session.setLoggedIn(null);
		login();
	}
}
