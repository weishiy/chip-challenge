package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Fires when player reaches the exit
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record PlayerWonEvent(Player player) implements GameOverEvent {
}
