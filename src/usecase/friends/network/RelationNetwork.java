package usecase.friends.network;

import usecase.friends.manager.RelationshipObserver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * RelationshipManagement manages the relationship between two UUID (which can represent any two objects)
 * usually between people and people, but it's not the limitation of this interface, it really
 * just defines the connection between any two UUID.
 *
 * <p>
 * Class Invariant:
 * <ul>
 *     <li>{@code getLayersOfConnection(p, k)} always returns a integer between 0 and
 *     {@code Integer.MAX_VALUE}.</li>
 * </ul>
 * </p>
 */
public interface RelationNetwork extends Serializable{
	
	/**
	 * Add one layer of connection between p1 and p2.
	 *
	 * <p>
	 * Note : if {@code connect(p1, p2)} is called twice, then {@code getLayerOfConnection(p1, p2)}
	 * should returns at least 2.
	 * </p>
	 *
	 * @param p an UUID.
	 * @param k an UUID.
	 */
	void connect(UUID p, UUID k);
	
	/**
	 * Remove one layer of connection between p1 and p2.
	 *
	 * <p>
	 * Notice that the layers of connection is non-negative.
	 * </p>
	 *
	 * @param p an UUID.
	 * @param k an UUID.
	 */
	void disconnect(UUID p, UUID k);
	
	/**
	 * Check if p1 and p2 are connected.
	 *
	 * @param p an UUID.
	 * @param k an UUID.
	 * @return true iff p1 and p2 are connected.
	 */
	boolean isConnected(UUID p, UUID k);
	
	/**
	 * Apply a offset in the layers of connection, without violating the class invariant.
	 * i.e. if the offset would make the layers of connection becomes negative or over Integer.MAX_VALUE,
	 * it will be adjusted to the nearest bound.
	 *
	 * @param p an UUID
	 * @param k an UUID
	 * @param offset an integer describes the offset.
	 */
	void applyConnectionOffset(UUID p, UUID k, int offset);
	
	/**
	 * Get a list of P that is connected to p.
	 *
	 * @param p the p to check with.
	 * @return a list of P that is connected to p.
	 */
	List<UUID> getConnectedTo(UUID p);
	
	/**
	 * Update the network by calling the function on every people that is specified by ps, returns an
	 * integer as offset and is applied to the network using {@link #applyConnectionOffset(UUID, UUID, int)}.
	 *
	 * @param p the p to check with.
	 * @param ps the people to apply the function with (the domain of the function)
	 * @param function the function describes the change.
	 */
	void update(UUID p, Collection<UUID> ps, Function<UUID, Integer> function);
	
	void addObserver(RelationshipObserver observer);
	
	void notifyConnected(UUID p1, UUID p2);
	
	void notifyDisconnected(UUID p1, UUID p2);
}
