package entity.people.factory;

import entity.people.*;
import usecase.people.PersonType;

public class PersonFactory{
	
	public Person create(PersonType type, String name){
		switch(type){
			case ORGANIZER:
				return new Organizer(name);
			case SPEAKER:
				return new Speaker(name);
			case ATTENDEE:
				return new Attendee(name);
			default:
				throw new IllegalArgumentException("Person of type " + type + " is not supported!");
		}
	}
	
	public Person convertToVIPAttendee(Person person){
		if(! person.canAttend())
			throw new IllegalArgumentException("Can't Convert Non Attendee!");
		VIPAttendee attendee = new VIPAttendee(person.getID(), person.getName());
		attendee.setPoints(person.getPoints());
		return attendee;
	}
	
	public Person convertToRegularAttendee(Person person){
		if(! person.canAttend())
			throw new IllegalArgumentException("Can't Convert Non Attendee!");
		Attendee attendee = new Attendee(person.getID(), person.getName());
		attendee.setPoints(person.getPoints());
		return attendee;
	}
}
