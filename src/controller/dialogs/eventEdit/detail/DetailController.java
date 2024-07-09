package controller.dialogs.eventEdit.detail;


import controller.session.Controller;
import controller.session.Session;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.control.RangeSlider;
import presenter.event.EventEditPresent;
import usecase.dto.builder.EventBuilder;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public abstract class DetailController extends Controller{
	
	private DateTimeFormatter previewFormatter;
	
	protected UUID event;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		previewFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
	}
	
	public void initEvent(UUID event, EventBuilder builder){
		this.event = event;
		getTitle().setText(builder.getTitle());
		getLocation().setText(builder.getLocation());
		initDateTime(builder);
		getCapacity().setValue(builder.getCapacity());
		getReward().setValue(builder.getReward());
		getRequirement().setValue(builder.getRequirement());
	}
	
	// init date, startTime, duration, time preview
	private void initDateTime(EventBuilder builder){
		LocalDateTime start = builder.getStartTime();
		LocalDateTime end = builder.getEndTime();
		if(start == null || end == null){
			getDate().setValue(LocalDate.now());
			setTime(LocalTime.of(0, 0), LocalTime.of(23, 59));
		}else{
			getDate().setValue(start.toLocalDate());
			setTime(start.toLocalTime(), end.toLocalTime());
		}
	}
	
	private void setTime(LocalTime start, LocalTime end){
		double startTime = toValue(start);
		double endTime = toValue(end);
		getTime().setHighValue(endTime);
		getTime().setLowValue(startTime);
	}
	
	private double toValue(LocalTime time){
		return time.toSecondOfDay() / 60.d;
	}
	
	protected abstract void fetchView();
	
	public boolean create(EventEditPresent presenter){
		EventBuilder builder = getBuilder();
		builder.setOrganizer(session.getLoggedIn());
		builder.setTitle(getTitle().getText());
		builder.setLocation(getLocation().getText());
		createSetStartEndTime(builder);
		builder.setCapacity((int) getCapacity().getValue());
		builder.setReward((int) getReward().getValue());
		builder.setRequirement((int) getRequirement().getValue());
		return true;
	}
	
	public boolean cancel(EventEditPresent presenter){
		// for now it is true
		return true;
	}
	
	public void messageAllAttendee(){
		// Dialog to Message All attendees
		List<UUID> attendees = new ArrayList<>(session.getEM().getSignedUp(event));
		String title = session.getEM().getTitle(event);
		stageHandler.attachGroupMessage("To Attendees of " + title, attendees);
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void createSetStartEndTime(EventBuilder builder){
		LocalDateTime[] pair = getStartEndPair();
		builder.setStartTime(pair[0]);
		builder.setEndTime(pair[1]);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		setUpTime();
		setUpCapacity();
		setUpReward();
		setUpRequirement();
		// hook up listeners
		getDate().valueProperty().addListener((ov, n, t1) -> updateTimePreview());
	}
	
	private void setUpTime(){
		RangeSlider time = getTime();
		time.setShowTickMarks(true);
		time.setShowTickLabels(true);
		time.setMax(1439);
		time.setMin(0);
		time.setBlockIncrement(1);
		time.setMajorTickUnit(60);
		time.setMinorTickCount(60);
		time.setSnapToTicks(true);
		time.setLabelFormatter(new TimeLabelConverter());
		// set up changing
		time.highValueProperty().addListener((ov, n, t1) -> updateTimePreview());
		time.lowValueProperty().addListener((ov, n, t1) -> updateTimePreview());
	}
	
	private LocalDateTime[] getStartEndPair(){
		LocalDate selectedDate = getDate().getValue();
		LocalTime startTime = toTime(getTime().getLowValue());
		LocalTime endTime = toTime(getTime().getHighValue());
		return new LocalDateTime[]{LocalDateTime.of(selectedDate, startTime), LocalDateTime.of(selectedDate, endTime)};
	}
	
	private LocalTime toTime(double value){
		return LocalTime.of((int) (value / 60), (int) (value % 60));
	}
	
	private void updateTimePreview(){
		LocalDateTime[] pair = getStartEndPair();
		String preview = String.format(
				"%s ~ %s",
				pair[0].format(previewFormatter),
				pair[1].format(previewFormatter)
		);
		getTimePreview().setText(preview);
	}
	
	private void setUpCapacity(){
		Slider capacity = getCapacity();
		capacity.setMin(0);
		capacity.setMax(1000);
		capacity.setMajorTickUnit(100);
		capacity.setMinorTickCount(4);
		capacity.setBlockIncrement(10);
	}
	
	private void setUpReward(){
		Slider reward = getReward();
		reward.setMin(0);
		reward.setMax(500);
		reward.setMajorTickUnit(50);
		reward.setMinorTickCount(2);
		reward.setBlockIncrement(1);
	}
	
	private void setUpRequirement(){
		Slider requirement = getRequirement();
		requirement.setMin(0);
		requirement.setMax(500);
		requirement.setMajorTickUnit(50);
		requirement.setMinorTickCount(2);
		requirement.setBlockIncrement(1);
	}
	
	private static class TimeLabelConverter extends StringConverter<Number>{
		
		@Override
		public String toString(Number d){
			int minutes = d.intValue();
			return String.format("%d:%d", minutes / 60, minutes % 60);
		}
		
		@Override
		public Number fromString(String s){
			return null;
		}
	}
	
	protected abstract TextField getTitle();
	
	protected abstract TextField getLocation();
	
	protected abstract DatePicker getDate();
	
	protected abstract RangeSlider getTime();
	
	protected abstract Label getTimePreview();
	
	protected abstract Slider getCapacity();
	
	protected abstract Slider getReward();
	
	protected abstract Slider getRequirement();
	
	protected abstract EventBuilder getBuilder();
}
