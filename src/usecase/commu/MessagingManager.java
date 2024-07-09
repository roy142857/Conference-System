package usecase.commu;

import exception.social.SocialException;
import presenter.center.MessageCenterPresent;
import presenter.chat.GroupMessagePresent;
import usecase.commu.chat.manager.Announcement;
import usecase.commu.chat.manager.ChatManager;
import usecase.commu.chat.manager.DirectChat;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.commu.facade.GroupMessaging;
import usecase.commu.facade.MessagingConvert;
import usecase.commu.facade.MessagingCreate;
import usecase.commu.facade.MessagingView;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;

import java.util.*;
import java.util.stream.Stream;

public class MessagingManager implements MessagingManage{
	
	private final Map<UUID, Announcement> announcements;
	private final Map<UUID, DirectChat>   directChats;
	
	private MessagingConvert messagingConvert;
	private MessagingCreate  messagingCreate;
	private MessagingView    messagingView;
	private GroupMessaging   groupMessaging;
	
	public MessagingManager(){
		this.announcements = new LinkedHashMap<>();
		this.directChats = new LinkedHashMap<>();
	}
	
	@Override
	public void initialize(PeopleManage pm, FriendsManage fm, EventManage em){
		messagingConvert = new MessagingConvert(pm);
		messagingCreate = new MessagingCreate(this, pm, fm, em);
		groupMessaging = new GroupMessaging(this, fm);
		messagingView = new MessagingView(this);
	}
	
	@Override
	public void updateView(UUID person, MessageCenterPresent presenter){
		presenter.updateView(messagingConvert.convert(messagingView.getChats(person), person));
	}
	
	@Override
	public boolean groupMessage(String message, UUID p1, List<UUID> to, GroupMessagePresent presenter){
		return groupMessaging.groupMessage(message, p1, to, presenter);
	}
	
	@Override
	public void createAnnouncement(UUID event){
		messagingCreate.createAnnouncement(event);
	}
	
	@Override
	public void removeAnnouncement(UUID event){
		messagingCreate.removeAnnouncement(event);
	}
	
	@Override
	public ChatManager getChat(UUID chatID){
		ChatManager result = directChats.get(chatID);
		if(result == null) result = announcements.get(chatID);
		return result;
	}
	
	@Override
	public ChatManager getChat(UUID p1, UUID p2){
		return messagingCreate.getChat(p1, p2);
	}
	
	@Override
	public void enrollAnnouncementFor(UUID event, UUID person, AccessLevel level){
		getANC(event).orElseThrow(() -> new SocialException("Announcement is not created!"))
		             .enrollFor(person, level);
	}
	
	@Override
	public void unEnrollAnnouncementFor(UUID event, UUID person){
		getANC(event).orElseThrow(() -> new SocialException("Announcement is not created!"))
		             .unEnrollFor(person);
	}
	
	@Override
	public void enrollAnnouncementFor(UUID event, Collection<UUID> person, AccessLevel level){
		Announcement anc = getANC(event).orElseThrow(() -> new SocialException("Announcement is not created!"));
		for(UUID p : person){
			anc.enrollFor(p, level);
		}
	}
	
	@Override
	public void unEnrollAnnouncementFor(UUID event, Collection<UUID> person){
		Announcement anc = getANC(event).orElseThrow(() -> new SocialException("Announcement is not created!"));
		for(UUID p : person){
			anc.unEnrollFor(p);
		}
	}
	
	@Override
	public void addANC(Announcement anc){
		announcements.put(anc.getID(), anc);
	}
	
	@Override
	public void removeANC(UUID event){
		announcements.entrySet().removeIf(entry -> entry.getValue().isAnnouncementFor(event));
	}
	
	@Override
	public void addDC(DirectChat dc){
		directChats.put(dc.getID(), dc);
	}
	
	@Override
	public Optional<Announcement> getANC(UUID event){
		return announcements.values().stream()
		                    .filter(announcement -> announcement.isAnnouncementFor(event))
		                    .findAny();
	}
	
	@Override
	public Optional<DirectChat> getDC(UUID p1, UUID p2){
		return directChats.values().stream()
		                  .filter(directChat -> directChat.isDirectChatFor(p1, p2))
		                  .findAny();
	}
	
	@Override
	public Stream<Announcement> getAllANCFor(UUID person){
		return announcements.values().stream().filter(anc -> anc.hasAccess(person, AccessLevel.VIEW));
	}
	
	@Override
	public Stream<DirectChat> getAllDCFor(UUID person){
		return directChats.values().stream().filter(dc -> dc.hasAccess(person, AccessLevel.VIEW));
	}
}
