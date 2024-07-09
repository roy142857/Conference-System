package usecase.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EventDTO{
	
	private UUID          ID;
	private String        type;
	private String        title;
	private List<String>  signedUp;
	private String        speaker;
	private String        organizer;
	private int           capacity;
	private int           reward;
	private int           requirement;
	private double        score;
	private String        location;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public double getScore(){
		return score;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public UUID getID(){
		return ID;
	}
	
	public void setID(UUID ID){
		this.ID = ID;
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
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public List<String> getSignedUp(){
		return signedUp;
	}
	
	public void setSignedUp(List<String> signedUp){
		this.signedUp = signedUp;
	}
	
	public String getSpeaker(){
		return speaker;
	}
	
	public void setSpeaker(String speaker){
		this.speaker = speaker;
	}
	
	public String getOrganizer(){
		return organizer;
	}
	
	public void setOrganizer(String organizer){
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
