package usecase.dto;

import java.util.UUID;

public class TaskDTO{
	
	private UUID   ID;
	private UUID   taskFor;
	private String type;
	private String owner;
	private String title;
	
	public UUID getTaskFor(){
		return taskFor;
	}
	
	public void setTaskFor(UUID taskFor){
		this.taskFor = taskFor;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public UUID getID(){
		return ID;
	}
	
	public void setID(UUID ID){
		this.ID = ID;
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
}
