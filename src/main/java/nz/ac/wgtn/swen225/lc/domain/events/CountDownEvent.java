package nz.ac.wgtn.swen225.lc.domain.events;

/**
 * Fires every second (PS: countdown decrease 1)
 */
public record CountDownEvent(int countDown) implements GameEvent {
}
