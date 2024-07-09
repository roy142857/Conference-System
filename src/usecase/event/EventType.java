package usecase.event;

import java.util.function.Consumer;

public enum EventType{
	TALK("Talk", visitor -> visitor.isTalk(null)),
	NON_SPEAKER_EVENT("Non Speaker Event", visitor -> visitor.isNonSpeakerEvent(null)),
	MULTI_SPEAKER_EVENT("Multi Speaker Event", visitor -> visitor.isMultiSpeakerEvent(null));
	
	private final String                     rep;
	private final Consumer<EventTypeVisitor> accept;
	
	EventType(String rep, Consumer<EventTypeVisitor> accept){
		this.rep = rep;
		this.accept = accept;
	}
	
	public void accept(EventTypeVisitor visitor){
		accept.accept(visitor);
	}
	
	@Override
	public String toString(){
		return rep;
	}
}
