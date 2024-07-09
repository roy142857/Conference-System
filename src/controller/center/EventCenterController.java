package controller.center;

import controller.session.Controller;
import controller.session.Session;
import gateway.PDFGenerate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presenter.center.EventCenterPresent;
import usecase.dto.EventDTO;
import usecase.event.EventSearchProperty;
import usecase.event.EventType;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventCenterController extends Controller{
	
	@FXML private ChoiceBox<EventSearchProperty> searchChoice;
	@FXML private TextField                      search;
	@FXML private Button                         createEvent;
	@FXML private Button                         downloadSchedule;
	
	@FXML private TableView<EventDTO> eventTable;
	
	private ObservableList<EventDTO> events;
	private DateTimeFormatter        dateFormatter;
	private DateTimeFormatter        timeFormatter;
	private EventCenterPresent       presenter;
	private PDFGenerate              pdfGenerate;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		// set up the availability
		createEvent.setDisable(! session.canOrganize());
		dateFormatter = DateTimeFormatter.ofPattern("MM/dd");
		timeFormatter = DateTimeFormatter.ofPattern("H:mm");
		presenter = new EventCenterPresenter();
		pdfGenerate = new PDFGenerate();
		fetchView();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// set up choices
		searchChoice.setItems(FXCollections.observableList(Arrays.asList(EventSearchProperty.values())));
		searchChoice.setValue(EventSearchProperty.ALL);
		search.setText("");
		// hook the listeners
		search.textProperty().addListener(actionEvent -> fetchView());
		searchChoice.valueProperty().addListener(actionEvent -> fetchView());
		createEvent.setOnAction(actionEvent -> createEvent());
		downloadSchedule.setOnAction(actionEvent -> downloadSchedule());
		// set up table
		events = FXCollections.observableArrayList();
		eventTable.setItems(events);
		setUpTable();
	}
	
	private void fetchView(){
		EventSearchProperty esp = searchChoice.getValue();
		String keyword = search.getText();
		session.getEM().updateView(esp, keyword, presenter);
	}
	
	private void setUpTable(){
		// add columns
		TableColumn<EventDTO, String> type = new TableColumn<>("Type");
		TableColumn<EventDTO, String> title = new TableColumn<>("Title");
		TableColumn<EventDTO, String> speaker = new TableColumn<>("Speaker");
		TableColumn<EventDTO, String> location = new TableColumn<>("Location");
		TableColumn<EventDTO, String> time = new TableColumn<>("Time");
		TableColumn<EventDTO, String> score = new TableColumn<>("Score");
		TableColumn<EventDTO, String> availability = new TableColumn<>("Availability");
		TableColumn<EventDTO, String> reward = new TableColumn<>("Reward");
		TableColumn<EventDTO, String> requirement = new TableColumn<>("Required Points");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		speaker.setCellValueFactory(new PropertyValueFactory<>("speaker"));
		location.setCellValueFactory(new PropertyValueFactory<>("location"));
		time.setCellValueFactory(ef -> new SimpleStringProperty(getTimeRange(ef.getValue())));
		score.setCellValueFactory(ef -> new SimpleStringProperty(getScore(ef.getValue())));
		availability.setCellValueFactory(ef -> new SimpleStringProperty(getAvailability(ef.getValue())));
		reward.setCellValueFactory(ef -> new SimpleStringProperty(getReward(ef.getValue())));
		requirement.setCellValueFactory(ef -> new SimpleStringProperty(getRequirement(ef.getValue())));
		eventTable.getColumns().add(type);
		eventTable.getColumns().add(title);
		eventTable.getColumns().add(speaker);
		eventTable.getColumns().add(location);
		eventTable.getColumns().add(time);
		eventTable.getColumns().add(score);
		eventTable.getColumns().add(availability);
		eventTable.getColumns().add(reward);
		eventTable.getColumns().add(requirement);
		// set up parameters
		eventTable.setEditable(false);
		eventTable.setPlaceholder(new Label("No Events Yet!"));
		eventTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		eventTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		eventTable.setRowFactory(evt -> getRow());
	}
	
	private TableRow<EventDTO> getRow(){
		TableRow<EventDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::doubleClickAction);
		return row;
	}
	
	private void doubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			if(session.canOrganize() || session.canSpeak()){
				editEvent();
			}else{
				viewEvent();
			}
		}
	}
	
	private String getScore(EventDTO eventDTO){
		return String.valueOf(eventDTO.getScore());
	}
	
	private String getRequirement(EventDTO eventDTO){
		return String.valueOf(eventDTO.getRequirement());
	}
	
	private String getReward(EventDTO eventDTO){
		return String.valueOf(eventDTO.getReward());
	}
	
	private String getAvailability(EventDTO eventDTO){
		int capacity = eventDTO.getCapacity();
		int signedUpCount = eventDTO.getSignedUp().size();
		return String.format(
				"%d/%d",
				signedUpCount, capacity
		);
	}
	
	private String getTimeRange(EventDTO eventDTO){
		LocalDateTime start = eventDTO.getStartTime();
		LocalDateTime end = eventDTO.getEndTime();
		String result;
		if(start.toLocalDate().equals(end.toLocalDate())){
			result = String.format(
					"%s %s ~ %s",
					start.format(dateFormatter),
					start.format(timeFormatter), end.format(timeFormatter)
			);
		}else{
			result = String.format(
					"%s %s ~ %s %s",
					start.format(dateFormatter), start.format(timeFormatter),
					end.format(dateFormatter), end.format(timeFormatter)
			);
		}
		return result;
	}
	
	private void downloadSchedule(){
		// first prompt to get a file path
		Map<String, String> filter = new HashMap<>();
		filter.put("PDF files", "*.pdf");
		File file = stageHandler.showFileChooser("Choose a Location To Save", "schedule.pdf", filter);
		if(file == null)
			return;
		// get the necessary data from usecase
		String html = session.getDM().generateAllEventScheduleHTML();
		try{
			String filePath = file.getAbsolutePath();
			// generate PDF
			pdfGenerate.writeTo(file, html);
			stageHandler.showInfoDialog("PDF Created", "Schedule Output success!", "PDF create in >> " + filePath);
		}catch(Exception e){
			stageHandler.showExceptionDialog(e);
		}
	}
	
	private void createEvent(){
		// first prompt to choose a type of event to create.
		Optional<EventType> result = stageHandler.showChoiceDialog(
				"Event Create",
				"Choose a Type of Event to Create",
				Arrays.asList(EventType.values()), EventType.TALK
		);
		if(result.isPresent()){
			// opens up the event edit to create a new event.
			stageHandler.attachEventCreate(result.get());
			stageHandler.showAndWait();
		}
		fetchView();
	}
	
	private void editEvent(){
		// opens up the event edit to change the event
		EventDTO selected = getSelected();
		if(selected == null)
			return;
		if(session.getEM().isManagedBy(selected.getID(), session.getLoggedIn())){
			stageHandler.attachEventEdit(selected.getID());
			stageHandler.showAndWait();
			fetchView();
		}else{
			// if it is not managed by the logged in
			// we switch to view
			viewEvent();
		}
	}
	
	private void viewEvent(){
		// open up the event view to view the event detail
		EventDTO selected = getSelected();
		if(selected == null)
			return;
		stageHandler.attachEventView(selected.getID());
		stageHandler.showAndWait();
		fetchView();
	}
	
	private EventDTO getSelected(){
		return eventTable.getSelectionModel().getSelectedItem();
	}
	
	private class EventCenterPresenter implements EventCenterPresent{
		
		@Override
		public void updateView(List<EventDTO> eventDTOS){
			events.clear();
			events.addAll(eventDTOS);
		}
	}
}
