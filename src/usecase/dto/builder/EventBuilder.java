package usecase.dto.builder;

import entity.event.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class EventBuilder{
	
	protected String        title;
	protected UUID          organizer;
	protected int           capacity;
	protected String        location;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;
	protected int           reward;
	protected int           requirement;
	
	public abstract Event build();
	
	protected void build(Event e){
		e.setTitle(title);
		e.setOrganizer(organizer);
		e.setCapacity(capacity);
		e.setLocation(location);
		e.setStartTime(startTime);
		e.setEndTime(endTime);
		e.setRequirement(requirement);
		e.setReward(reward);
	}
	
	public int getReward(){
		return reward;
	}
	
	public void setReward(int reward){
		this.reward = reward;
	}
	
	public int getRequirement(){
		return requirement;
	}
	
	public void setRequirement(int requirement){
		this.requirement = requirement;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public UUID getOrganizer(){
		return organizer;
	}
	
	public void setOrganizer(UUID organizer){
		this.organizer = organizer;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public void setCapacity(int capacity){
		this.capacity = capacity;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public LocalDateTime getStartTime(){
		return startTime;
	}
	
	public void setStartTime(LocalDateTime startTime){
		this.startTime = startTime;
	}
	
	public LocalDateTime getEndTime(){
		return endTime;
	}
	
	public void setEndTime(LocalDateTime endTime){
		this.endTime = endTime;
	}
}
