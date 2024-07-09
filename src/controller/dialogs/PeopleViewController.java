package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import presenter.people.AddFriendsPresent;
import presenter.people.PeopleViewPresent;
import usecase.commu.chat.manager.ChatManager;
import usecase.dto.PersonDTO;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;


public class PeopleViewController extends Controller{
	
	@FXML private Button launchChat;
	
	@FXML private Label type;
	@FXML private Label name;
	@FXML private Label points;
	@FXML private Label respond;
	
	@FXML private Button addFriend;
	@FXML private Button removeFriend;
	
	private PeopleViewPresent viewPresenter;
	private AddFriendsPresent addFriendsPresent;
	private UUID              person;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		viewPresenter = new PeopleViewPresenter();
		addFriendsPresent = new AddFriendsPresenter();
	}
	
	public void initPerson(UUID person){
		this.person = person;
		fetchView();
	}
	
	private void fetchView(){
		boolean isFriend = session.getFM().isFriend(session.getLoggedIn(), person);
		addFriend.setDisable(isFriend);
		removeFriend.setDisable(! isFriend);
		launchChat.setDisable(! session.getFM().hasRelation(session.getLoggedIn(), person));
		session.getPM().updateView(person, viewPresenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// hook up listeners
		launchChat.setOnAction(actionEvent -> launchChat());
		addFriend.setOnAction(actionEvent -> addFriend());
		removeFriend.setOnAction(actionEvent -> removeFriend());
	}
	
	private void launchChat(){
		ChatManager chatManager = session.getMM().getChat(session.getLoggedIn(), person);
		stageHandler.attachChat(chatManager);
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void addFriend(){
		if(session.getFM().addFriend(session.getLoggedIn(), person, addFriendsPresent)){
			stageHandler.close();
		}
		fetchView();
	}
	
	private void removeFriend(){
		if(session.getFM().removeFriend(session.getLoggedIn(), person, addFriendsPresent)){
			stageHandler.close();
		}
		fetchView();
	}
	
	private class PeopleViewPresenter implements PeopleViewPresent{
		
		@Override
		public void updateView(PersonDTO personDTO){
			type.setText(personDTO.getType());
			name.setText(personDTO.getName());
			points.setText(String.valueOf(personDTO.getPoints()));
		}
	}
	
	private class AddFriendsPresenter implements AddFriendsPresent{
		
		@Override
		public void respondNotYetFriends(){
			respond.setText("Not Friends Yet!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void respondAlreadyFriends(){
			respond.setText("Already Friends!");
			respond.setTextFill(Color.RED);
		}
	}
}
