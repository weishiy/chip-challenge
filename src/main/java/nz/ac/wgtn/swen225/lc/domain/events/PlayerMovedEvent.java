package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Fires when position of the player changed
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record PlayerMovedEvent(Player player, Vector2D from, Vector2D to) implements GameEvent {
}
