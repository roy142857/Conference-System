package usecase.commu.chat.manager.facade;

import entity.social.Message;
import entity.social.MessageVisitor;
import entity.social.StringMessage;
import usecase.commu.chat.record.ChatRecord;
import usecase.dto.MessageDTO;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatRecordConvert implements MessageVisitor, Serializable{
	
	private List<MessageDTO> messageDTOS;
	
	private PeopleManage pm;
	
	public ChatRecordConvert(PeopleManage pm){
		this.pm = pm;
	}
	
	public List<MessageDTO> convert(ChatRecord chatRecord){
		messageDTOS = new ArrayList<>();
		for(Message message : chatRecord){
			message.accept(this);
		}
		return messageDTOS;
	}
	
	@Override
	public void visitStringMessage(StringMessage stringMessage){
		MessageDTO dto = new MessageDTO();
		dto.setContent(stringMessage.getContent());
		dto.setSender(pm.getName(stringMessage.getSender()));
		dto.setSenderID(stringMessage.getSender());
		dto.setTime(stringMessage.getTime());
		messageDTOS.add(dto);
	}
}
