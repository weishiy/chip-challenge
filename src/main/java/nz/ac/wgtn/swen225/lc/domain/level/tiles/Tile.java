package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

public abstract class Tile extends Entity {

    private final Vector2D position;
    private Level level;

    public Tile(Vector2D position) {
        super();
        this.position = position;
    }

    public abstract boolean isEnterable(Player player);

    public abstract void onEnter(Player player);

    public abstract void onExit(Player player);

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Game getGame() {
        return level.getGame();
    }

    public Vector2D getPosition() {
        return position;
    }

}
