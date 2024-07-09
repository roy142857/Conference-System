package exception.general;

/**
 * Threw when we received an illegal input.
 */
public class IllegalInputException extends Exception{
	
	public IllegalInputException(String input, String s){
		this("The input " + input + " is not a valid input, because : " + s);
	}
	
	public IllegalInputException(String s){
		super(s);
	}
}
