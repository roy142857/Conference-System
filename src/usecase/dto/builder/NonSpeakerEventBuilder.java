package usecase.dto.builder;

import entity.event.Event;
import entity.event.NonSpeakerEvent;

public class NonSpeakerEventBuilder extends EventBuilder{
	
	@Override
	public Event build(){
		NonSpeakerEvent nse = new NonSpeakerEvent();
		build(nse);
		return nse;
	}
}
