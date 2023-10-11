package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a key tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class KeyTile extends Tile {

    private final Key key;
    /**
     * Constructs a KeyTile object with the specified position and key.
     *
     * @param position The position of the tile.
     * @param key The key contained within the tile.
     */
    public KeyTile(Vector2D position, Key key) {
        super(position);
        this.key = key;
    }
    /**
     * Constructs a KeyTile object with the specified ID, position, and key.
     *
     * @param id The unique identifier for the tile.
     * @param position The position of the tile.
     * @param key The key contained within the tile.
     */
    public KeyTile(int id, Vector2D position, Key key) {
        super(id, position);
        this.key = key;
    }
    /**
     * Get the key contained within this tile.
     *
     * @return The key object within this tile.
     */
    public Key getKey() {
        return key;
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * @param player The player character attempting to enter the tile.
     * @return True, indicating that the tile is enterable by the player.
     * @throws IllegalStateException if the tile is not associated with a valid level.
     */
    @Override
    public boolean isEnterable(Player player) {
        if (getLevel() == null || !getLevel().getTiles().contains(this)) {
            throw new IllegalStateException("Stale tile being used!");
        }
        return true;
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile, the key is picked up by the player. The tile is removed
     * from the level, and a KeyPickedUpEvent is fired to indicate that the player has collected
     * the key.
     *
     * @param player The player character entering the tile.
     * @throws IllegalStateException if the tile is not associated with a valid level.
     */
    @Override
    public void onEnter(Player player) {
        if (getLevel() == null || !getLevel().getTiles().contains(this)) {
            throw new IllegalStateException("Stale tile being used!");
        }

        player.addKey(key);
        getLevel().removeTile(this);
        getGame().fire(new KeyPickedUpEvent(this, player));
        setLevel(null);
    }
    /**
     * Actions to be taken when the player exits this tile.
     *
     * For KeyTiles, no specific action is taken when the player exits.
     *
     * @param player The player character exiting the tile.
     */
    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
