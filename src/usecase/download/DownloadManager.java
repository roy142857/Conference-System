package usecase.download;

import usecase.download.facade.EventDownload;
import usecase.event.EventManage;
import usecase.people.PeopleManage;

import java.util.List;
import java.util.UUID;

/**
 * The program will produce a neatly schedule for the conference
 * that users have the option of "downloading". User will active this option by clicking a "download"
 * button. The output file will be as in pdf.
 *
 * The "download" button must appear by itself.
 *
 * People including Attendees, Speakers, and Organizers use {@link DownloadManager} to ensure
 * they are able to check the booking of time and location whenever necessary.
 *
 * OUTPUT of below program -
 *
 * PDF created in >> e:/Schedule.pdf
 *
 * @see usecase.schedule.Scheduler
 */

public class DownloadManager implements DownloadManage{
	
	private EventDownload eventDownload;
	
	public void initialize(EventManage em, PeopleManage pm){
		eventDownload = new EventDownload(em, pm);
	}
	
	public String generateAllEventScheduleHTML(){
		return eventDownload.generateAll();
	}
}
