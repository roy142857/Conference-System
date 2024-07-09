package usecase.commu.chat.manager.access;

/**
 * The AccessLevel defines a cumulative style of permission, that is, the
 * higher level has can perform everything that the lower level can perform.
 *
 * Here we have 7 levels:
 * <ol>
 *     <li>Own : All Actions.</li>
 *     <li>Manage : Administrative Actions.</li>
 *     <li>Edit : Add Message.</li>
 *     <li>Sync : Receive New Message.</li>
 *     <li>View : View Message.</li>
 *     <li>Undefined</li>
 * </ol>
 *
 * For example:
 * If we know some person has Edit level of access, then they must necessarily has:
 * Sync, View Access.
 */
public enum AccessLevel{
	/**
	 * {@code OWN} level has access to everything.
	 * This is the highest Access Level.
	 * <p>
	 * Including: everything {@code MANAGE}, {@code EDIT}, {@code SYNC}, {@code VIEW} has.
	 * </p>
	 */
	OWN(10),
	/**
	 * {@code MANAGE} level has access that is up to administrative.
	 * <p>
	 * Including: enroll, deSync, set access level, and everything {@code EDIT}, {@code SYNC}, {@code VIEW} has.
	 * </p>
	 */
	MANAGE(8),
	/**
	 * {@code EDIT} level has access that is up to add new messages.
	 * <p>
	 * Including: add Message, and everything {@code SYNC}, {@code VIEW} has.
	 * </p>
	 */
	EDIT(6),
	/**
	 * {@code SYNC} level has access that is up to receive new messages.
	 * <p>
	 * Including: receive Message, and everything {@code VIEW} has.
	 * </p>
	 */
	SYNC(4),
	/**
	 * {@code VIEW} level has access that is up to view messages.
	 * This is the lowest Access Level.
	 * <p>
	 * Including: view Message.
	 * </p>
	 */
	VIEW(2),
	/**
	 * {@code UNDEFINED} level is a "null" object describing access of people that is not in this chat.
	 */
	UNDEFINED(- 1),
	;
	
	private final int access;
	
	AccessLevel(int access){
		this.access = access;
	}
	
	public int getLevel(){
		return access;
	}
}
