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
import presenter.center.TaskCenterPresent;
import usecase.dto.TaskDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TaskCenterController extends Controller{
	
	@FXML private TableView<TaskDTO> taskTable;
	
	private ObservableList<TaskDTO> tasks;
	private TaskCenterPresent       presenter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new TaskCenterPresenter();
		fetchView();
	}
	
	private void fetchView(){
		session.getTM().updateView(session.getLoggedIn(), presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		tasks = FXCollections.observableArrayList();
		taskTable.setItems(tasks);
		setUpTaskTable();
	}
	
	private void setUpTaskTable(){
		// add columns
		TableColumn<TaskDTO, String> type = new TableColumn<>("Type");
		TableColumn<TaskDTO, String> name = new TableColumn<>("Title");
		type.setCellValueFactory(new PropertyValueFactory<>("type"));
		name.setCellValueFactory(new PropertyValueFactory<>("title"));
		taskTable.getColumns().add(type);
		taskTable.getColumns().add(name);
		// set up parameters
		taskTable.setEditable(false);
		taskTable.setPlaceholder(new Label("No Tasks Yet!"));
		taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		taskTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		taskTable.setRowFactory(evt -> getRow());
	}
	
	private TableRow<TaskDTO> getRow(){
		TableRow<TaskDTO> row = new TableRow<>();
		row.setOnMouseClicked(this::doubleClickAction);
		return row;
	}
	
	private void doubleClickAction(MouseEvent event){
		if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
			viewTask();
		}
	}
	
	private void viewTask(){
		// open event view
		TaskDTO selected = getSelected();
		if(selected == null)
			return;
		if(session.getEM().isManagedBy(selected.getTaskFor(), session.getLoggedIn())){
			stageHandler.attachEventEdit(selected.getTaskFor());
		}else{
			stageHandler.attachEventView(selected.getTaskFor());
		}
		stageHandler.showAndWait();
		fetchView();
	}
	
	private TaskDTO getSelected(){
		return taskTable.getSelectionModel().getSelectedItem();
	}
	
	private class TaskCenterPresenter implements TaskCenterPresent{
		
		@Override
		public void updateView(List<TaskDTO> tasksDTO){
			tasks.clear();
			tasks.addAll(tasksDTO);
		}
	}
}
