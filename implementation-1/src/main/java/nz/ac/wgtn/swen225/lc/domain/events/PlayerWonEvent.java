package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Fires when player reaches the exit
 */
public record PlayerWonEvent(Player player) implements GameOverEvent {
}
