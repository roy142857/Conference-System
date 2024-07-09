package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import presenter.login.LoginPresent;
import usecase.people.PersonType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class LoginController extends Controller{
	
	@FXML private TextField     usernameField;
	@FXML private PasswordField passwordField;
	
	@FXML private Label  respond;
	@FXML private Button login;
	@FXML private Button createAttendee;
	
	private LoginPresent presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new LoginPresenter();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// hook the listener
		login.setOnAction(actionEvent -> login());
		createAttendee.setOnAction(actionEvent -> createAttendee());
	}
	
	private void login(){
		String username = usernameField.getText();
		char[] password = passwordField.getText().toCharArray();
		UUID loggedIn = session.getPM().login(username, password, presenter);
		if(loggedIn != null){
			session.setLoggedIn(loggedIn);
			stageHandler.close();
		}
	}
	
	private void createAttendee(){
		String username = usernameField.getText();
		char[] password = passwordField.getText().toCharArray();
		// Dialog to create the account
		PeopleCreateController createController = stageHandler.attachPeopleCreate(
				"Create Attendee Account", PersonType.ATTENDEE, username, password
		);
		stageHandler.showAndWait();
		syncCreated(createController.getCreated());
	}
	
	private void syncCreated(UUID created){
		if(created != null){
			String username = session.getPM().getName(created);
			usernameField.setText(username);
			respond.setText("Username filled!");
			respond.setTextFill(Color.BLACK);
		}
	}
	
	private class LoginPresenter implements LoginPresent{
		
		@Override
		public void respondUsernameNotExist(){
			respond.setText("Username Doesn't Exist!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void respondIncorrectPassword(){
			respond.setText("Incorrect Password! Please Try Again!");
			respond.setTextFill(Color.RED);
		}
	}
}
