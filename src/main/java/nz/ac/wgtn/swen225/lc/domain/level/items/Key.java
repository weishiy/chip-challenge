package nz.ac.wgtn.swen225.lc.domain.level.items;

import nz.ac.wgtn.swen225.lc.domain.Entity;

/**
 * Presents a key, which is used by {@link nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile} and
 * {@link nz.ac.wgtn.swen225.lc.domain.level.characters.Player}
 */
public final class Key extends Entity {

    public enum Color {
        RED, YELLOW, BLUE
    }

    private final Color color;

    public Key(Color color) {
        super();
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
