package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.level.Level;

import javax.swing.*;

/**
 * Renders the entirety of the tiles on the level.
 */
class Maze extends JPanel {
    private static final int DEFAULT_TILE_LENGTH = 50;

    /**
     * The level this maze is rendering.
     */
    private Level level;

    /**
     * The length of a square tile. Changing this acts like a "zoom" feature, making the maze bigger
     * or smaller.
     */
    private int tileLength = DEFAULT_TILE_LENGTH;

    /**
     * Constructor.
     */
    Maze() {
        //TODO: stub
    }

    /**
     * Sets the level to render.
     *
     * @param level The new level, or <code>null</code>.
     */
    public void setLevel(final Level level) {
        this.level = level;
        render();
    }

    /**
     * Sets the tile length.
     *
     * <p>The tile length determines the size of the overall maze, and setting it changes how large
     * the maze appears.
     *
     * @param tileLength The new length.
     * @throws IllegalArgumentException If <code>tileLength</code> isn't positive.
     */
    public void setTileLength(final int tileLength) throws IllegalArgumentException {
        if (tileLength <= 0) {
            throw new IllegalArgumentException("`tileLength` must be positive.");
        }
        this.tileLength = tileLength;
        render();
    }

    /**
     * Updates to account to changes in level.
     *
     * <p>If <code>level</code> isn't set, or was set to <code>null</code>, doesn't render any
     * tiles.
     */
    public void render() {
        //TODO:stub
    }
}
