package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a wall tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Wall extends Tile {
    /**
     * Constructs a Wall object with the specified position.
     *
     * @param position The position of the tile.
     */
    public Wall(Vector2D position) {
        super(position);
    }
    /**
     * Constructs a Wall object with the specified ID and position.
     *
     * @param id The unique identifier for the tile.
     * @param position The position of the tile.
     */
    public Wall(int id, Vector2D position) {
        super(id, position);
    }

    /**
     * Checks whether the player is allowed to enter the tile.
     *
     * Since Wall tiles are impassable obstacles, players cannot enter them.
     *
     * @param player The player character attempting to enter the tile.
     * @return False, indicating that the tile is not enterable by the player.
     */
    @Override
    public boolean isEnterable(Player player) {
        return false;
    }
    /**
     * Actions to be taken when the player enters the tile.
     *
     * Entering a Wall tile is considered an illegal movement, and an exception is thrown.
     *
     * @param player The player character attempting to enter the tile.
     * @throws IllegalStateException if the player attempts to enter a wall tile.
     */
    @Override
    public void onEnter(Player player) {
        throw new IllegalStateException("Illegal movement!");
    }
    /**
     * Actions to be taken when the player exits the tile.
     *
     * Exiting a Wall tile is considered an illegal movement, and an exception is thrown.
     *
     * @param player The player character attempting to exit the tile.
     * @throws IllegalStateException if the player attempts to exit a wall tile.
     */
    @Override
    public void onExit(Player player) {
        throw new IllegalStateException("Illegal movement!");
    }

}
