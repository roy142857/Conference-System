package usecase.event;

public enum EventSearchProperty{
	ALL("All"),
	TITLE("Title"),
	SPEAKER("Speaker"),
	LOCATION("Location");
	
	private final String rep;
	
	EventSearchProperty(String rep){
		this.rep = rep;
	}
	
	@Override
	public String toString(){
		return rep;
	}
}
