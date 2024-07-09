package entity.interfaces;

/**
 * The flags for possible Occupancy Level for an {@code IEvent}
 *
 * <p>
 * Note: Enum are inherently Serializable.
 * </p>
 */
public enum OccupancyLevel{
	/**
	 * The {@code IEvent} with No level of Occupancy doesn't occupy
	 * the time period and location on the {@code Schedule}.
	 * Other {@code IEvent} are free to occupy this period and location again.
	 */
	OCCUPANCY_NO,
	/**
	 * The {@code IEvent} with Specific level of Occupancy only occupy
	 * the period at this location specifically on the {@code Schedule}.
	 * Other {@code IEvent} can't occupy this location at this time period,
	 * but can occupy other location at this time period.
	 */
	OCCUPANCY_SPECIFIC,
	/**
	 * The {@code IEvent} with Absolute level of Occupancy occupy
	 * the time period at all the locations simultaneously on the {@code Schedule}.
	 * Other {@code IEvent} can't occupy any location at this time period.
	 */
	OCCUPANCY_ABSOLUTE
}
