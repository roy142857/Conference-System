package exception.social;

/**
 * This is an Error (meaning it's probably not recoverable) throws when adding trying to add
 * a message to a Chat but the new Message has a time before the last message.
 */
public class MessageTimeFlowError extends Error{
	
	public MessageTimeFlowError(String message){
		super(message);
	}
}
