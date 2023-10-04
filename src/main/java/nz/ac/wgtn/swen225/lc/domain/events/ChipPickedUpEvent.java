package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;

/**
 * Fires when a chip is picked up by a player
 */
public record ChipPickedUpEvent(ChipTile chipTile, Player player) implements GameEvent {
}
