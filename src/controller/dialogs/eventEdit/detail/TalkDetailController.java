package controller.dialogs.eventEdit.detail;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.RangeSlider;
import presenter.event.EventEditPresent;
import usecase.dto.PersonDTO;
import usecase.dto.builder.EventBuilder;
import usecase.dto.builder.TalkBuilder;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;


public class TalkDetailController extends DetailController{
	
	@FXML private ChoiceBox<PersonDTO> speaker;
	
	@FXML private TextField   title;
	@FXML private TextField   location;
	@FXML private DatePicker  date;
	@FXML private RangeSlider time;
	@FXML private Label       timePreview;
	@FXML private Slider      capacity;
	@FXML private Slider      reward;
	@FXML private Slider      requirement;
	
	private TalkBuilder               builder;
	private ObservableList<PersonDTO> speakers;
	
	@Override
	public void initEvent(UUID event, EventBuilder builder){
		super.initEvent(event, builder);
		if(! (builder instanceof TalkBuilder))
			throw new IllegalArgumentException("Talk Builder Required!");
		TalkBuilder talkBuilder = (TalkBuilder) builder;
		this.builder = talkBuilder;
		speakers.addAll(session.getPM().getAllSpeakerDTO());
		speakers.stream()
		        .filter(dto -> dto.getID().equals(talkBuilder.getSpeaker()))
		        .findAny()
		        .ifPresent(selected -> speaker.getSelectionModel().select(selected));
	}
	
	@Override
	protected void fetchView(){}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		speakers = FXCollections.observableArrayList();
		speaker.setItems(speakers);
		speaker.setConverter(new SpeakerConverter(speakers));
	}
	
	@Override
	public boolean create(EventEditPresent presenter){
		if(! super.create(presenter)) throw new IllegalStateException("Parent Create Failed!");
		PersonDTO selected = speaker.getSelectionModel().getSelectedItem();
		if(selected != null)
			builder.setSpeaker(selected.getID());
		// call the usecase to create the Talk
		if(event == null){
			// we are creating
			return session.getEM().createEvent(builder, presenter);
		}else{
			// we are editing
			return session.getEM().changeEvent(event, builder, presenter);
		}
	}
	
	@Override
	public boolean cancel(EventEditPresent presenter){
		if(! super.cancel(presenter)) throw new IllegalStateException("Parent Cancel Failed!");
		return session.getEM().removeEvent(event, presenter);
	}
	
	
	@Override
	public TextField getTitle(){
		return title;
	}
	
	@Override
	public TextField getLocation(){
		return location;
	}
	
	@Override
	public DatePicker getDate(){
		return date;
	}
	
	@Override
	protected RangeSlider getTime(){
		return time;
	}
	
	@Override
	public Label getTimePreview(){
		return timePreview;
	}
	
	@Override
	public Slider getCapacity(){
		return capacity;
	}
	
	@Override
	public Slider getReward(){
		return reward;
	}
	
	@Override
	public Slider getRequirement(){
		return requirement;
	}
	
	@Override
	protected EventBuilder getBuilder(){
		return builder;
	}
}
