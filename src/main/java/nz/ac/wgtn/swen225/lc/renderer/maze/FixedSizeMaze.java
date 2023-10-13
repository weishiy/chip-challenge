package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.domain.Game;

import java.awt.*;

/**
 * A Maze which can't be resized from outside.
 *
 * <p>Instead, call <code>setTileLength</code> to change size.
 */
public class FixedSizeMaze extends ResizeableMaze {

    /**
     * Default size of the tile.
     */
    private static final int DEFAULT_TILE_LENGTH = 50;

    /**
     * Length of a tile's side, in pixels.
     */
    private int tileLength = DEFAULT_TILE_LENGTH;

    /**
     * Internally used to decide when size can be set.
     */
    private boolean canSetSize = false;

    /**
     * Constructor.
     *
     * @param game The game the maze represents.
     */
    public FixedSizeMaze(final Game game) {
        super(game);
    }

    private void assertCanSetSize() {
        if (!canSetSize) {
            throw new UnsupportedOperationException("This maze can't be resized.");
        }
    }

    //Doesn't allow to change size.
    @Override
    public final void setSize(final Dimension d) {
        assertCanSetSize();
        super.setSize(d);
    }

    @Override
    public final void setSize(final int width, final int height) {
        assertCanSetSize();
        super.setSize(width, height);
    }

    @Override
    public final void setBounds(final Rectangle r) {
        assertCanSetSize();
        super.setBounds(r);
    }

    @Override
    public final void setBounds(final int x, final int y, final int width, final int height) {
        assertCanSetSize();
        super.setBounds(x, y, width, height);
    }

    @Override
    @SuppressWarnings("deprecation")
    public final void resize(final Dimension d) {
        assertCanSetSize();
        super.resize(d);
    }

    @Override
    @SuppressWarnings("deprecation")
    public final void resize(final int width, final int height) {
        assertCanSetSize();
        super.resize(width, height);
    }

    /**
     * Gets the current tile length
     *
     * <p>This method is used internally to decide on the size of this maze.
     *
     * @return The tile length, in pixels.
     */
    @Override

    protected final int getTileLength() {
        return tileLength;
    }


    /**
     * Set's the tile length, and updates the board.
     *
     * @param newTileLength In pixels, the tile length.
     */
    public void setTileLength(final int newTileLength) {
        this.tileLength = newTileLength;
        if (isLevelSet()) {
            internalSetSize(getCroppedSize());
        }
    }

    private synchronized void internalSetSize(final Dimension size) {
        try {
            canSetSize = true;
            setSize(size);

        } finally {
            canSetSize = false;
        }
    }
}
