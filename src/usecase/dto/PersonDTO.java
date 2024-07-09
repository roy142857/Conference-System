package usecase.dto;

import java.util.UUID;

public class PersonDTO{
	
	private UUID   ID;
	private String type;
	private int    points;
	private String name;
	
	public UUID getID(){
		return ID;
	}
	
	public void setID(UUID ID){
		this.ID = ID;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int points){
		this.points = points;
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
	
	@Override
	public String toString(){
		return name;
	}
}
