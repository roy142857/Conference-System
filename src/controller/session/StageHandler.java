package controller.session;

import controller.center.MenuController;
import controller.dialogs.*;
import controller.dialogs.eventEdit.EventEditController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.*;
import usecase.commu.chat.manager.ChatManager;
import usecase.event.EventType;
import usecase.people.PersonType;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

//@SuppressWarnings()
public class StageHandler{
	
	private final static String MENU_FXML          = "/javafx/center/menu.fxml";
	private final static String LOGIN_FXML         = "/javafx/dialog/login.fxml";
	private final static String PEOPLE_CREATE_FXML = "/javafx/dialog/peopleCreate.fxml";
	private final static String PEOPLE_VIEW_FXML   = "/javafx/dialog/peopleView.fxml";
	private final static String CHAT_FXML          = "/javafx/dialog/chat.fxml";
	private final static String GROUP_MESSAGE_FXML = "/javafx/dialog/groupMessage.fxml";
	private final static String EVENT_VIEW         = "/javafx/dialog/eventView.fxml";
	private final static String EVENT_EDIT_FXML    = "/javafx/dialog/eventEdit/eventEdit.fxml";
	private final static String EVENT_RATING_FXML  = "/javafx/dialog/eventRating.fxml";
	
	private final Deque<Stage> stageDeque;
	private final Session      session;
	
	public StageHandler(Session session){
		this.stageDeque = new ArrayDeque<>();
		this.session = session;
	}
	
	public MenuController attachMenu(String title, Stage primaryStage){
		// load the fxml
		FXMLLoader loader = getLoader(MENU_FXML);
		Parent root = loadRoot(loader);
		pushStage(primaryStage);
		// load the controller
		MenuController menuController = loader.getController();
		menuController.initSession(session);
		menuController.initStageHandler(this);
		// set up stage
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.setOnShown(windowEvent -> menuController.onShown());
		primaryStage.setOnShowing(windowEvent -> menuController.onShowing());
		return menuController;
	}
	
	public void pushStage(Stage stage){
		stageDeque.push(stage);
	}
	
	public Stage peekStage(){
		return stageDeque.peek();
	}
	
	public void show(){
		peekStage().show();
	}
	
	public void showAndWait(){
		debugPrint();
		peekStage().showAndWait();
	}
	
	public void hide(){
		peekStage().hide();
	}
	
	public void close(){
		debugPrint();
		stageDeque.pop().close();
	}
	
	private void debugPrint(){
		System.out.println(stageDeque.stream().map(Stage::getTitle).collect(Collectors.joining(" <- ")));
	}
	
	public LoginController attachLogin(String title){
		return setUpStage(
				LOGIN_FXML, title
		);
	}
	
	public ChatController attachChat(ChatManager chat){
		ChatController chatController = setUpStage(
				CHAT_FXML, chat.getChatName()
		);
		chatController.initChat(chat);
		return chatController;
	}
	
	public GroupMessageController attachGroupMessage(String title, List<UUID> messagingTo){
		GroupMessageController groupMessageController = setUpStage(
				GROUP_MESSAGE_FXML, title
		);
		groupMessageController.initMessagingTo(messagingTo);
		return groupMessageController;
	}
	
	public PeopleViewController attachPeopleView(UUID people){
		PeopleViewController viewController = setUpStage(
				PEOPLE_VIEW_FXML, "View Person Detail"
		);
		viewController.initPerson(people);
		return viewController;
	}
	
	public PeopleCreateController attachPeopleCreate(String title, PersonType type, String username, char[] password){
		PeopleCreateController peopleCreate = setUpStage(
				PEOPLE_CREATE_FXML, title
		);
		peopleCreate.init(type, username, password);
		return peopleCreate;
	}
	
	public PeopleCreateController attachPeopleCreate(String title, PersonType type){
		PeopleCreateController peopleCreate = setUpStage(
				PEOPLE_CREATE_FXML, title
		);
		peopleCreate.init(type);
		return peopleCreate;
	}
	
	public EventRatingController attachEventRating(UUID event){
		EventRatingController eventRating = setUpStage(
				EVENT_RATING_FXML, "Event Evaluation"
		);
		eventRating.initEvent(event);
		return eventRating;
	}
	
	public EventViewController attachEventView(UUID event){
		EventViewController eventViewController = setUpStage(
				EVENT_VIEW, "View Event"
		);
		
		eventViewController.initEvent(event);
		return eventViewController;
	}
	
	public EventEditController attachEventEdit(UUID event){
		EventEditController eventEditController = setUpStage(
				EVENT_EDIT_FXML, "Edit Event"
		);
		eventEditController.initEvent(event);
		return eventEditController;
	}
	
	public EventEditController attachEventCreate(EventType type){
		EventEditController eventEditController = setUpStage(
				EVENT_EDIT_FXML, "Create Event"
		);
		eventEditController.initEvent(type);
		return eventEditController;
	}
	
	public void showExceptionDialog(Exception e){
		Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
		exceptionAlert.setTitle("Exception Dialog");
		exceptionAlert.setHeaderText(null);
		exceptionAlert.setContentText("The Stack Trace is as follow:");
		exceptionAlert.initOwner(peekStage());
		exceptionAlert.initModality(Modality.WINDOW_MODAL);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		// add an text area to display the text
		TextArea textArea = new TextArea(sw.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane pane = new GridPane();
		pane.add(textArea, 0, 0);
		exceptionAlert.getDialogPane().setContent(pane);
		// display the dialog
		exceptionAlert.showAndWait();
	}
	
	public <E> Optional<E> showChoiceDialog(String title, String contentMessage, List<E> choices, E defaultChoice){
		ChoiceDialog<E> dialog = new ChoiceDialog<>(defaultChoice, choices);
		dialog.setTitle(title);
		dialog.initOwner(peekStage());
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.setContentText(contentMessage);
		return dialog.showAndWait();
	}
	
	public File showFileChooser(String title, String initFileName, Map<String, String> fileFilter){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.setInitialFileName(initFileName);
		fileChooser.getExtensionFilters().addAll(
				fileFilter.entrySet().stream().sorted(Map.Entry.comparingByKey())
				          .map(entry -> new FileChooser.ExtensionFilter(entry.getKey(), entry.getValue()))
				          .collect(Collectors.toList())
		);
		return fileChooser.showSaveDialog(peekStage());
	}
	
	public void showInfoDialog(String title, String header, String content){
		Alert info = new Alert(Alert.AlertType.INFORMATION);
		info.setTitle(title);
		info.setHeaderText(header);
		info.setContentText(content);
		info.initOwner(peekStage());
		info.showAndWait();
	}
	
	public boolean showConfirmDialog(String title, String header, String content){
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle(title);
		confirm.setHeaderText(header);
		confirm.setContentText(content);
		confirm.initOwner(peekStage());
		confirm.getButtonTypes().clear();
		confirm.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = confirm.showAndWait();
		return result.isPresent() && result.get().equals(ButtonType.YES);
	}
	
	public FXMLLoader getLoader(String path){
		return new FXMLLoader(getClass().getResource(path));
	}
	
	public Parent loadRoot(FXMLLoader loader){
		try{
			return loader.load();
		}catch(IOException e){
			showExceptionDialog(e);
			e.printStackTrace();
			return null;
		}
	}
	
	private <T extends Controller> T setUpStage(String path, String title){
		FXMLLoader loader = getLoader(path);
		Parent root = loadRoot(loader);
		createStage(title, root);
		Controller controller = loader.getController();
		controller.initSession(session);
		controller.initStageHandler(this);
		return (T) controller;
	}
	
	private Stage createStage(String title, Parent root){
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.setScene(scene);
		stage.initOwner(peekStage());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setOnShowing(event -> stage.hide());
		stage.setOnShown(event -> setXY(stage.getOwner(), stage));
		stage.setOnCloseRequest(windowEvent -> stageDeque.pop());
		pushStage(stage);
		return stage;
	}
	
	private void setXY(Window parent, Stage stage){
		double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
		double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
		stage.setX(x);
		stage.setY(y);
		stage.show();
	}
}
