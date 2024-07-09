package usecase.event.facade;

import entity.event.EventVisitor;
import entity.event.MultiSpeakerEvent;
import entity.event.NonSpeakerEvent;
import entity.event.Talk;
import usecase.event.EventManage;
import usecase.event.EventType;
import usecase.event.EventTypeVisitor;

import java.io.Serializable;
import java.util.UUID;

public class EventTypeDistributor implements Serializable{
	
	private final EventManage em;
	
	public EventTypeDistributor(EventManage em){
		this.em = em;
	}
	
	public void distribute(UUID event, EventTypeVisitor visitor){
		em.get(event).accept(new EventTypeExecutor(visitor));
	}
	
	public void distribute(EventType type, EventTypeVisitor visitor){
		type.accept(visitor);
	}
	
	private static class EventTypeExecutor implements EventVisitor{
		
		private final EventTypeVisitor eventTypeVisitor;
		
		private EventTypeExecutor(EventTypeVisitor eventTypeVisitor){
			this.eventTypeVisitor = eventTypeVisitor;
		}
		
		@Override
		public void visitTalk(Talk talk){
			eventTypeVisitor.isTalk(talk.getID());
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			eventTypeVisitor.isNonSpeakerEvent(nse.getID());
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			eventTypeVisitor.isMultiSpeakerEvent(mse.getID());
		}
	}
}
