package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import presenter.chat.GroupMessagePresent;
import usecase.dto.PersonDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class GroupMessageController extends Controller{
	
	@FXML private TableView<PersonDTO> messagingList;
	
	@FXML private TextArea respond;
	@FXML private TextArea inputArea;
	@FXML private Button   send;
	
	private ObservableList<PersonDTO> messagingTo;
	private GroupMessagePresent       presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new GroupMessagePresenter();
	}
	
	public void initMessagingTo(List<UUID> messagingTo){
		this.messagingTo.addAll(session.getPM().convert(messagingTo));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		messagingTo = FXCollections.observableArrayList();
		messagingList.setItems(messagingTo);
		setUpColumns();
		setUpRespond();
		// hook the listeners
		send.setOnAction(actionEvent -> sendMessage());
		inputArea.setOnKeyPressed(this::sendMessageKeyPress);
	}
	
	private void setUpRespond(){
		respond.setEditable(false);
		respond.setWrapText(true);
	}
	
	private void setUpColumns(){
		messagingList.setPlaceholder(new Label("No Messaging Target!"));
		TableColumn<PersonDTO, String> type = new TableColumn<>("Type");
		TableColumn<PersonDTO, String> name = new TableColumn<>("Name");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		messagingList.getColumns().add(type);
		messagingList.getColumns().add(name);
		messagingList.setEditable(false);
		messagingList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		messagingList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	private void sendMessageKeyPress(KeyEvent keyEvent){
		if(keyEvent.isShiftDown() && keyEvent.getCode().equals(KeyCode.ENTER)){
			sendMessage();
		}
	}
	
	private void sendMessage(){
		String input = inputArea.getText();
		// send the message
		List<UUID> ids = messagingTo.stream().map(PersonDTO::getID).collect(Collectors.toList());
		if(session.getMM().groupMessage(input, session.getLoggedIn(), ids, presenter)){
			inputArea.clear();
		}
	}
	
	private void newSection(){
		int width = respond.getPrefColumnCount();
		for(int i = 0; i < width; i++)
			respond.appendText("=");
		respond.appendText("\n");
	}
	
	private void scrollToBottom(){
		respond.appendText("");
	}
	
	private void appendLine(String format, Object... objs){
		respond.appendText(String.format(format + "\n", objs));
	}
	
	private class GroupMessagePresenter implements GroupMessagePresent{
		
		@Override
		public void respondStarting(int total){
			newSection();
			appendLine("Sending Messaging to %d people...", total);
			scrollToBottom();
		}
		
		@Override
		public void respondPersonStatus(UUID person, boolean success){
			appendLine(
					"Send to \"%s\" %s!",
					session.getPM().getName(person), success ? "success" : "fail"
			);
			scrollToBottom();
		}
		
		@Override
		public void respondEnding(int success, int total){
			appendLine("Sent Message finished, %d/%d succeed!", success, total);
			scrollToBottom();
		}
	}
}
