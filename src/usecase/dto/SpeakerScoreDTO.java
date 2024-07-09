package usecase.dto;

import java.util.UUID;

public class SpeakerScoreDTO{
	
	private UUID   id;
	private String name;
	private double score;
	
	public UUID getID(){
		return id;
	}
	
	public void setID(UUID id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setScore(double score){
		this.score = score;
	}
}
