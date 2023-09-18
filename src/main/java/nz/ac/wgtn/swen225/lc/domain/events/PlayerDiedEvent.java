package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

public record PlayerDiedEvent(Player player) implements GameOverEvent {
}
