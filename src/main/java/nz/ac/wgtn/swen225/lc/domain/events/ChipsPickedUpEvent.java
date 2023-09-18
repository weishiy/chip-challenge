package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;

public record ChipsPickedUpEvent(ChipTile chipTile, Player player) implements GameEvent {
}
