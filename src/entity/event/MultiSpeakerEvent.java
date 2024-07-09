package entity.event;

import entity.interfaces.OccupancyLevel;

import java.util.Set;
import java.util.UUID;

/**
 * MultiSpeakerEvent represents a multiple-speaker Talk happens at a Conference, with a {@code OCCUPANCY_SPECIFIC}
 * level of Occupancy to the Schedule.
 */
public class MultiSpeakerEvent extends Event {

    private Set<UUID> speaker;

    public MultiSpeakerEvent() {
        super();
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visitMultiSpeakerEvent(this);
    }

    @Override
    public OccupancyLevel getOccupancyLevel() {
        return OccupancyLevel.OCCUPANCY_SPECIFIC;
    }

    /**
     * Get the set of ID of the speakers of this Event.
     *
     * @return the speaker.
     */
    public Set<UUID> getSpeakers(){
        return speaker;
    }

    /**
     * Set the Speakers of the Event.
     */
    public void setSpeakers(Set<UUID> speaker){
        this.speaker = speaker;
    }
}
