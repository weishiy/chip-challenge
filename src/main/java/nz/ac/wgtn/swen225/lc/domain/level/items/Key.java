package nz.ac.wgtn.swen225.lc.domain.level.items;

import nz.ac.wgtn.swen225.lc.domain.Entity;

/**
 * Presents a key, which is used by {@link nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile} and
 * {@link nz.ac.wgtn.swen225.lc.domain.level.characters.Player}
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Key extends Entity {

    /**
     * An enumeration representing the color of the key.
     */
    public enum Color {
        RED, YELLOW, BLUE, GREEN
    }

    private final Color color;

    /**
     * Constructs a key object with the specified color.
     *
     * @param color The color of the key.
     */
    public Key(Color color) {
        super();
        this.color = color;
    }

    /**
     * Constructs a key object with a specific ID and color.
     *
     * @param id The unique identifier for the key.
     * @param color The color of the key.
     */
    public Key(int id, Color color) {
        super(id);
        this.color = color;
    }

    /**
     * Get the color of the key.
     *
     * @return The color of the key, as a Color enumeration value.
     */
    public Color getColor() {
        return color;
    }

}
