package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

/**
 * Fires when a key is consumed (to unlock a door)
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record KeyConsumedEvent(Key key) implements GameEvent {
}
