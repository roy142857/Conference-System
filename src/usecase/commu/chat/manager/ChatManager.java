package usecase.commu.chat.manager;

import entity.social.Message;
import entity.social.factory.MessageFactory;
import exception.social.AccessException;
import exception.social.SocialException;
import presenter.chat.ChatPresent;
import usecase.commu.chat.manager.access.AccessLevel;
import usecase.commu.chat.manager.access.AccessManage;
import usecase.commu.chat.manager.access.AccessManager;
import usecase.commu.chat.manager.facade.ChatRecordConvert;
import usecase.commu.chat.manager.facade.MessageValidator;
import usecase.commu.chat.record.ChatRecord;
import usecase.commu.chat.record.ChatRecorder;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Base class for Chat.
 * Any chat should supports Manage and Messaging.
 */
public abstract class ChatManager implements Serializable{
	
	private final Map<UUID, ChatRecord> syncList;
	private final String                name;
	private final AccessManage          access;
	private final UUID                  ID;
	
	// Facade
	private MessageValidator  messageValidator;
	private ChatRecordConvert chatRecordConvert;
	
	public ChatManager(String name){
		this.ID = UUID.randomUUID();
		this.name = name;
		// LinkedHashMap provide half the proportion time for iterating over it.
		this.syncList = new LinkedHashMap<>();
		this.access = new AccessManager();
	}
	
	public void initialize(PeopleManage pm){
		this.chatRecordConvert = new ChatRecordConvert(pm);
		this.messageValidator = new MessageValidator();
	}
	
	public void updateView(UUID sender, ChatPresent presenter){
		presenter.updateView(chatRecordConvert.convert(getRecordFor(sender)), sender);
	}
	
	/**
	 * Send a message.
	 * If the sender don't have {@link AccessLevel#EDIT}, throws {@link AccessException}.
	 *
	 * <p>
	 * Before using this method, check the sender's access, and the chat's active status.
	 * </p>
	 *
	 * @param content the content
	 * @throws exception.social.MessageTimeFlowError if the time of the new message is prior to the previous message.
	 */
	public boolean sendMessage(UUID sender, Object content, ChatPresent presenter){
		// check the access and content valid
		Message message = getFactory().create(sender, content);
		message.accept(messageValidator);
		if(! messageValidator.getLastResult()){
			presenter.respondMessageEmpty();
		}else if(! hasAccess(sender, AccessLevel.EDIT)){
			presenter.respondNoAccess(AccessLevel.EDIT.toString());
		}else{
			notifyAll(message);
			return true;
		}
		return false;
	}
	
	public Message getMostRecentMessage(UUID sender){
		return getRecordFor(sender).getMostRecent();
	}
	
	/**
	 * Send a message.
	 * If the sender don't have {@link AccessLevel#EDIT}, throws {@link AccessException}.
	 *
	 * <p>
	 * Before using this method, check the sender's access, and the chat's active status.
	 * </p>
	 *
	 * @param content the content
	 * @return true if the message is send successful.
	 * @throws exception.social.MessageTimeFlowError if the time of the new message is prior to the previous message.
	 */
	public boolean sendMessage(UUID sender, Object content){
		Message message = getFactory().create(sender, content);
		if(! message.isValidMessage() || ! hasAccess(sender, AccessLevel.EDIT)){
			return false;
		}else{
			notifyAll(message);
			return true;
		}
	}
	
	/**
	 * Get a the name of the chat.
	 */
	public String getChatName(){
		return name;
	}
	
	/**
	 * Check the person has at least the access level specified.
	 *
	 * @param person the person.
	 * @param level the level required.
	 * @return true if people has at least the level of access.
	 * @see AccessManage#hasAccess(UUID, AccessLevel)
	 */
	public boolean hasAccess(UUID person, AccessLevel level){
		return access.hasAccess(person, level);
	}
	
	public UUID getID(){
		return ID;
	}
	
	/**
	 * Set the access level for the person, only when the caster has {@link AccessLevel#MANAGE}.
	 * If the person doesn't have a record yet, do nothing.
	 *
	 * @param person the person that is effected.
	 * @param level the level of access that it has been given.
	 * @see AccessManage#setAccess(UUID, AccessLevel)
	 */
	protected void setAccess(UUID person, AccessLevel level){
		if(! syncList.containsKey(person))
			throw new SocialException("Person doesn't own a copy!");
		access.setAccess(person, level);
	}
	
	/**
	 * Create a new record of chat for the person.
	 *
	 * @param person the person.
	 * @param initialAccess the initial access.
	 */
	protected void createFor(UUID person, AccessLevel initialAccess){
		if(syncList.containsKey(person))
			throw new SocialException(person + " already has a record of the chat!");
		ChatRecord record = new ChatRecorder();
		syncList.put(person, record);
		access.setAccess(person, initialAccess);
	}
	
	/**
	 * Remove the record of person, and set the access to {@link AccessLevel#UNDEFINED}
	 *
	 * @param person the person.
	 */
	protected void removeFor(UUID person){
		if(! syncList.containsKey(person))
			throw new SocialException(person + " doesn't have a record of the chat!");
		syncList.remove(person);
		access.setAccess(person, AccessLevel.UNDEFINED);
	}
	
	/**
	 * Return the chat record for the corresponding person.
	 */
	private ChatRecord getRecordFor(UUID person){
		return syncList.get(person);
	}
	
	/**
	 * Update all other chat with the updater.
	 */
	private void notifyAll(Message message){
		// dispatch the change
		syncList.entrySet().stream()
		        .filter(entry -> access.hasAccess(entry.getKey(), AccessLevel.SYNC))
		        .forEach(entry -> entry.getValue().update(message));
	}
	
	/**
	 * Return the message factory.
	 */
	private MessageFactory getFactory(){
		return new MessageFactory();
	}
}
