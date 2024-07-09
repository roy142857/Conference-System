package exception.general;

/**
 * General Not Supported Exception, threw when some action is not yet supported.
 */
public class NotSupportedException extends RuntimeException{
	
	public NotSupportedException(String s){
		super(s);
	}
}
