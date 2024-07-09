package usecase.friends.manager;

public interface RelationshipObservable{
	
	/**
	 * Add a friend ship observer to observe friendship update.
	 *
	 * @param relationshipObserver an relationship observer.
	 */
	void addFriendshipObserver(RelationshipObserver relationshipObserver);
	
	/**
	 * Add a friend ship observer to observe work relation update.
	 *
	 * @param relationshipObserver an relationship observer.
	 */
	void addWorkRelationObserver(RelationshipObserver relationshipObserver);
}
