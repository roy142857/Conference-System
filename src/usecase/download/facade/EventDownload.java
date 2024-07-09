package usecase.download.facade;

import entity.event.*;
import usecase.event.EventManage;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventDownload implements EventVisitor, Serializable{
	
	private final EventManage  eventManage;
	private final PeopleManage peopleManage;
	
	private StringBuilder builder;
	
	public EventDownload(EventManage eventManage, PeopleManage peopleManage){
		this.eventManage = eventManage;
		this.peopleManage = peopleManage;
	}
	
	public String generateAll(){
		return generateHTML(eventManage.getAll());
	}
	
	public String generateHTML(List<UUID> Scheduler){
		// FIXME
		//  Exception in thread "JavaFX Application Thread" com.itextpdf.tool.xml.exceptions.
		//  RuntimeWorkerException: Invalid nested tag table found, expected closing tag tr.
		
		//Build html using StringBuilder to be converted to pdf.
		
		builder = new StringBuilder();
		// append some javascript for auto scrolling
		builder.append("<!DOCTYPE html>");
		builder.append("<html>");
		builder.append("<body>");
		builder.append("<table border=\"0.5\">");
		builder.append("<tr>");
		builder.append("<th>Title</th>");
		builder.append("<th>Start</th>");
		builder.append("<th>End</th>");
		builder.append("<th>Place</th>");
		builder.append("<th>Capacity</th>");
		builder.append("<th>SignedUp</th>");
		builder.append("<th>Organizer</th>");
		builder.append("<th>Speaker</th>");
		builder.append("</tr>");
		
		//append the body

		for(UUID event : Scheduler){
			Event e = eventManage.get(event);
			e.accept(this);
		}

		// append the ending html tag
		builder.append("</table>");
		builder.append("</body>");
		builder.append("</html>");
		
		return builder.toString();
	}
	
	/**
	 * Visit {@code NonSpeakerEvent}.
	 *
	 * @param talk a Talk that is feed in.
	 */
	
	@Override
	public void visitTalk(Talk talk){
		// event basic information
		
		// event title
		String title = talk.getTitle();
		LocalDateTime eventStartTime = talk.getStartTime();
		LocalDateTime eventEndTime = talk.getEndTime();
		
		// event location
		
		String eventLocation = talk.getLocation();
		
		// event capacity and signed up count
		
		int eventCapacity = talk.getCapacity();
		int eventSignedUpCount = talk.getSignedUpCount();
		
		// event organizer and speaker
		
		UUID eventOrganizer = talk.getOrganizer();
		UUID eventSpeaker = talk.getSpeaker();

		builder.append("<tr>");
		builder.append("<td>");
		builder.append(title);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventStartTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventEndTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventLocation);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventCapacity);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventSignedUpCount);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(peopleManage.getName(eventOrganizer));
		builder.append("</td>");
		builder.append("<td>");
		builder.append(peopleManage.getName(eventSpeaker));
		builder.append("</td>");
		builder.append("</tr>");

	}
	
	/**
	 * Visit {@code NonSpeakerEvent}.
	 *
	 * @param nse a NonSpeakerEvent that is feed in.
	 */
	@Override
	public void visitNonSpeakerEvent(NonSpeakerEvent nse){
		// event basic information
		
		// event title
		String title = nse.getTitle();
		LocalDateTime eventStartTime = nse.getStartTime();
		LocalDateTime eventEndTime = nse.getEndTime();
		
		// event location
		
		String eventLocation = nse.getLocation();
		
		// event capacity and signed up count
		
		int eventCapacity = nse.getCapacity();
		int eventSignedUpCount = nse.getSignedUpCount();
		
		// event organizer
		
		UUID eventOrganizer = nse.getOrganizer();

		builder.append("<tr>");
		builder.append("<td>");
		builder.append(title);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventStartTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventEndTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventLocation);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventCapacity);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventSignedUpCount);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(peopleManage.getName(eventOrganizer));
		builder.append("</td>");
		builder.append("<td>");
		builder.append("None");
		builder.append("</td>");
		builder.append("</tr>");


	}
	
	/**
	 * Visit {@code MultiSpeakerEvent}.
	 *
	 * @param mse a MultiSpeakerEvent that is feed in.
	 */
	@Override
	public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
		// event basic information
		
		// event title
		String title = mse.getTitle();
		LocalDateTime eventStartTime = mse.getStartTime();
		LocalDateTime eventEndTime = mse.getEndTime();
		
		// event location
		
		String eventLocation = mse.getLocation();
		
		// event capacity and signed up count
		
		int eventCapacity = mse.getCapacity();
		int eventSignedUpCount = mse.getSignedUpCount();
		
		// event organizer
		
		UUID eventOrganizer = mse.getOrganizer();
		
		// list of speakers
		Set<UUID> eventSpeakers = mse.getSpeakers();
		List<String> names = new ArrayList<>();
		for(UUID speaker : eventSpeakers){
			names.add(peopleManage.getName(speaker));
		}

		builder.append("<tr>");
		builder.append("<td>");
		builder.append(title);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventStartTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventEndTime);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventLocation);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventCapacity);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(eventSignedUpCount);
		builder.append("</td>");
		builder.append("<td>");
		builder.append(peopleManage.getName(eventOrganizer));
		builder.append("</td>");
		builder.append("<td>");
		builder.append(names);
		builder.append("</td>");
		builder.append("</tr>");



	}
}
