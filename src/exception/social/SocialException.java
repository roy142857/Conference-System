package exception.social;

/**
 * An General Social Exception, threw when anything went wrong inside the social module.
 */
public class SocialException extends RuntimeException{
	
	public SocialException(String s){
		super(s);
	}
}
