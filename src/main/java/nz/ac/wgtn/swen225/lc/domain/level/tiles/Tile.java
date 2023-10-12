package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Parent class for all tiles
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public abstract class Tile extends Entity {

    private final Vector2D position;
    private Level level;
    /**
     * Constructs a Tile object with the specified position.
     *
     * @param position The position of the tile.
     */
    public Tile(Vector2D position) {
        super();
        this.position = position;
    }
    /**
     * Constructs a Tile object with the specified ID and position.
     *
     * @param id The unique identifier for the tile.
     * @param position The position of the tile.
     */
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
    /**
     * Get the level to which the tile belongs.
     *
     * @return The level to which the tile belongs.
     */
    public Level getLevel() {
        return level;
    }
    /**
     * Set the level to which the tile belongs.
     *
     * @param level The level to which the tile belongs.
     */
    public void setLevel(Level level) {
        this.level = level;
    }
    /**
     * Get the game associated with the tile.
     *
     * @return The game associated with the tile.
     */
    public Game getGame() {
        return getLevel().getGame();
    }
    /**
     * Get the position of the tile.
     *
     * @return The position of the tile.
     */
    public Vector2D getPosition() {
        return position;
    }

}
