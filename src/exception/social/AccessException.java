package exception.social;

/**
 * Threw when someone is trying to perform actions that his privilege can't reach.
 */
public class AccessException extends RuntimeException{
	
	public AccessException(String s){
		super(s);
	}
}
