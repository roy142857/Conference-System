package controller.center;

import controller.session.Controller;
import controller.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presenter.center.MessageCenterPresent;
import usecase.dto.ChatDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageCenterController extends Controller{
	
	@FXML private TableView<ChatDTO> chatTable;
	
	private ObservableList<ChatDTO> chats;
	private MessageCenterPresent    presenter;
	
	public MessageCenterController(){}
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new MessageCenterPresenter();
		fetchView();
	}
	
	private void fetchView(){
		session.getMM().updateView(session.getLoggedIn(), presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		chats = FXCollections.observableArrayList();
		chatTable.setItems(chats);
		setUpChatTable();
	}
	
	private void setUpChatTable(){
		// add columns
		TableColumn<ChatDTO, String> name = new TableColumn<>("Name");
		TableColumn<ChatDTO, String> recentMessage = new TableColumn<>("Recent Message");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		recentMessage.setCellValueFactory(new PropertyValueFactory<>("mostRecent"));
		chatTable.getColumns().add(name);
		chatTable.getColumns().add(recentMessage);
		// set up parameters
		chatTable.setEditable(false);
		chatTable.setPlaceholder(new Label("No Chats Yet!"));
		chatTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		chatTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		chatTable.setRowFactory(evt -> getRow());
	}
	
	private TableRow<ChatDTO> getRow(){
		TableRow<ChatDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::doubleClickAction);
		return row;
	}
	
	private void doubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			openChat();
		}
	}
	
	private void openChat(){
		ChatDTO chat = chatTable.getSelectionModel().getSelectedItem();
		if(chat == null)
			return;
		stageHandler.attachChat(session.getMM().getChat(chat.getID()));
		stageHandler.showAndWait();
		fetchView();
	}
	
	private class MessageCenterPresenter implements MessageCenterPresent{
		
		@Override
		public void updateView(List<ChatDTO> chatsDTO){
			chats.clear();
			chats.addAll(chatsDTO);
		}
	}
	
}
