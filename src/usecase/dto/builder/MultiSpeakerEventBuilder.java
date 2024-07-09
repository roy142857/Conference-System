package usecase.dto.builder;

import entity.event.Event;
import entity.event.MultiSpeakerEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MultiSpeakerEventBuilder extends EventBuilder{
	
	private Set<UUID> speakers = new HashSet<>();
	
	@Override
	public Event build(){
		MultiSpeakerEvent mse = new MultiSpeakerEvent();
		build(mse);
		mse.setSpeakers(speakers);
		return mse;
	}
	
	public Set<UUID> getSpeakers(){
		return speakers;
	}
	
	public void setSpeakers(Set<UUID> speakers){
		this.speakers = speakers;
	}
}
