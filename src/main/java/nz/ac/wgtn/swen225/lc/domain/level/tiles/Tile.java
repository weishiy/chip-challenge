package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

public abstract class Tile extends Entity {

    private Level level;
    private final Vector2D position;

    public Tile(Vector2D position) {
        super();
        this.position = position;
    }

    public boolean isEnterable(Player player) {
        throw new UnsupportedOperationException();
    }

    public void onEnter(Player player) {
        throw new UnsupportedOperationException();
    }

    public void onExit(Player player) {
        throw new UnsupportedOperationException();
    }

    public Game getGame() {
        return level.getGame();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Vector2D getPosition() {
        return position;
    }

}
