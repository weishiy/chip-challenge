package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Game;

import javax.swing.*;


/**
 * Displays a portion of the maze centered around the player.
 */
public class GameWindow extends JViewport {
    /**
     * Constructor.
     *
     * @param game       The game world to be rendered.
     * @param windowSize Size-length of this component. Unused.
     */
    public GameWindow(final Game game, final int windowSize) {
        //TODO: stub
    }

    @Override
    public final void setEnabled(final boolean enabled) {
        //TODO: stub. Should only subscribe
        super.setEnabled(enabled);
    }
}
