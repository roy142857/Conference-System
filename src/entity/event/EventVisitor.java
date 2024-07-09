package entity.event;

/**
 * Visitor Design Pattern.
 * An Interface that specific a handling procedure for each kind of Event.
 */
public interface EventVisitor{
	
	/**
	 * Visit {@code Talk}.
	 *
	 * @param talk a Talk that is feed in.
	 */
	void visitTalk(Talk talk);
	
	/**
	 * Visit {@code NonSpeakerEvent}.
	 *
	 * @param nse a NonSpeakerEvent that is feed in.
	 */
	void visitNonSpeakerEvent(NonSpeakerEvent nse);
	
	/**
	 * Visit {@code MultiSpeakerEvent}.
	 *
	 * @param mse a MultiSpeakerEvent that is feed in.
	 */
	void visitMultiSpeakerEvent(MultiSpeakerEvent mse);
}
