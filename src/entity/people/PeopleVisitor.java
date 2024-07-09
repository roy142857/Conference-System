package entity.people;

public interface PeopleVisitor{
	
	void visitOrganizer(Organizer organizer);
	
	void visitSpeaker(Speaker speaker);
	
	void visitAttendee(Attendee attendee);
	
	void visitVIPAttendee(VIPAttendee vipAttendee);
}
