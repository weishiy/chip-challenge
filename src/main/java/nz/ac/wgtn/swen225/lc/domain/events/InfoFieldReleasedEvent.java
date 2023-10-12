package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;

/**
 * Fires when player exited the info field
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record InfoFieldReleasedEvent(InfoField infoField, Player player) implements GameEvent {
}
