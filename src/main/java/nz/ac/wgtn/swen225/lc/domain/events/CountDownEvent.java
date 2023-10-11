package nz.ac.wgtn.swen225.lc.domain.events;

/**
 * Fires every second (PS: countdown decrease 1)
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record CountDownEvent(int countDown) implements GameEvent {
}
