package exception.schedule;

/**
 * A UnChecked Exception, throws when an exception occurs inside Schedule.
 */
public class ScheduleException extends RuntimeException{
	
	/**
	 * Construct ScheduleException with a String message.
	 *
	 * @param s a message for why this exception occurs.
	 */
	public ScheduleException(String s){
		super(s);
	}
}
