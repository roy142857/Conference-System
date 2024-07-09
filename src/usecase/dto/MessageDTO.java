package usecase.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageDTO{
	
	private String        content;
	private String        sender;
	private UUID          senderID;
	private LocalDateTime time;
	
	public UUID getSenderID(){
		return senderID;
	}
	
	public void setSenderID(UUID senderID){
		this.senderID = senderID;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getSender(){
		return sender;
	}
	
	public void setSender(String sender){
		this.sender = sender;
	}
	
	public LocalDateTime getTime(){
		return time;
	}
	
	public void setTime(LocalDateTime time){
		this.time = time;
	}
}
