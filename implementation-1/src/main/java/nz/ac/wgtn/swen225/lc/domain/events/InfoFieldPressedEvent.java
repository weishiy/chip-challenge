package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;

/**
 * Fires when player entered a info field
 */
public record InfoFieldPressedEvent(InfoField infoField, Player player) implements GameEvent {
}
