package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.DockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.KeyConsumedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a locked door tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class LockedDoor extends Tile {
    private final Key.Color color;
    /**
     * Constructs a LockedDoor tile object with the specified position and key color.
     *
     * @param position The position of the locked door tile.
     * @param color The color of the key required to unlock the door.
     */
    public LockedDoor(Vector2D position, Key.Color color) {
        super(position);
        this.color = color;
    }
    /**
     * Constructs a LockedDoor tile object with the specified ID, position, and key color.
     *
     * @param id The unique identifier for the locked door tile.
     * @param position The position of the locked door tile.
     * @param color The color of the key required to unlock the door.
     */
    public LockedDoor(int id, Vector2D position, Key.Color color) {
        super(id, position);
        this.color = color;
    }
    /**
     * Get the color of the key required to unlock this locked door.
     *
     * @return The color of the key required to unlock the door.
     */
    public Key.Color getColor() {
        return color;
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * The player can enter this tile if they have a key of the matching color.
     *
     * @param player The player character attempting to enter the tile.
     * @return True if the player has a matching key and can unlock the door.
     */
    @Override
    public boolean isEnterable(Player player) {
        return player.getKeys().stream().anyMatch(k -> k.getColor() == color);
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile with the matching key, the key is consumed, and the door is
     * unlocked. A KeyConsumedEvent is fired to indicate that the key has been consumed, and a
     * DockUnlockedEvent is fired to indicate that the player has unlocked the door.
     *
     * @param player The player character entering the locked door tile.
     * @throws IllegalStateException if the player attempts to enter without the matching key.
     */
    public void onEnter(Player player) {
        if (!isEnterable(player)) {
            throw new IllegalStateException("Illegal movement!");
        }

        var matchingKey = player.getKeys().stream().filter(k -> k.getColor() == color).findAny().orElse(null);
        assert matchingKey != null;
        player.removeKey(matchingKey);
        getGame().fire(new KeyConsumedEvent(matchingKey));
        // remove this tile from the level
        getLevel().removeTile(this);
        getGame().fire(new DockUnlockedEvent(this, player));
        setLevel(null);
    }
    /**
     * Actions to be taken when the player exits this tile.
     *
     * For LockedDoor tiles, no specific action is taken when the player exits.
     *
     * @param player The player character exiting the locked door tile.
     */
    @Override
    public void onExit(Player player) {
        // do nothing
    }
}
