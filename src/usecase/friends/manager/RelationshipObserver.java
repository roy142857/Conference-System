package usecase.friends.manager;

import java.util.UUID;

public interface RelationshipObserver{
	
	boolean isObserving(UUID p1, UUID p2);
	
	void updateConnected(UUID p1, UUID p2);
	
	void updateDisconnected(UUID p1, UUID p2);
}
