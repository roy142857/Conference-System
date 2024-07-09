package usecase.commu.chat.manager.access;

import java.io.Serializable;
import java.util.UUID;

/**
 * AccessManage manages access (or permission) that is cumulative.
 *
 * <p>
 * A typical application is to manage the level of access someone has to some system,
 * since privilege is often cumulative. Meaning someone with a higher level of privilege
 * can do everything those with a lower level can.
 * </p>
 */
public interface AccessManage extends Serializable{
	
	/**
	 * Get a access level for a person.
	 *
	 * @param person the person
	 * @return an integer
	 */
	AccessLevel getAccess(UUID person);
	
	/**
	 * Check does a person have high enough level of access.
	 *
	 * @param person the person
	 * @param accessLevel the accessLevel to compare to
	 * @return true if the person's access level is greater or equals to accessLevel.
	 */
	boolean hasAccess(UUID person, AccessLevel accessLevel);
	
	/**
	 * Set the level of access for a person.
	 *
	 * @param person the person
	 * @param accessLevel the new level of access.
	 */
	void setAccess(UUID person, AccessLevel accessLevel);
}
