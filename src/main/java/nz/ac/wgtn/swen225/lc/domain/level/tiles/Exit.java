package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a exit tile
 */
public final class Exit extends Tile {
    public Exit(Vector2D position) {
        super(position);
    }

    public Exit(int id, Vector2D position) {
        super(id, position);
    }

    @Override
    public boolean isEnterable(Player player) {
        return true;
    }

    @Override
    public void onEnter(Player player) {
        getGame().fire(new PlayerWonEvent(player));
    }

    @Override
    public void onExit(Player player) {
        throw new IllegalStateException("Illegal movement!");
    }
}
