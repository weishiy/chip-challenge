package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.events.TickEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * Exports the view of the maze to other modules.
 *
 * <p>To use the Renderer module, construct this object, introduce it into your Swing or AWT
 * hierarchy, and then enable it with <code>setEnabled</code>.
 */
public class GameWindow extends JPanel implements GameEventListener {
    /**
     * Main domain object to query.
     */
    private final Game game;

    /**
     * Shows the entire level.
     */
    private final Maze maze = new Maze();

    /**
     * Constructor.
     *
     * <p>Only shows the maze when enabled.
     *
     * @param game The game world to be rendered.
     */
    public GameWindow(final Game game) {
        setEnabled(false);

        this.game = game;

        add(maze);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fitMaze();
            }
        });
        //TODO:stub
    }

    /**
     * Constructs with the given preferred size.
     *
     * @param game       The game world to be rendered.
     * @param windowSize Preferred length of this panel.
     */
    public GameWindow(final Game game, final int windowSize) {
        this(game);
        setSize(windowSize, windowSize);
    }

    private void fitMaze() {
        Level level = game.getLevel();

        int xRatio = getWidth() / level.getWidth();
        int yRatio = getHeight() / level.getHeight();

        int tileLength = Math.min(xRatio, yRatio);

        maze.setTileLength(tileLength);
    }

    /**
     * Sets whether this component is enabled. A component that is enabled may respond to user
     * input, while a component that is not enabled cannot respond to user input.  Some components
     * may alter their visual representation when they are disabled in order to provide feedback to
     * the user that they cannot take input.
     *
     * <p>Enabling this component allows it to update to game events. This component is disabled by
     * default.
     *
     * <p>Note: Disabling a component does not disable its children.
     *
     * <p>Note: Disabling a lightweight component does not prevent it from
     * receiving MouseEvents.
     *
     * @param enabled true if this component should be enabled, false otherwise
     * @see Component#isEnabled
     * @see Component#isLightweight
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            game.addListener(this);
            maze.setVisible(true);
        } else {
            game.removeListener(this);
            maze.setVisible(false);
        }
    }

    /*
     * Updates the renderer when necessary.
     */
    @Override //GameEventListener
    public void onGameEvent(final GameEvent gameEvent) {
        //Frequently repaints when needed.
        if (gameEvent instanceof TickEvent) {
            maze.render();
            repaint();
        }
    }


}
