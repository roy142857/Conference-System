package controller.center.dashboard;

import controller.session.Controller;
import controller.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presenter.center.dashboard.AttendeeDashboardPresent;
import usecase.dto.EventRecommendDTO;
import usecase.dto.PersonDTO;
import usecase.dto.PersonRecommendDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AttendeeDashboardController extends Controller{
	
	@FXML private Label                         welcome;
	@FXML private TableView<EventRecommendDTO>  recommendedEvents;
	@FXML private TableView<PersonRecommendDTO> recommendedFriends;
	
	private ObservableList<EventRecommendDTO>  rEvents;
	private ObservableList<PersonRecommendDTO> rFriends;
	private AttendeeDashboardPresent           presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new DashboardPresent();
		fetchView();
	}
	
	private void fetchView(){
		session.getPM().updateDashboardView(session.getLoggedIn(), presenter);
		session.getRM().updateRecommendFriends(session.getLoggedIn(), presenter);
//		session.getRM().updateRecommendEvents(session.getLoggedIn(), presenter); TODO Bug in Recommend Events
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// set up tables
		rEvents = FXCollections.observableArrayList();
		rFriends = FXCollections.observableArrayList();
		recommendedEvents.setItems(rEvents);
		recommendedFriends.setItems(rFriends);
		setUpEventTable();
		setUpFriendsTable();
	}
	
	private void setUpEventTable(){
		TableColumn<EventRecommendDTO, String> type = new TableColumn<>("Type");
		TableColumn<EventRecommendDTO, String> title = new TableColumn<>("Title");
		TableColumn<EventRecommendDTO, String> estimation = new TableColumn<>("Estimation");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		estimation.setCellValueFactory(new PropertyValueFactory<>("estimation"));
		recommendedEvents.getColumns().add(type);
		recommendedEvents.getColumns().add(title);
		recommendedEvents.getColumns().add(estimation);
		// set up parameters
		recommendedEvents.setEditable(false);
		recommendedEvents.setPlaceholder(new Label("No Recommending Events Yet!"));
		recommendedEvents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		recommendedEvents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		recommendedEvents.setRowFactory(evt -> getEventRow());
	}
	
	private void setUpFriendsTable(){
		TableColumn<PersonRecommendDTO, String> type = new TableColumn<>("Type");
		TableColumn<PersonRecommendDTO, String> name = new TableColumn<>("Name");
		TableColumn<PersonRecommendDTO, String> similarity = new TableColumn<>("Similarity");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		similarity.setCellValueFactory(new PropertyValueFactory<>("similarity"));
		recommendedFriends.getColumns().add(type);
		recommendedFriends.getColumns().add(name);
		recommendedFriends.getColumns().add(similarity);
		// set up parameters
		recommendedFriends.setEditable(false);
		recommendedFriends.setPlaceholder(new Label("No Recommending Friends Yet!"));
		recommendedFriends.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		recommendedFriends.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		recommendedFriends.setRowFactory(evt -> getFriendsRow());
	}
	
	private TableRow<EventRecommendDTO> getEventRow(){
		TableRow<EventRecommendDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::eventDoubleClickAction);
		return row;
	}
	
	private TableRow<PersonRecommendDTO> getFriendsRow(){
		TableRow<PersonRecommendDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::friendsDoubleClickAction);
		return row;
	}
	
	private void eventDoubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			openEvent();
		}
	}
	
	private void friendsDoubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			addFriend();
		}
	}
	
	private void openEvent(){
		EventRecommendDTO selected = recommendedEvents.getSelectionModel().getSelectedItem();
		// display a dialog to show event
		if(selected == null)
			return;
		stageHandler.attachEventView(selected.getID());
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void addFriend(){
		PersonRecommendDTO selected = recommendedFriends.getSelectionModel().getSelectedItem();
		// display a dialog to add friends
		if(selected == null)
			return;
		stageHandler.attachPeopleView(selected.getID());
		stageHandler.showAndWait();
		fetchView();
	}
	
	private class DashboardPresent implements AttendeeDashboardPresent{
		
		@Override
		public void updateWelcomeView(PersonDTO personDTO){
			welcome.setText(String.format(
					"Welcome %s! You are signed in as %s!",
					personDTO.getName(), personDTO.getType()
			));
		}
		
		@Override
		public void updateRecommendFriendView(List<PersonRecommendDTO> recommended){
			rFriends.clear();
			rFriends.addAll(recommended);
		}
		
		@Override
		public void updateRecommendEventView(List<EventRecommendDTO> recommended){
			rEvents.clear();
			rEvents.addAll(recommended);
		}
	}
}
