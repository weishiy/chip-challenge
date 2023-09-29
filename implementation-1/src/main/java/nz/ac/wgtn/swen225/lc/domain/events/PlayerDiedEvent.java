package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Fires when player is caught by an enemy
 */
public record PlayerDiedEvent(Player player) implements GameOverEvent {
}
