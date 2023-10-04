package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Parent class for Enemy and Player
 */
public abstract class Character extends Entity {

    private Level level;
    private Vector2D position;

    public Character(Vector2D position) {
        this(null, position);
    }

    public Character(Level level, Vector2D position) {
        super();
        this.level = level;
        this.position = position;
    }

    public Level getLevel() {
        return level;
    }

    public Game getGame() {
        return level.getGame();
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
