package controller.dialogs.eventEdit;

import controller.dialogs.eventEdit.detail.DetailController;
import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import presenter.event.EventEditPresent;
import usecase.dto.EventDTO;
import usecase.dto.TaskDTO;
import usecase.dto.builder.EventBuilder;
import usecase.event.EventType;
import usecase.event.EventTypeVisitor;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * edit view
 *
 * respond (label)
 * createEvent
 * cancelEvent
 */
public class EventEditController extends Controller{
	
	private final String TALK_DETAIL_FXML          = "/javafx/dialog/eventEdit/detail/talkDetail.fxml";
	private final String NON_SPEAKER_DETAIL_FXML   = "/javafx/dialog/eventEdit/detail/nonSpeakerDetail.fxml";
	private final String MULTI_SPEAKER_DETAIL_FXML = "/javafx/dialog/eventEdit/detail/multiSpeakerDetail.fxml";
	
	@FXML private Button messageAllAttendee;
	
	@FXML private BorderPane detailPane;
	
	@FXML private TextArea respond;
	@FXML private Button   confirm;
	@FXML private Button   cancel;
	
	private EventEditPresent  presenter;
	private DetailController  detailController;
	private DateTimeFormatter formatter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		presenter = new EventEditPresenter();
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
	}
	
	public void initEvent(UUID event){
		EventBuilder builder = session.getEM().getEventBuilder(event);
		session.getEM().acceptTypeVisitor(event, new EventInitVisitor(builder));
	}
	
	public void initEvent(EventType type){
		EventBuilder builder = session.getEM().getEventBuilder(type);
		session.getEM().acceptTypeVisitor(type, new EventInitVisitor(builder));
		// on create mode, we can't message attendees
		cancel.setDisable(true);
		messageAllAttendee.setDisable(true);
	}
	
	private void constructEventDetail(UUID event, EventBuilder builder, String fxml){
		FXMLLoader loader = stageHandler.getLoader(fxml);
		Parent root = stageHandler.loadRoot(loader);
		DetailController controller = loader.getController();
		controller.initSession(session);
		controller.initStageHandler(stageHandler);
		controller.initEvent(event, builder);
		root.setDisable(! session.canOrganize());
		cancel.setDisable(! session.canOrganize());
		detailPane.setCenter(root);
		detailController = controller;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		setUpRespond();
		// hook some listener
		messageAllAttendee.setOnAction(actionEvent -> detailController.messageAllAttendee());
		confirm.setOnAction(actionEvent -> createEvent());
		cancel.setOnAction(actionEvent -> cancelEvent());
	}
	
	private void setUpRespond(){
		respond.setEditable(false);
		respond.setWrapText(true);
	}
	
	private void createEvent(){
		if(detailController.create(presenter)){
			stageHandler.close();
		}
	}
	
	private void cancelEvent(){
		// first get a confirmation
		if(! stageHandler.showConfirmDialog("Cancel Event Confirmation",
		                                    "Are you sure to cancel the event?",
		                                    "Canceling the event will:\n" +
		                                    "- Delete all current appointments to this event.\n" +
		                                    "- Delete all existing speaker's duty to this event.\n" +
		                                    "- Remove the announcement group for this event."))
			return;
		if(detailController.cancel(presenter)){
			stageHandler.close();
		}
	}
	
	private void scrollToBottom(){
		respond.appendText("");
	}
	
	private void appendLine(String format, Object... objs){
		respond.appendText(String.format(format + "\n", objs));
	}
	
	private class EventEditPresenter implements EventEditPresent{
		
		@Override
		public void respondConflictingEvents(List<EventDTO> conflictingEvents){
			appendLine("Create Failed, Due to the following Conflicting Events!");
			for(EventDTO c : conflictingEvents){
				appendLine(
						"\t%s (%s) at %s ~ %s",
						c.getTitle(),
						c.getType(),
						c.getStartTime().format(formatter),
						c.getEndTime().format(formatter)
				);
			}
			scrollToBottom();
		}
		
		@Override
		public void respondCapacityTooSmall(){
			appendLine("Capacity is Too Small!");
			scrollToBottom();
		}
		
		@Override
		public void respondChangeNotSupported(String type){
			appendLine("Change in %s is not supported!", type);
			scrollToBottom();
		}
		
		@Override
		public void respondInvalidTime(){
			appendLine("Time is invalid!");
			scrollToBottom();
		}
		
		@Override
		public void respondAlreadyStarted(){
			appendLine("Event has already Started, so can't be changed!");
			scrollToBottom();
		}
		
		@Override
		public void respondPleaseSet(String type){
			appendLine("Please set a %s!", type);
			scrollToBottom();
		}
		
		@Override
		public void respondConflictingTasks(List<TaskDTO> conflictingTasks){
			appendLine("Create Failed, Due to the following Conflicting Speaker's Tasks!");
			for(TaskDTO t : conflictingTasks){
				appendLine(
						"\t%s : %s for %s",
						t.getOwner(),
						t.getType(),
						t.getTitle()
				);
			}
			scrollToBottom();
		}
		
		@Override
		public void respondAlreadyOwnTask(){
			appendLine("Speaker(s) are already the speaker for this event!");
			scrollToBottom();
		}
	}
	
	private class EventInitVisitor implements EventTypeVisitor{
		
		private final EventBuilder builder;
		
		private EventInitVisitor(EventBuilder builder){
			this.builder = builder;
		}
		
		@Override
		public void isTalk(UUID talk){
			constructEventDetail(talk, builder, TALK_DETAIL_FXML);
		}
		
		@Override
		public void isNonSpeakerEvent(UUID nse){
			constructEventDetail(nse, builder, NON_SPEAKER_DETAIL_FXML);
		}
		
		@Override
		public void isMultiSpeakerEvent(UUID mse){
			constructEventDetail(mse, builder, MULTI_SPEAKER_DETAIL_FXML);
		}
	}
}
