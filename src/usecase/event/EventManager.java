package usecase.event;

import entity.event.Event;
import presenter.center.EventCenterPresent;
import presenter.event.EventEditPresent;
import presenter.event.EventSignupPresent;
import presenter.event.EventViewPresent;
import usecase.commu.MessagingManage;
import usecase.dto.builder.EventBuilder;
import usecase.event.facade.*;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.schedule.Schedule;
import usecase.schedule.Scheduler;
import usecase.score.ScoreManage;
import usecase.task.TaskManage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventManager implements EventManage{
	
	private final Schedule<Event> events;
	
	private EventChange          eventChange;
	private EventCreate          eventCreate;
	private EventSignup          eventSignup;
	private EventView            eventView;
	private EventTypeDistributor typeDistributor;
	
	public EventManager(){
		this.events = new Scheduler<>();
	}
	
	@Override
	public void initialize(PeopleManage pm, TaskManage tm, MessagingManage mm, FriendsManage fm, ScoreManage sm){
		EventConvert eventConvert = new EventConvert(this, pm, sm);
		eventChange = new EventChange(pm, this, tm, mm, fm);
		eventCreate = new EventCreate(this, tm, mm, fm, sm, eventConvert);
		eventSignup = new EventSignup(pm, this, tm, mm, fm);
		eventView = new EventView(this, pm, eventConvert);
		typeDistributor = new EventTypeDistributor(this);
	}
	
	@Override
	public void acceptTypeVisitor(UUID event, EventTypeVisitor visitor){
		typeDistributor.distribute(event, visitor);
	}
	
	@Override
	public void acceptTypeVisitor(EventType type, EventTypeVisitor visitor){
		typeDistributor.distribute(type, visitor);
	}
	
	@Override
	public boolean createEvent(EventBuilder event, EventEditPresent presenter){
		return eventCreate.createEvent(event, presenter);
	}
	
	@Override
	public boolean removeEvent(UUID event, EventEditPresent presenter){
		return eventCreate.removeEvent(event, presenter);
	}
	
	@Override
	public EventBuilder getEventBuilder(UUID e){
		return eventCreate.getEventBuilder(e);
	}
	
	@Override
	public EventBuilder getEventBuilder(EventType type){
		return eventCreate.getEventBuilder(type);
	}
	
	@Override
	public boolean changeEvent(UUID originalEvent, EventBuilder current, EventEditPresent presenter){
		return eventChange.changeEvent(originalEvent, current, presenter);
	}
	
	@Override
	public void updateView(EventSearchProperty searchProperty, String keyword, EventCenterPresent presenter){
		eventView.updateView(searchProperty, keyword, presenter);
	}
	
	@Override
	public void updateView(UUID event, EventViewPresent presenter){
		eventView.updateView(event, presenter);
	}
	
	@Override
	public boolean isManagedBy(UUID event, UUID person){
		return eventView.isManagedBy(event, person);
	}
	
	@Override
	public boolean signUp(UUID event, UUID person, EventSignupPresent presenter){
		return eventSignup.signUp(event, person, presenter);
	}
	
	@Override
	public boolean cancelSignUp(UUID event, UUID person, EventSignupPresent presenter){
		return eventSignup.cancelSignUp(event, person, presenter);
	}
	
	@Override
	public boolean isStarted(Event event){
		return event.getStartTime().isBefore(LocalDateTime.now());
	}
	
	@Override
	public boolean isFinished(Event event){
		return event.getEndTime().isBefore(LocalDateTime.now());
	}
	
	@Override
	public boolean isFinished(UUID event){
		return isFinished(get(event));
	}
	
	@Override
	public Set<UUID> getSignedUp(UUID event){
		return get(event).getSignedUp();
	}
	
	@Override
	public boolean isSignedUp(UUID person, UUID event){
		return get(event).isSignedUp(person);
	}
	
	@Override
	public String getTitle(UUID event){
		return get(event).getTitle();
	}
	
	@Override
	public Event get(UUID id){
		return events.get(id);
	}
	
	@Override
	public void add(Event event){
		events.add(event);
	}
	
	@Override
	public void remove(UUID event){
		events.remove(event);
	}
	
	@Override
	public List<UUID> getConflicting(Event event){
		return events.getConflicting(event);
	}
	
	@Override
	public List<UUID> getAll(){
		return events.getAll();
	}
}
