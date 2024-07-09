package entity.event;

import entity.interfaces.OccupancyLevel;


/**
 * NonSpeakerEvent represents a non-speaker Talk happens at a Conference, with a {@code OCCUPANCY_SPECIFIC}
 * level of Occupancy to the Schedule.
 */
public class NonSpeakerEvent extends Event {

    public NonSpeakerEvent() {
        super();
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visitNonSpeakerEvent(this);
    }

    @Override
    public OccupancyLevel getOccupancyLevel() {
        return OccupancyLevel.OCCUPANCY_SPECIFIC;
    }
}
