package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.level.Level;

import javax.swing.*;
import java.awt.*;

/**
 * Renders the entirety of the tiles on the level.
 */
class Maze extends JPanel {

    /**
     * By default, the length we set our tiles.
     */
    private static final int DEFAULT_TILE_LENGTH = 50;
    /**
     * The layout manager for this component.
     */
    private final GridLayout layout = new GridLayout();
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
        setLayout(layout);
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
        removeAll();

        addTiles();

        repaint();
        //TODO:stub
    }

    /*
     * Adds tiles to this panel.
     */
    private void addTiles() {
        if (level != null) {
            int rows = level.getHeight();
            int columns = level.getWidth();

            layout.setRows(rows);
            layout.setColumns(columns);

            JComponent[][] board = Tiles.makeBoard(level);
            assert board.length == columns;
            assert board[0].length == rows;

            for (int x = 0; x < columns; ++x) {
                for (int y = 0; y < rows; ++y) {
                    add(board[x][y]);
                }
            }
        }
    }
}
