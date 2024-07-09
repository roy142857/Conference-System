package usecase.commu.facade;

import presenter.chat.GroupMessagePresent;
import usecase.commu.MessagingManage;
import usecase.friends.manager.FriendsManage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class GroupMessaging implements Serializable{
	
	private MessagingManage mm;
	private FriendsManage   fm;
	
	public GroupMessaging(MessagingManage mm, FriendsManage fm){
		this.mm = mm;
		this.fm = fm;
	}
	
	public boolean groupMessage(String message, UUID p1, List<UUID> to, GroupMessagePresent presenter){
		presenter.respondStarting(to.size());
		int success = 0;
		for(UUID p2 : to){
			presenter.respondPersonStatus(
					p2, fm.isCoWorker(p1, p2) && mm.getChat(p1, p2).sendMessage(p1, message)
			);
			success++;
		}
		presenter.respondEnding(success, to.size());
		return success == to.size();
	}
}
