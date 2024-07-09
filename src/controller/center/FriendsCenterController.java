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
import presenter.center.PeopleCenterPresent;
import usecase.dto.PersonDTO;
import usecase.people.PersonType;

import java.net.URL;
import java.util.*;

public class FriendsCenterController extends Controller{
	
	@FXML private Button createPerson;
	@FXML private Button messageAllAttendee;
	@FXML private Button messageAllSpeaker;
	
	@FXML private TableView<PersonDTO> friendTable;
	@FXML private TableView<PersonDTO> coworkerTable;
	
	private ObservableList<PersonDTO> friends;
	private ObservableList<PersonDTO> coWorker;
	private PeopleCenterPresent       presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		createPerson.setDisable(! session.canOrganize());
		messageAllAttendee.setDisable(! session.canOrganize());
		messageAllSpeaker.setDisable(! session.canOrganize());
		presenter = new PeopleCenterPresenter();
		fetchView();
	}
	
	private void fetchView(){
		session.getPM().updateCenterView(session.getLoggedIn(), presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		friends = FXCollections.observableArrayList();
		coWorker = FXCollections.observableArrayList();
		friendTable.setItems(friends);
		coworkerTable.setItems(coWorker);
		setUpFriendsTable();
		setUpCoWorkerTable();
		// hook up listeners
		createPerson.setOnAction(actionEvent -> createPerson());
		messageAllAttendee.setOnAction(actionEvent -> messageAllAttendee());
		messageAllSpeaker.setOnAction(actionEvent -> messageAllSpeaker());
	}
	
	
	private void setUpFriendsTable(){
		// add columns
		TableColumn<PersonDTO, String> type = new TableColumn<>("Type");
		TableColumn<PersonDTO, String> name = new TableColumn<>("Name");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		friendTable.getColumns().add(type);
		friendTable.getColumns().add(name);
		// set up parameters
		friendTable.setEditable(false);
		friendTable.setPlaceholder(new Label("No Friends Yet!"));
		friendTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		friendTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		friendTable.setRowFactory(evt -> getRow());
		friendTable.getSelectionModel().selectedItemProperty().addListener((obs, oldS, newS) -> sync(friendTable));
	}
	
	private void setUpCoWorkerTable(){
		// add columns
		TableColumn<PersonDTO, String> type = new TableColumn<>("Type");
		TableColumn<PersonDTO, String> name = new TableColumn<>("name");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		coworkerTable.getColumns().add(type);
		coworkerTable.getColumns().add(name);
		// set up parameters
		coworkerTable.setEditable(false);
		coworkerTable.setPlaceholder(new Label("No CoWorker Yet!"));
		coworkerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		coworkerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		coworkerTable.setRowFactory(evt -> getRow());
		coworkerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldS, newS) -> sync(coworkerTable));
	}
	
	private TableRow<PersonDTO> getRow(){
		TableRow<PersonDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::doubleClickAction);
		return row;
	}
	
	private void doubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			openPersonDetail();
		}
	}
	
	private void sync(TableView<PersonDTO> initiator){
		if(initiator.equals(friendTable)){
			coworkerTable.getSelectionModel().clearSelection();
		}else{
			friendTable.getSelectionModel().clearSelection();
		}
	}
	
	private void messageAllAttendee(){
		// Dialog to Message All Attendee
		List<UUID> attendees = session.getPM().getAllAttendee();
		stageHandler.attachGroupMessage("To All Attendees", attendees);
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void messageAllSpeaker(){
		// Dialog to Message All Speaker
		List<UUID> speakers = session.getPM().getAllSpeaker();
		stageHandler.attachGroupMessage("To All Speakers", speakers);
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void createPerson(){
		// first choose a person type
		Optional<PersonType> type = stageHandler.showChoiceDialog(
				"Type Selection", "Choose a Type to Create",
				Arrays.asList(PersonType.values()), PersonType.SPEAKER
		);
		if(type.isPresent()){
			stageHandler.attachPeopleCreate("Create " + type, type.get());
			stageHandler.showAndWait();
		}
		fetchView();
	}
	
	private void openPersonDetail(){
		PersonDTO selected = getSelected();
		if(selected == null)
			return;
		// dialog to Display Detail
		stageHandler.attachPeopleView(selected.getID());
		stageHandler.showAndWait();
		fetchView();
	}
	
	private PersonDTO getSelected(){
		if(friendTable.getSelectionModel().isEmpty()){
			return coworkerTable.getSelectionModel().getSelectedItem();
		}else{
			return friendTable.getSelectionModel().getSelectedItem();
		}
	}
	
	private class PeopleCenterPresenter implements PeopleCenterPresent{
		
		@Override
		public void updateCoworkerView(List<PersonDTO> coworkerDTO){
			coWorker.clear();
			coWorker.addAll(coworkerDTO);
		}
		
		@Override
		public void updateFriendsView(List<PersonDTO> friendsDTO){
			friends.clear();
			friends.addAll(friendsDTO);
		}
	}
}
