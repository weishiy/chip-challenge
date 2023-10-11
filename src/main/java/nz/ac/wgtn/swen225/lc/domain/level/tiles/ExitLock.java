package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ExitLockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents an exit lock tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class ExitLock extends Tile {
    /**
     * Constructs an ExitLock tile object with the specified position.
     *
     * @param position The position of the exit lock tile.
     */
    public ExitLock(Vector2D position) {
        super(position);
    }
    /**
     * Constructs an ExitLock tile object with the specified ID and position.
     *
     * @param id The unique identifier for the exit lock tile.
     * @param position The position of the exit lock tile.
     */
    public ExitLock(int id, Vector2D position) {
        super(id, position);
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * The player can enter this tile only if there are no adjacent ChipTiles, as indicated by the
     * absence of ChipTiles in the game level.
     *
     * @param player The player character attempting to enter the tile.
     * @return True if the tile is enterable, given the absence of adjacent ChipTiles.
     */
    @Override
    public boolean isEnterable(Player player) {
        return getLevel().getTiles().stream().noneMatch(t -> t instanceof ChipTile);
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile, it checks whether the player can enter it. If the conditions
     * are met, the tile is removed from the level, and an ExitLockUnlockedEvent is fired.
     *
     * @param player The player character attempting to enter the exit lock tile.
     * @throws IllegalStateException if the player attempts to enter when the conditions are not met.
     */
    @Override
    public void onEnter(Player player) {
        if (!isEnterable(player)) {
            throw new IllegalStateException("Illegal movement!");
        }

        // remove this tile from the level
        getLevel().removeTile(this);
        getGame().fire(new ExitLockUnlockedEvent(this, player));
        setLevel(null);
    }
    /**
     * Actions to be taken when the player exits this tile.
     *
     * For ExitLock tiles, no specific action is taken when the player exits.
     *
     * @param player The player character exiting the exit lock tile.
     */
    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
