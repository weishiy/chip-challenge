package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.LockedDoor;

/**
 * Fires when a door is unlocked
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record DockUnlockedEvent(LockedDoor lockedDoor, Player player) implements GameEvent {
}
