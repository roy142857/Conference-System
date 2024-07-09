package controller.dialogs.eventEdit.detail;

import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import usecase.dto.PersonDTO;

public class SpeakerConverter extends StringConverter<PersonDTO>{
	
	private final ObservableList<PersonDTO> speakers;
	
	public SpeakerConverter(ObservableList<PersonDTO> speakers){
		this.speakers = speakers;
	}
	
	@Override
	public String toString(PersonDTO personDTO){
		return personDTO.getName();
	}
	
	@Override
	public PersonDTO fromString(String s){
		return speakers.stream().filter(personDTO -> personDTO.getName().equals(s)).findAny().orElse(null);
	}
}