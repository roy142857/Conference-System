package entity.social;

/**
 * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">Visitor Design Pattern</a>
 * An Interface that specific a handling procedure for each kind of Message.
 */
public interface MessageVisitor{
	
	/**
	 * Visit {@code StringMessage}.
	 *
	 * @param stringMessage a StringMessage that is feed in.
	 */
	void visitStringMessage(StringMessage stringMessage);
}
