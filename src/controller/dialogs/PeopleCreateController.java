package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import presenter.people.PeopleCreatePresent;
import usecase.people.PersonType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class PeopleCreateController extends Controller{
	
	@FXML private TextField usernameField;
	@FXML private TextField passwordField;
	@FXML private Label     respond;
	
	@FXML private Button create;
	
	private PeopleCreatePresent presenter;
	private PersonType          type;
	private UUID                created;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new PeopleCreatePresenter();
	}
	
	public void init(PersonType type){
		this.type = type;
	}
	
	public void init(PersonType type, String username, char[] password){
		this.type = type;
		this.usernameField.setText(username);
		this.passwordField.setText(new String(password));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		create.setOnAction(actionEvent -> create());
	}
	
	private void create(){
		String username = usernameField.getText();
		char[] password = passwordField.getText().toCharArray();
		UUID created = session.getPM().create(type, username, password, presenter);
		if(created != null){
			stageHandler.close();
		}
		this.created = created;
	}
	
	public UUID getCreated(){
		return created;
	}
	
	private class PeopleCreatePresenter implements PeopleCreatePresent{
		
		@Override
		public void respondEmtpyUsername(){
			respond.setText("The Username is empty!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void respondEmtpyPassword(){
			respond.setText("The Password is empty!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void respondUsernameExist(){
			respond.setText("Username already exist!");
			respond.setTextFill(Color.RED);
		}
	}
}
