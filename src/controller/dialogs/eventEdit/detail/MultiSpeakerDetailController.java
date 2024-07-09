package controller.dialogs.eventEdit.detail;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.RangeSlider;
import presenter.event.EventEditPresent;
import usecase.dto.PersonDTO;
import usecase.dto.builder.EventBuilder;
import usecase.dto.builder.MultiSpeakerEventBuilder;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class MultiSpeakerDetailController extends DetailController{
	
	// No Supported Multiple selection, consider using ControlFX
	@FXML private CheckComboBox<PersonDTO> speaker;
	
	@FXML private TextField   title;
	@FXML private TextField   location;
	@FXML private DatePicker  date;
	@FXML private RangeSlider time;
	@FXML private Label       timePreview;
	@FXML private Slider      capacity;
	@FXML private Slider      reward;
	@FXML private Slider      requirement;
	
	private MultiSpeakerEventBuilder  builder;
	private ObservableList<PersonDTO> speakers;
	
	@Override
	public void initEvent(UUID event, EventBuilder builder){
		super.initEvent(event, builder);
		if(! (builder instanceof MultiSpeakerEventBuilder))
			throw new IllegalArgumentException("Multi Speaker Builder Required!");
		MultiSpeakerEventBuilder multiSpeakerBuilder = (MultiSpeakerEventBuilder) builder;
		this.builder = multiSpeakerBuilder;
		speakers.addAll(session.getPM().getAllSpeakerDTO());
		speakers.stream()
		        .filter(dto -> multiSpeakerBuilder.getSpeakers().contains(dto.getID()))
		        .forEach(selected -> speaker.getCheckModel().check(selected));
	}
	
	@Override
	protected void fetchView(){}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		speakers = speaker.getItems();
		speaker.setConverter(new SpeakerConverter(speakers));
	}
	
	@Override
	public boolean create(EventEditPresent presenter){
		if(! super.create(presenter)) throw new IllegalStateException("Parent Create Failed!");
		builder.setSpeakers(speaker.getCheckModel().getCheckedItems().stream()
		                           .map(PersonDTO::getID)
		                           .collect(Collectors.toSet()));
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
