package usecase.commu.chat.manager.facade;

import entity.social.MessageVisitor;
import entity.social.StringMessage;

import java.io.Serializable;

public class MessageValidator implements MessageVisitor, Serializable{
	
	private boolean lastResult = false;
	
	@Override
	public void visitStringMessage(StringMessage stringMessage){
		lastResult = ! stringMessage.getContent().equals("");
	}
	
	public boolean getLastResult(){
		return lastResult;
	}
}
