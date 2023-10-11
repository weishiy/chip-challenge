package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.events.PlayerMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Presents a player
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Player extends Character {

    private final Set<Key> keys = new HashSet<>();
    private final Set<Chip> chips = new HashSet<>();

    /**
     * Constructs a player with the given position.
     *
     * @param position The position of the player.
     */
    public Player(Vector2D position) {
        super(position);
    }
    /**
     * Constructs a new player object with the given ID and position.
     *
     * @param id The unique identifier for the player.
     * @param position The position of the player.
     */
    public Player(int id, Vector2D position, Set<Key> keys, Set<Chip> chips) {
        super(id, position);
        this.keys.addAll(keys);
        this.chips.addAll(chips);
    }

    /**
     * Moves the enemy to a new position and fires {@link PlayerMovedEvent}
     * <p>
     * @param position
     */
    @Override
    public void setPosition(Vector2D position) {
        var oldPosition = getPosition();
        super.setPosition(position);
        getGame().fire(new PlayerMovedEvent(this, oldPosition, getPosition()));
    }

    /**
     * Get an unmodifiable view of the keys held by the player.
     *
     * @return An unmodifiable set of keys.
     */
    public Set<Key> getKeys() {
        return Set.copyOf(keys);
    }

    /**
     * Add a key to the player's inventory.
     *
     * @param key The key to add.
     */
    public void addKey(Key key) {
        keys.add(key);
    }

    /**
     * Remove a key from the player's inventory.
     *
     * @param key The key to remove.
     */
    public void removeKey(Key key) {
        keys.remove(key);
    }

    /**
     * Get an unmodifiable view of the chips collected by the player.
     *
     * @return An unmodifiable set of chips.
     */
    public Set<Chip> getChips() {
        return Set.copyOf(chips);
    }

    /**
     * Add a chip to the player's collected chips.
     *
     * @param chip The chip to add.
     */
    public void addChip(Chip chip) {
        chips.add(chip);
    }

    /**
     * Remove a chip from the player's collected chips.
     *
     * @param chip The chip to remove.
     */
    public void removeChip(Chip chip) {
        chips.remove(chip);
    }

}
