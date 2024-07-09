package entity.task;

/**
 * Visitor Design Pattern.
 * An Interface that specific a handling procedure for each kind of Task.
 */
public interface TaskVisitor{
	
	/**
	 * Visit {@code Appointment}.
	 *
	 * @param appointment a Appointment that is feed in.
	 */
	void visitAppointment(Appointment appointment);
	
	/**
	 * Visit {@code SpeakerDuty}.
	 *
	 * @param speakerDuty a SpeakerDuty that is feed in.
	 */
	void visitSpeakerDuty(SpeakerDuty speakerDuty);
}
