package usecase.commu.chat.manager;

import usecase.commu.chat.manager.access.AccessLevel;
import usecase.friends.manager.RelationshipObserver;

import java.util.UUID;

/**
 * Direct Chat, for messaging between two people.
 */
public class DirectChat extends ChatManager{
	
	private boolean workConnected    = false;
	private boolean friendsConnected = false;
	
	private final RelationshipObserver friendsObserver;
	private final RelationshipObserver workObserver;
	
	public DirectChat(String name){
		super(name);
		friendsObserver = new DirectChatFriendsObserver();
		workObserver = new DirectChatWorkRelationObserver();
	}
	
	public RelationshipObserver getFriendsObserver(){
		return friendsObserver;
	}
	
	public RelationshipObserver getWorkObserver(){
		return workObserver;
	}
	
	public void initialize(UUID p1, UUID p2, boolean friendsConnected, boolean workConnected){
		// give the most basic access
		this.createFor(p1, AccessLevel.VIEW);
		this.createFor(p2, AccessLevel.VIEW);
		this.friendsConnected = friendsConnected;
		this.workConnected = workConnected;
		sync(p1, p2);
	}
	
	public boolean isDirectChatFor(UUID p1, UUID p2){
		return hasAccess(p1, AccessLevel.VIEW) &&
		       hasAccess(p2, AccessLevel.VIEW);
	}
	
	private void sync(UUID p1, UUID p2){
		if((! workConnected) && (! friendsConnected)){
			updateDisconnected(p1, p2);
		}else{
			updateConnected(p1, p2);
		}
	}
	
	@Override
	public String toString(){
		return "DirectChat{" +
		       "workConnected=" + workConnected +
		       ", friendsConnected=" + friendsConnected +
		       '}';
	}
	
	public void updateConnected(UUID p1, UUID p2){
		setAccess(p1, AccessLevel.OWN);
		setAccess(p2, AccessLevel.OWN);
	}
	
	public void updateDisconnected(UUID p1, UUID p2){
		setAccess(p1, AccessLevel.VIEW);
		setAccess(p2, AccessLevel.VIEW);
	}
	
	private class DirectChatFriendsObserver implements RelationshipObserver{
		
		@Override
		public boolean isObserving(UUID p1, UUID p2){
			return isDirectChatFor(p1, p2);
		}
		
		@Override
		public void updateConnected(UUID p1, UUID p2){
			friendsConnected = true;
			sync(p1, p2);
		}
		
		@Override
		public void updateDisconnected(UUID p1, UUID p2){
			friendsConnected = false;
			sync(p1, p2);
		}
	}
	
	private class DirectChatWorkRelationObserver implements RelationshipObserver{
		
		@Override
		public boolean isObserving(UUID p1, UUID p2){
			return isDirectChatFor(p1, p2);
		}
		
		@Override
		public void updateConnected(UUID p1, UUID p2){
			workConnected = true;
			sync(p1, p2);
		}
		
		@Override
		public void updateDisconnected(UUID p1, UUID p2){
			workConnected = false;
			sync(p1, p2);
		}
	}
}
