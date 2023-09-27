package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

public record KeyConsumedEvent(Key key) implements GameEvent {
}
