package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class Wall extends Tile {

    public Wall(Vector2D position) {
        super(position);
    }

    @Override
    public boolean isEnterable(Player player) {
        return false;
    }

    @Override
    public void onEnter(Player player) {
        throw new IllegalStateException(player + " shouldn't be seating on " + this);
    }

    @Override
    public void onExit(Player player) {
        throw new IllegalStateException(player + " shouldn't be seating on " + this);
    }

}
