package usecase.people;

import entity.people.Person;
import presenter.people.PeopleViewPresent;
import presenter.people.VIPPromotePresent;
import presenter.center.dashboard.DashboardPresent;
import presenter.login.LoginPresent;
import presenter.center.PeopleCenterPresent;
import presenter.people.PeopleCreatePresent;
import usecase.dto.PersonDTO;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * PeopleManage manages a all {@code Person}.
 */
public interface PeopleManage extends Serializable{
	
	void debugCreate(PersonType type, String username, char[] password);
	
	void initialize(EventManage em, FriendsManage fm);
	
	void addPoints(UUID person, int point, VIPPromotePresent presenter);
	
	void deductPoints(UUID person, int point, VIPPromotePresent presenter);
	
	boolean matchPassword(String username, char[] password);
	
	void putPassword(String username, char[] password);
	
	Person get(UUID id);
	
	void put(Person person);
	
	void updatePerson(Person person);
	
	UUID getID(String username);
	
	void updateView(UUID people, PeopleViewPresent presenter);
	
	void updateCenterView(UUID people, PeopleCenterPresent presenter);
	
	void updateDashboardView(UUID person, DashboardPresent presenter);
	
	/**
	 * Create and store a new Person with type, and name, also with password,
	 *
	 * <p>
	 * Notice that the Person Entity only needs name. The password is stored inside people manage.
	 * And this method uses PersonFactory to create.
	 * </p>
	 *
	 * @param type the type
	 * @param username the username
	 * @param password the password
	 * @see entity.people.factory.PersonFactory
	 */
	UUID create(PersonType type, String username, char[] password, PeopleCreatePresent presenter);
	
	/**
	 * Get a Person based on it's username and password, this method is used when logging in.
	 *
	 * @param username the username provided
	 * @param password the password provided
	 * @return a Person if the username and password pair matches some Person, otherwise return null.
	 */
	UUID login(String username, char[] password, LoginPresent presenter);
	
	List<PersonDTO> convert(List<UUID> ids);
	
	/**
	 * Get all person that can speak.
	 *
	 * @return all speakers.
	 */
	List<PersonDTO> getAllSpeakerDTO();
	
	List<PersonDTO> getSpeakersOf(UUID event);
	
	PersonDTO getOrganizerOf(UUID event);
	
	/**
	 * Get the name of the person
	 *
	 * @param person the person
	 * @return a person
	 */
	String getName(UUID person);
	
	/**
	 * Check the person can attend or not.
	 *
	 * @param person the person.
	 * @return true iff the person can attend.
	 */
	boolean canAttend(UUID person);
	
	/**
	 * Check the person can speak or not.
	 *
	 * @param person the person.
	 * @return true iff the person can speak.
	 */
	boolean canSpeak(UUID person);
	
	/**
	 * Check the person can organize or not.
	 *
	 * @param person the person.
	 * @return true iff the person can organize.
	 */
	boolean canOrganize(UUID person);
	
	/**
	 * Get all people's ID as a list.
	 *
	 * @return all people's ID.
	 */
	List<UUID> getAllPeople();
	
	/**
	 * Get all person that can attend.
	 *
	 * @return all attendees.
	 */
	List<UUID> getAllAttendee();
	
	/**
	 * Get all person that can speak.
	 *
	 * @return all speakers.
	 */
	List<UUID> getAllSpeaker();
	
	/**
	 * Get all person that can organize.
	 *
	 * @return all organizer.
	 */
	List<UUID> getAllOrganizer();
	
	/**
	 * Check the name is already have or not, if the name in, return true, else
	 * return false.
	 *
	 * @param username the username
	 * @return bool
	 */
	boolean hasSameUsername(String username);
}
