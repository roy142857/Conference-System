package usecase.dto;

import java.util.UUID;

public class EventRecommendDTO{
	
	private UUID   ID;
	private String type;
	private String title;
	private double estimation;
	
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
	
	public double getEstimation(){
		return estimation;
	}
	
	public void setEstimation(double estimation){
		this.estimation = estimation;
	}
}
