package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Fires when player is caught by an enemy
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record PlayerDiedEvent(Player player) implements GameOverEvent {
}
