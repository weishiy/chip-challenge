package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldPressedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldReleasedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents an info field tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class InfoField extends Tile {
    private final String message;
    private boolean active;
    /**
     * Constructs an InfoField tile object with the specified position and message, initially inactive.
     *
     * @param position The position of the info field tile.
     * @param message The message to be displayed by the info field.
     */
    public InfoField(Vector2D position, String message) {
        this(position, message, false);
    }
    /**
     * Constructs an InfoField tile object with the specified position, message, and activation state.
     *
     * @param position The position of the info field tile.
     * @param message The message to be displayed by the info field.
     * @param active The initial activation state of the info field.
     */
    public InfoField(Vector2D position, String message, boolean active) {
        super(position);
        this.message = message;
        this.active = active;
    }
    /**
     * Constructs an InfoField tile object with the specified ID, position, message, and activation state.
     *
     * @param id The unique identifier for the info field tile.
     * @param position The position of the info field tile.
     * @param message The message to be displayed by the info field.
     * @param active The initial activation state of the info field.
     */
    public InfoField(int id, Vector2D position, String message, boolean active) {
        super(id, position);
        this.message = message;
        this.active = active;
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * The player can always enter an info field tile.
     *
     * @param player The player character attempting to enter the tile.
     * @return True, indicating that the tile is always enterable.
     */
    @Override
    public boolean isEnterable(Player player) {
        return true;
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile, the info field becomes active, and an InfoFieldPressedEvent
     * is fired, indicating that the player has interacted with the info field.
     *
     * @param player The player character entering the info field tile.
     */
    @Override
    public void onEnter(Player player) {
        active = true;
        getGame().fire(new InfoFieldPressedEvent(this, player));
    }
    /**
     * Actions to be taken when the player exits this tile.
     *
     * When the player exits this tile, the info field becomes inactive, and an InfoFieldReleasedEvent
     * is fired, indicating that the player has finished interacting with the info field.
     *
     * @param player The player character exiting the info field tile.
     */
    @Override
    public void onExit(Player player) {
        active = false;
        getGame().fire(new InfoFieldReleasedEvent(this, player));
    }
    /**
     * Get the message displayed by the info field.
     *
     * @return The message displayed by the info field.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Check whether the info field is currently active.
     *
     * @return True if the info field is active, indicating that the message is currently displayed.
     */
    public boolean isActive() {
        return active;
    }

}
