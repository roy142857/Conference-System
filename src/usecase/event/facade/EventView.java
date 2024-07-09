package usecase.event.facade;

import entity.event.*;
import presenter.center.EventCenterPresent;
import presenter.event.EventViewPresent;
import usecase.event.EventManage;
import usecase.event.EventSearchProperty;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventView implements Serializable{
	
	private final EventManage  em;
	private final PeopleManage pm;
	
	private final EventConvert eventConvert;
	
	public EventView(EventManage em, PeopleManage pm, EventConvert eventConvert){
		this.em = em;
		this.pm = pm;
		this.eventConvert = eventConvert;
	}
	
	public void updateView(EventSearchProperty searchProperty, String keyword, EventCenterPresent presenter){
		List<UUID> events = em.getAll().stream()
		                      .filter(filterPredicate(searchProperty, keyword))
		                      .collect(Collectors.toList());
		presenter.updateView(eventConvert.convert(events));
	}
	
	public void updateView(UUID event, EventViewPresent presenter){
		presenter.updateView(eventConvert.convert(event));
	}
	
	public boolean isManagedBy(UUID event, UUID person){
		Event e = em.get(event);
		SpeakerMatch speakerMatch = new SpeakerMatch(id -> id.equals(person));
		e.accept(speakerMatch);
		return e.getOrganizer().equals(person) || speakerMatch.isMatched();
	}
	
	private Predicate<UUID> filterPredicate(EventSearchProperty searchProperty, String keyword){
		return id -> {
			Event e = em.get(id);
			switch(searchProperty){
				default:
				case ALL:
					return matchTitle(e, keyword) || matchSpeaker(e, keyword) || matchLocation(e, keyword);
				case TITLE:
					return matchTitle(e, keyword);
				case SPEAKER:
					return matchSpeaker(e, keyword);
				case LOCATION:
					return matchLocation(e, keyword);
			}
		};
	}
	
	private boolean matchTitle(Event e, String keyword){
		return e.getTitle().contains(keyword);
	}
	
	private boolean matchSpeaker(Event e, String keyword){
		SpeakerMatch speakerMatch = new SpeakerMatch(id -> pm.getName(id).contains(keyword));
		e.accept(speakerMatch);
		return speakerMatch.isMatched();
	}
	
	private boolean matchLocation(Event e, String keyword){
		return e.getLocation().contains(keyword);
	}
	
	private static class SpeakerMatch implements EventVisitor{
		
		private final Predicate<UUID> predicate;
		private       boolean         isMatch;
		
		public SpeakerMatch(Predicate<UUID> predicate){
			this.predicate = predicate;
		}
		
		public boolean isMatched(){
			return isMatch;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() != null)
				isMatch = predicate.test(talk.getSpeaker());
			else
				isMatch = false;
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			isMatch = false;
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			isMatch = mse.getSpeakers().stream().anyMatch(predicate);
		}
	}
}
