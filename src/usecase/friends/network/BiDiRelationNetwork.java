package usecase.friends.network;

import entity.relation.Relationship;
import usecase.friends.manager.RelationshipObserver;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Bi-Direction Graph implementation of Relationship Manage.
 * Which means, a relation is always double sided: p1 is connected to p2 implies p2 is connected to p1.
 */
public class BiDiRelationNetwork implements RelationNetwork{
	
	private final Map<UUID, Map<UUID, Relationship>> relationshipMap;
	private final List<RelationshipObserver>         observers;
	
	public BiDiRelationNetwork(){
		relationshipMap = new HashMap<>();
		observers = new ArrayList<>();
	}
	
	@Override
	public void connect(UUID p1, UUID p2){
		ensureExist(p1);
		ensureExist(p2);
		ifElseDo(p1, p2, Relationship::increase, () -> create(1));
		ifElseDo(p2, p1, Relationship::increase, () -> create(1));
		// update the observer
		notifyConnected(p1, p2);
	}
	
	@Override
	public void disconnect(UUID p1, UUID p2){
		ifElseDo(p1, p2, Relationship::decrease, () -> create(0));
		ifElseDo(p2, p1, Relationship::decrease, () -> create(0));
		// update the observer
		notifyDisconnected(p1, p2);
	}
	
	@Override
	public boolean isConnected(UUID p1, UUID p2){
		return getLayersOfConnection(p1, p2) > 0;
	}
	
	@Override
	public void applyConnectionOffset(UUID p1, UUID p2, int offset){
		getRelationship(p1, p2).applyOffset(offset);
		getRelationship(p2, p1).applyOffset(offset);
	}
	
	@Override
	public List<UUID> getConnectedTo(UUID p){
		return getValidConnections(p).map(Map.Entry::getKey).collect(Collectors.toList());
	}
	
//	@Override
	public void update(UUID p, Function<UUID, Integer> function){
		update(p, getConnectedTo(p), function);
	}
	
	@Override
	public void update(UUID p, Collection<UUID> ps, Function<UUID, Integer> function){
		ps.forEach(person -> {
			// record the connected state
			boolean connectedFlag = isConnected(p, person);
			int offset = function.apply(person);
			applyConnectionOffset(p, person, offset);
			// check the connected state, if state changed, notify the observer
			boolean currentConnectedFlag = isConnected(p, person);
			if(connectedFlag && ! currentConnectedFlag){
				notifyDisconnected(p, person);
			}else if(! connectedFlag && currentConnectedFlag){
				notifyConnected(p, person);
			}
		});
	}
	
	@Override
	public void addObserver(RelationshipObserver observer){
		observers.add(observer);
	}
	
	@Override
	public void notifyConnected(UUID p1, UUID p2){
		observers.stream().filter(observer -> observer.isObserving(p1, p2))
		         .forEach(observer -> observer.updateConnected(p1, p2));
	}
	
	@Override
	public void notifyDisconnected(UUID p1, UUID p2){
		observers.stream().filter(observer -> observer.isObserving(p1, p2))
		         .forEach(observer -> observer.updateDisconnected(p1, p2));
	}
	
	private Stream<Map.Entry<UUID, Relationship>> getValidConnections(UUID p){
		return relationshipMap.getOrDefault(p, Collections.emptyMap()).entrySet().stream().filter(
				entry -> entry.getValue().getLayer() != 0
		);
	}
	
	private void ifElseDo(UUID p1, UUID p2, Consumer<Relationship> ifLogic, Supplier<Relationship> elseLogic){
		if(relationshipMap.containsKey(p1)){
			Map<UUID, Relationship> p1Map = relationshipMap.get(p1);
			if(p1Map.containsKey(p2)){
				ifLogic.accept(p1Map.get(p2));
			}else{
				p1Map.put(p2, elseLogic.get());
			}
		}
	}
	
	private void ensureExist(UUID p){
		if(! relationshipMap.containsKey(p))
			relationshipMap.put(p, new HashMap<>());
	}
	
	private Relationship getRelationship(UUID p, UUID k){
		ensureExist(p);
		if(! relationshipMap.get(p).containsKey(k))
			relationshipMap.get(p).put(k, create(0));
		return relationshipMap.get(p).get(k);
	}
	
	private int getLayersOfConnection(UUID p1, UUID p2){
		return getRelationship(p1, p2).getLayer();
	}
	
	private Relationship create(int initValue){
		return new Relationship(initValue);
	}
	
}
