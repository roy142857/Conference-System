package usecase.commu;

import presenter.center.MessageCenterPresent;
import presenter.chat.GroupMessagePresent;
import usecase.commu.chat.manager.Announcement;
import usecase.commu.chat.manager.ChatManager;
import usecase.commu.chat.manager.DirectChat;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * ChatManage manages all chats, including
 * DirectChat (two people directly talk to each other),
 * Announcement (a group of people can view, and only some can add message).
 */
public interface MessagingManage extends Serializable{
	
	void initialize(PeopleManage pm, FriendsManage fm, EventManage em);
	
	void updateView(UUID person, MessageCenterPresent presenter);
	
	/**
	 * Get the chat for p1 and p2.
	 * If the chat doesn't exist, create it.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 * @return the chat between p1 and p2.
	 */
	ChatManager getChat(UUID p1, UUID p2);
	
	/**
	 * Get the chat for p1 and p2.
	 * If the chat doesn't exist, create it.
	 *
	 * @param chatID the ID of the chat.
	 * @return the chat between p1 and p2.
	 */
	ChatManager getChat(UUID chatID);
	
	/**
	 * message all people together
	 */
	boolean groupMessage(String message, UUID p1, List<UUID> to, GroupMessagePresent presenter);
	
	/**
	 * Create a announcement for that event.
	 * If the announcement already exist, throws an {@link exception.social.SocialException}.
	 *
	 * @param event the event.
	 * @throws exception.social.SocialException if the announcement already exist.
	 */
	void createAnnouncement(UUID event);
	
	void removeAnnouncement(UUID event);
	
	/**
	 * enroll a person to an announcement.
	 *
	 * @param event the event.
	 * @param person a person.
	 * @param level the initial level.
	 */
	void enrollAnnouncementFor(UUID event, UUID person, AccessLevel level);
	
	/**
	 * remove the person from an announcement.
	 *
	 * @param event the event.
	 * @param person the person.
	 */
	void unEnrollAnnouncementFor(UUID event, UUID person);
	
	/**
	 * enroll a person to an announcement.
	 *
	 * @param event the event.
	 * @param person a person.
	 * @param level the initial level.
	 */
	void enrollAnnouncementFor(UUID event, Collection<UUID> person, AccessLevel level);
	
	/**
	 * remove the person from an announcement.
	 *
	 * @param event the event.
	 * @param person the person.
	 */
	void unEnrollAnnouncementFor(UUID event, Collection<UUID> person);
	
	void addANC(Announcement anc);
	
	void removeANC(UUID event);
	
	void addDC(DirectChat dc);
	
	Optional<Announcement> getANC(UUID event);
	
	Optional<DirectChat> getDC(UUID p1, UUID p2);
	
	Stream<Announcement> getAllANCFor(UUID person);
	
	Stream<DirectChat> getAllDCFor(UUID person);
	
}
