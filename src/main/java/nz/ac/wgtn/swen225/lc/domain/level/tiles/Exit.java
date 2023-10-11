package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a exit tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Exit extends Tile {
    /**
     * Constructs an Exit tile object with the specified position.
     *
     * @param position The position of the exit tile.
     */
    public Exit(Vector2D position) {
        super(position);
    }
    /**
     * Constructs an Exit tile object with the specified ID and position.
     *
     * @param id The unique identifier for the exit tile.
     * @param position The position of the exit tile.
     */
    public Exit(int id, Vector2D position) {
        super(id, position);
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * @param player The player character attempting to enter the tile.
     * @return True, indicating that the tile is enterable by the player.
     */
    @Override
    public boolean isEnterable(Player player) {
        return true;
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile, a PlayerWonEvent is fired, indicating that the player has won
     * the level.
     *
     * @param player The player character entering the exit tile.
     */
    @Override
    public void onEnter(Player player) {
        getGame().fire(new PlayerWonEvent(player));
    }
    /**
     * Actions to be taken when the player exits this tile.
     *
     * For Exit tiles, it's considered an illegal movement, so it throws an IllegalStateException.
     *
     * @param player The player character exiting the exit tile.
     * @throws IllegalStateException if the player attempts to exit an Exit tile.
     */
    @Override
    public void onExit(Player player) {
        throw new IllegalStateException("Illegal movement!");
    }
}
