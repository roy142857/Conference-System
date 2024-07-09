package entity.social.factory;

import entity.social.Message;
import entity.social.StringMessage;
import exception.general.NotSupportedException;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageFactory{
	
	public Message create(UUID sender, Object content){
		if(content instanceof String){
			return new StringMessage(sender, currentTime(), (String) content);
		}else{
			throw new NotSupportedException("Object of type " +
			                                content.getClass().getName() +
			                                " is not Supported!");
		}
	}
	
	private LocalDateTime currentTime(){
		return LocalDateTime.now();
	}
}
