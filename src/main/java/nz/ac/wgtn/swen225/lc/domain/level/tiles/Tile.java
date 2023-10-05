package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Parent class for all tiles
 */
public abstract class Tile extends Entity {

    private final Vector2D position;
    private Level level;

    public Tile(Vector2D position) {
        super();
        this.position = position;
    }

    public Tile(int id, Vector2D position) {
        super(id);
        this.position = position;
    }

    /**
     * Used to check if player is allowed to enter the tile
     * <p>
     * @param player player
     * @return true if player is allowed to enter. false if not.
     */
    public abstract boolean isEnterable(Player player);

    /**
     * Fired when player enters the tile
     * @param player player
     */
    public abstract void onEnter(Player player);

    /**
     * Fired when player exits the tile
     * @param player player
     */
    public abstract void onExit(Player player);

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Game getGame() {
        return getLevel().getGame();
    }

    public Vector2D getPosition() {
        return position;
    }

}
