package nz.ac.wgtn.swen225.lc.domain.events;

/**
 * Fires at the end of every tick
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record TickEvent(int tickNo) implements GameEvent {
}
