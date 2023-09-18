package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile;

public record KeyPickedUpEvent(KeyTile keyTile, Player player) implements GameEvent {
}
