package usecase.people;

public enum PersonType{
	ORGANIZER("Organizer"),
	SPEAKER("Speaker"),
	ATTENDEE("Attendee");
	
	private final String rep;
	
	PersonType(String rep){
		this.rep = rep;
	}
	
	@Override
	public String toString(){
		return rep;
	}
}
