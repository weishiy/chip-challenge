package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

public final class Wall extends Tile {

    public Wall(Vector2D position) {
        this(null, position);
    }

    public Wall(Level level, Vector2D position) {
        super(level, position);
    }

    @Override
    public boolean isEnterable(Player player) {
        return false;
    }

    @Override
    public void onEnter(Player player) {
        throw new IllegalStateException("Illegal movement!");

    }

    @Override
    public void onExit(Player player) {
        throw new IllegalStateException("Illegal movement!");
    }

}
