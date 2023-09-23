package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;

public record InfoFieldReleasedEvent(InfoField infoField, Player player) implements GameEvent {
}
