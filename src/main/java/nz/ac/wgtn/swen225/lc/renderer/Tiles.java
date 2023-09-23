package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;

import javax.swing.*;
import java.util.Objects;

/**
 * Helper interface for making tiles for the maze.
 */
interface Tiles {
    /**
     * From the given <code>Tile</code>, constructs a new <code>JComponent</code> representing it.
     *
     * <p>The specific subclass of <code>tile</code> provides information on what the tile will appear as.
     *
     * @param tile The tile to make a component of.
     * @return A representation of the tile.
     * @throws IllegalArgumentException If <code>tile</code> doesn't match any known subclass of <code>Tile</code>.
     * @throws NullPointerException     If <code>tile</code> is null.
     */
    static JComponent makeTile(Tile tile) throws IllegalArgumentException {
        Objects.requireNonNull(tile);
        //TODO: Stub, final version should assign the label the image associated with the Tile.
        return new JLabel(tile.getClass().getName());
    }
}
