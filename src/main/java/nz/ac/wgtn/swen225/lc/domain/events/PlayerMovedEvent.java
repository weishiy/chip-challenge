package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

public record PlayerMovedEvent(Player player, Vector2D from, Vector2D to) implements GameEvent {
}
