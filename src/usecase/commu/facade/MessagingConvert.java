package usecase.commu.facade;

import entity.social.Message;
import entity.social.MessageVisitor;
import entity.social.StringMessage;
import usecase.commu.chat.manager.ChatManager;
import usecase.dto.ChatDTO;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessagingConvert implements MessageVisitor, Serializable{
	
	private final PeopleManage pm;
	private       String       lastResult;
	
	public MessagingConvert(PeopleManage pm){
		this.pm = pm;
	}
	
	public List<ChatDTO> convert(List<ChatManager> chatManagers, UUID owner){
		List<ChatDTO> chatDTOS = new ArrayList<>();
		for(ChatManager c : chatManagers){
			ChatDTO dto = new ChatDTO();
			dto.setName(c.getChatName());
			dto.setID(c.getID());
			Message mostRecent = c.getMostRecentMessage(owner);
			if(mostRecent != null){
				mostRecent.accept(this);
				dto.setMostRecent(lastResult);
			}else{
				dto.setMostRecent("No Message Yet!");
			}
			chatDTOS.add(dto);
		}
		return chatDTOS;
	}
	
	@Override
	public void visitStringMessage(StringMessage stringMessage){
		String currentMessage = stringMessage.getContent();
		String name = pm.getName(stringMessage.getSender());
		lastResult = String.format(
				"%s : \"%s\"", name, currentMessage
		);
	}
}
