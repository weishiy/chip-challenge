package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile;

/**
 * Fires when a key is picked up by a player
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record KeyPickedUpEvent(KeyTile keyTile, Player player) implements GameEvent {
}
