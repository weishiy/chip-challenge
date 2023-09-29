package nz.ac.wgtn.swen225.lc.domain.events;

/**
 * Fires at the end of every tick
 */
public record TickEvent(int tickNo) implements GameEvent {
}
