package entity.social;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Message that contains a String.
 */
public class StringMessage extends Message{
	
	private final String content;
	
	/**
	 * Construct a StringMessage.
	 *
	 * @param sender the sender of this Message.
	 * @param time the time this Message is being sent.
	 * @param content the content of this Message.
	 */
	public StringMessage(UUID sender, LocalDateTime time, String content){
		super(sender, time);
		this.content = content;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Accept a StringMessage.
	 *
	 * @param visitor a {@code MessageVisitor}.
	 */
	@Override
	public void accept(MessageVisitor visitor){
		visitor.visitStringMessage(this);
	}
	
	/**
	 * Get the content of this StringMessage.
	 *
	 * @return a String.
	 */
	public String getContent(){
		return content;
	}
	
	@Override
	public boolean isValidMessage(){
		return ! content.equals("");
	}
}
