package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

/**
 * Fires when a key is consumed (to unlock a door)
 */
public record KeyConsumedEvent(Key key) implements GameEvent {
}
