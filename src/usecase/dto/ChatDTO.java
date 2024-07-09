package usecase.dto;

import java.util.UUID;

public class ChatDTO{
	
	private UUID   ID;
	private String name;
	private String mostRecent;
	
	public UUID getID(){
		return ID;
	}
	
	public void setID(UUID ID){
		this.ID = ID;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getMostRecent(){
		return mostRecent;
	}
	
	public void setMostRecent(String mostRecent){
		this.mostRecent = mostRecent;
	}
}
