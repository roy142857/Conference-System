package usecase.dto.builder;

import entity.event.Event;
import entity.event.Talk;

import java.util.UUID;

public class TalkBuilder extends EventBuilder{
	
	private UUID speaker;
	
	@Override
	public Event build(){
		Talk talk = new Talk();
		build(talk);
		talk.setSpeaker(speaker);
		return talk;
	}
	
	public void setSpeaker(UUID speaker){
		this.speaker = speaker;
	}
	
	public UUID getSpeaker(){
		return speaker;
	}
}
