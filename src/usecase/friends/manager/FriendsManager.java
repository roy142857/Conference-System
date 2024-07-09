package usecase.friends.manager;

import exception.social.SocialException;
import presenter.people.AddFriendsPresent;
import usecase.friends.network.BiDiRelationNetwork;
import usecase.friends.network.RelationNetwork;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class FriendsManager implements FriendsManage{
	
	private final RelationNetwork friendship;
	private final RelationNetwork workRelation;
	
	public FriendsManager(){
		friendship = new BiDiRelationNetwork();
		workRelation = new BiDiRelationNetwork();
	}
	
	@Override
	public List<UUID> getFriends(UUID person){
		return friendship.getConnectedTo(person);
	}
	
	@Override
	public List<UUID> getCoWorker(UUID person){
		return workRelation.getConnectedTo(person);
	}
	
	@Override
	public boolean isCoWorker(UUID p1, UUID p2){
		return workRelation.isConnected(p1, p2);
	}
	
	@Override
	public boolean isFriend(UUID p1, UUID p2){
		return friendship.isConnected(p1, p2);
	}
	
	@Override
	public boolean hasRelation(UUID p1, UUID p2){
		return isFriend(p1, p2) || isCoWorker(p1, p2);
	}
	
	@Override
	public boolean addFriend(UUID p1, UUID p2, AddFriendsPresent presenter){
		if(friendship.isConnected(p1, p2)){
			presenter.respondAlreadyFriends();
		}else{
			checkSamePerson(p1, p2);
			friendship.connect(p1, p2);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeFriend(UUID p1, UUID p2, AddFriendsPresent presenter){
		if(! friendship.isConnected(p1, p2)){
			presenter.respondNotYetFriends();
		}else{
			checkSamePerson(p1, p2);
			friendship.disconnect(p1, p2);
			return true;
		}
		return false;
	}
	
	@Override
	public void addWorkRelation(UUID p1, UUID p2){
		workRelation.connect(p1, p2);
	}
	
	@Override
	public void removeWorkRelation(UUID p1, UUID p2){
		workRelation.disconnect(p1, p2);
	}
	
	@Override
	public void updateWorkRelation(UUID p, Collection<UUID> ps, Function<UUID, Integer> function){
		workRelation.update(p, ps, function);
	}
	
	@Override
	public void addFriendshipObserver(RelationshipObserver relationshipObserver){
		friendship.addObserver(relationshipObserver);
	}
	
	@Override
	public void addWorkRelationObserver(RelationshipObserver relationshipObserver){
		workRelation.addObserver(relationshipObserver);
	}
	
	private void checkSamePerson(UUID p1, UUID p2) throws SocialException{
		if(p1.equals(p2))
			throw new SocialException("They are the same people!");
	}
}
