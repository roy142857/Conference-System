package usecase.commu.chat.record;

import entity.social.Message;
import exception.social.MessageTimeFlowError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@inheritDoc}
 * </br>
 * An implementation of ChatManage as a flow of message using ArrayList to represent flow of messages.
 *
 * <p>
 * Note: Notice that we are adding always adding message to the end of the list,
 * and for MessageIterator, we are getting the message based on it's index,
 * so we use a array list, which provides O(1) on addMessage and RandomAccess.
 * </p>
 */
public class ChatRecorder implements ChatRecord{
	
	private final List<Message> messageList;
	
	public ChatRecorder(){
		super();
		this.messageList = new ArrayList<>();
	}
	
	@Override
	public void update(Message message){
		addMessage(message);
	}
	
	@Override
	public Message getMostRecent(){
		return messageList.size() == 0 ? null : getLast();
	}
	
	@Override
	public Iterator<Message> iterator(){
		return messageList.iterator();
	}
	
	private void addMessage(Message message){
		if(messageList.size() > 0 && message.compareTo(getLast()) < 0)
			throw new MessageTimeFlowError("The new Message Added has time " +
			                               message.getTime() +
			                               ", but the latest message recorded had time " +
			                               messageList.get(0).getTime());
		messageList.add(messageList.size(), message);
	}
	
	private Message getLast(){
		return messageList.get(messageList.size() - 1);
	}
}
