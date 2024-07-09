package usecase.dto;

import java.util.UUID;

public class PersonRecommendDTO{
	
	private UUID   ID;
	private String type;
	private String name;
	private double similarity;
	
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
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public double getSimilarity(){
		return similarity;
	}
	
	public void setSimilarity(double similarity){
		this.similarity = similarity;
	}
}
