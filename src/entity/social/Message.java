package entity.social;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Message object represents a single sentence inside a typical conversation.
 *
 * <p>
 * Note: This Object is designed with Immutability in mind, because message could
 * be stored in many places (many aliases). Also Message is comparable, it compares the time.
 * </p>
 */
public abstract class Message implements Comparable<Message>, Serializable{
	
	private final UUID          sender;
	private final LocalDateTime time;
	
	/**
	 * Construct a Message.
	 *
	 * @param sender the sender of this Message.
	 * @param time the time this Message is being sent.
	 */
	public Message(UUID sender, LocalDateTime time){
		this.sender = sender;
		this.time = time;
	}
	
	/**
	 * Visitor Design Patten,
	 * to traverse a list of Message and to properly handle each kind of Message. For
	 * example, if we want to print a list of Message. First define a
	 * {@code MessagePrintVisitor}:
	 *
	 * <pre>
	 * {@code
	 *     class MessagePrintVisitor implements MessageVisitor{
	 *         public void visitStringMessage(StringMessage message){
	 *             // handle a StringMessage
	 *             System.out.println(message.getContent());
	 *         }
	 *     }
	 * }
	 * </pre>
	 * and call {@code Message.accept} in a loop:
	 * <pre>
	 * {@code
	 *     MessageVisitor visitor = new MessagePrintVisitor();
	 *     for(Message message : listOfMessage){
	 *         message.accept(visitor);
	 *     }
	 * }
	 * </pre>
	 *
	 * @param visitor a {@code MessageVisitor}.
	 * @see MessageVisitor
	 */
	public abstract void accept(MessageVisitor visitor);
	
	/**
	 * Get the Content of this Message.
	 *
	 * @return the Content of this Message.
	 */
	public abstract Object getContent();
	
	/**
	 * Check if the current message is valid.
	 *
	 * @return true iff the message is acceptable.
	 */
	public abstract boolean isValidMessage();
	
	/**
	 * Get the sender of this message.
	 *
	 * @return a {@code Identifiable}.
	 */
	public UUID getSender(){
		return sender;
	}
	
	/**
	 * Get the time of this message being send.
	 *
	 * @return a time.
	 */
	public LocalDateTime getTime(){
		return time;
	}
	
	public int compareTo(Message o){
		return time.compareTo(o.getTime());
	}
}
