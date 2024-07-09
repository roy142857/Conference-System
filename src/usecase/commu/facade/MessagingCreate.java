package usecase.commu.facade;

import exception.social.SocialException;
import usecase.commu.chat.manager.Announcement;
import usecase.commu.chat.manager.ChatManager;
import usecase.commu.chat.manager.DirectChat;
import usecase.commu.MessagingManage;
import usecase.commu.stragergy.AnnouncementNameStrategy;
import usecase.commu.stragergy.DefaultANCNameStrategy;
import usecase.commu.stragergy.DefaultDCNameStrategy;
import usecase.commu.stragergy.DirectChatNameStrategy;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.UUID;

public class MessagingCreate implements Serializable{
	
	private final PeopleManage             pm;
	private final FriendsManage            fm;
	private final MessagingManage          mm;
	private final AnnouncementNameStrategy ancNameStrategy;
	private final DirectChatNameStrategy   dcNameStrategy;
	
	public MessagingCreate(MessagingManage mm, PeopleManage pm, FriendsManage fm, EventManage em){
		this.mm = mm;
		this.pm = pm;
		this.fm = fm;
		ancNameStrategy = new DefaultANCNameStrategy(em);
		dcNameStrategy = new DefaultDCNameStrategy(pm);
	}
	
	public ChatManager getChat(UUID p1, UUID p2){
		return mm.getDC(p1, p2).orElseGet(() -> createDirectChatFor(p1, p2));
	}
	
	public void createAnnouncement(UUID event){
		if(mm.getANC(event).isPresent())
			throw new SocialException("Announcement already been created for " + event + "!");
		String chatName = ancNameStrategy.generateName(event);
		Announcement anc = new Announcement(event, chatName);
		anc.initialize(pm);
		mm.addANC(anc);
	}
	
	public void removeAnnouncement(UUID event){
		if(! mm.getANC(event).isPresent())
			throw new SocialException("Announcement hasn't been created for " + event + "!");
		mm.removeANC(event);
	}
	
	private DirectChat createDirectChatFor(UUID p1, UUID p2){
		if(p1.equals(p2))
			throw new IllegalArgumentException("They are the same people!");
		String chatName = dcNameStrategy.generateName(p1, p2);
		DirectChat dc = new DirectChat(chatName);
		dc.initialize(p1, p2, fm.isFriend(p1, p2), fm.isCoWorker(p1, p2));
		dc.initialize(pm);
		fm.addFriendshipObserver(dc.getFriendsObserver());
		fm.addWorkRelationObserver(dc.getWorkObserver());
		mm.addDC(dc);
		return dc;
	}
}
