package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ExitLock;

/**
 * Fires when the exit lock is unlocked
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record ExitLockUnlockedEvent(ExitLock exitLock, Player player) implements GameEvent {
}
