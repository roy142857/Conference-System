package usecase.commu.facade;

import usecase.commu.chat.manager.ChatManager;
import usecase.commu.MessagingManage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessagingView implements Serializable{
	
	private final MessagingManage mm;
	
	public MessagingView(MessagingManage mm){
		this.mm = mm;
	}
	
	public List<ChatManager> getChats(UUID person){
		return Stream.of(mm.getAllANCFor(person), mm.getAllDCFor(person))
		             .flatMap(c -> c)
		             .collect(Collectors.toList());
	}
}
