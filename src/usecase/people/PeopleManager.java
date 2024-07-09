package usecase.people;

import entity.people.Person;
import exception.people.ShouldCreatePersonException;
import presenter.people.PeopleViewPresent;
import presenter.people.VIPPromotePresent;
import presenter.center.dashboard.DashboardPresent;
import presenter.login.LoginPresent;
import presenter.center.PeopleCenterPresent;
import presenter.people.PeopleCreatePresent;
import usecase.dto.PersonDTO;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.facade.*;

import java.util.*;
import java.util.stream.Collectors;

public class PeopleManager implements PeopleManage{
	
	private final Map<UUID, Person>   peopleBase;
	private final Map<String, char[]> passwordBase;
	private final Map<String, UUID>   nameBase;
	
	private PeopleConvert peopleConvert;
	private PeopleCreate  peopleCreate;
	private PeopleView    peopleView;
	private PeopleLogin   peopleLogin;
	private PeopleFilter  peopleFilter;
	private VIPPromote    vipPromote;
	
	public PeopleManager(){
		peopleBase = new HashMap<>();
		passwordBase = new HashMap<>();
		nameBase = new HashMap<>();
	}
	
	@Override
	public void debugCreate(PersonType type, String username, char[] password){
		// TODO DEBUG, TO BE REMOVED
		peopleCreate.debugCreate(type, username, password);
	}
	
	@Override
	public void initialize(EventManage em, FriendsManage fm){
		peopleConvert = new PeopleConvert(this);
		peopleCreate = new PeopleCreate(this, fm);
		peopleView = new PeopleView(peopleConvert, fm);
		peopleLogin = new PeopleLogin(this);
		peopleFilter = new PeopleFilter(this, peopleConvert, em);
		vipPromote = new VIPPromote(this);
	}
	
	@Override
	public void addPoints(UUID person, int point, VIPPromotePresent presenter){
		Person p = get(person);
		setPoint(p, p.getPoints() + point, presenter);
	}
	
	@Override
	public void deductPoints(UUID person, int point, VIPPromotePresent presenter){
		Person p = get(person);
		setPoint(p, p.getPoints() - point, presenter);
	}
	
	private void setPoint(Person person, int point, VIPPromotePresent presenter){
		int prevPoint = person.getPoints();
		person.setPoints(point);
		vipPromote.onPointChanged(person.getID(), prevPoint, presenter);
	}
	
	@Override
	public void updateView(UUID person, PeopleViewPresent presenter){
		peopleView.updateView(person, presenter);
	}
	
	@Override
	public void updateDashboardView(UUID person, DashboardPresent presenter){
		peopleView.updateDashboardView(person, presenter);
	}
	
	@Override
	public void updateCenterView(UUID people, PeopleCenterPresent presenter){
		peopleView.updateCenterView(people, presenter);
	}
	
	@Override
	public boolean matchPassword(String username, char[] password){
		return Arrays.equals(passwordBase.get(username), password);
	}
	
	@Override
	public void putPassword(String username, char[] password){
		passwordBase.put(username, password);
	}
	
	@Override
	public Person get(UUID id){
		return peopleBase.get(id);
	}
	
	@Override
	public void put(Person person){
		peopleBase.put(person.getID(), person);
		nameBase.put(person.getName(), person.getID());
	}
	
	public void updatePerson(Person person){
		peopleBase.put(person.getID(), person);
	}
	
	@Override
	public UUID getID(String username){
		return nameBase.get(username);
	}
	
	/**
	 * Create a person by type, name and  password, it might raise ItHasSameNameAlready
	 * if name is already existed.
	 *
	 * @param type the type
	 * @param username the username
	 * @param password the password
	 */
	@Override
	public UUID create(PersonType type, String username, char[] password, PeopleCreatePresent presenter){
		return peopleCreate.create(type, username, password, presenter);
	}
	
	/**
	 * Login will return null if username is not exist or the password
	 * cannot match, otherwise return the person relative to the username.
	 *
	 * @param username the username provided
	 * @param password the password provided
	 * @return person or null
	 */
	@Override
	public UUID login(String username, char[] password, LoginPresent presenter){
		return peopleLogin.login(username, password, presenter);
	}
	
	@Override
	public List<PersonDTO> convert(List<UUID> ids){
		return peopleConvert.convert(ids);
	}
	
	@Override
	public List<PersonDTO> getAllSpeakerDTO(){
		return peopleFilter.getAllSpeakerDTO();
	}
	
	@Override
	public List<PersonDTO> getSpeakersOf(UUID event){
		return peopleFilter.getSpeakersOf(event);
	}
	
	@Override
	public PersonDTO getOrganizerOf(UUID event){
		return peopleFilter.getOrganizerOf(event);
	}
	
	/**
	 * Return whether the name is already exist in the base.
	 *
	 * @param username the username
	 * @return true if the name is existed
	 */
	@Override
	public boolean hasSameUsername(String username){
		return nameBase.containsKey(username);
	}
	
	/**
	 * Get the name according to given person.
	 *
	 * @param person the person
	 * @return name of the person
	 */
	@Override
	public String getName(UUID person){
		if(person == null)
			return "";
		return peopleBase.get(person).getName();
	}
	
	/**
	 * Return this person can  attend or not.
	 *
	 * @param person the person.
	 * @return true if this person can attend
	 */
	@Override
	public boolean canAttend(UUID person){
		return peopleBase.get(person).canAttend();
	}
	
	/**
	 * Return this person can be speak or not.
	 *
	 * @param person the person.
	 * @return true if this person can speak
	 */
	@Override
	public boolean canSpeak(UUID person){
		return peopleBase.get(person).canSpeak();
	}
	
	/**
	 * Return this person can be organize or not.
	 *
	 * @param person the person.
	 * @return true if this person can organize
	 */
	@Override
	public boolean canOrganize(UUID person){
		return peopleBase.get(person).canOrganize();
	}
	
	/**
	 * Get all people's id and return them in a list.
	 *
	 * @return all people's id in a list
	 */
	@Override
	public List<UUID> getAllPeople(){
		return new ArrayList<>(peopleBase.keySet());
	}
	
	@Override
	public List<UUID> getAllAttendee(){
		return peopleBase.keySet().stream().filter(this::canAttend).collect(Collectors.toList());
	}
	
	@Override
	public List<UUID> getAllSpeaker(){
		return peopleBase.keySet().stream().filter(this::canSpeak).collect(Collectors.toList());
	}
	
	@Override
	public List<UUID> getAllOrganizer(){
		return peopleBase.keySet().stream().filter(this::canOrganize).collect(Collectors.toList());
	}
	
	/**
	 * Set the new name of the given person.
	 *
	 * @param person the person.
	 * @param newName the name.
	 * @throws ShouldCreatePersonException: throw ShouldCreatePersonException if the person is not
	 * * create
	 */
	public void setName(UUID person, String newName) throws ShouldCreatePersonException{
		if(! peopleBase.containsKey(person)){
			throw new ShouldCreatePersonException("You should create this Person at first.");
		}else{
			Person p = peopleBase.get(person);
			p.setName(newName);
		}
	}
	
	/**
	 * Set the password of an account.
	 *
	 * @param person the person
	 * @param newPassword the new password
	 * @throws ShouldCreatePersonException: throw ShouldCreatePersonException if the person is not
	 * create
	 */
	public void setPassword(UUID person, char[] newPassword) throws ShouldCreatePersonException{
		if(! peopleBase.containsKey(person)){
			throw new ShouldCreatePersonException("You should create this Person at first.");
		}else{
			Person p = peopleBase.get(person);
			passwordBase.replace(p.getName(), newPassword);
		}
	}
}
