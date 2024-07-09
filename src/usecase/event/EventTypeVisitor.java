package usecase.event;

import java.util.UUID;

public interface EventTypeVisitor{
	
	void isTalk(UUID talk);
	
	void isNonSpeakerEvent(UUID nse);
	
	void isMultiSpeakerEvent(UUID mse);
}
