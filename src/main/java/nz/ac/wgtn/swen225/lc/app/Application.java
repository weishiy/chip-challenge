package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.app.states.ApplicationState;
import nz.ac.wgtn.swen225.lc.app.states.DebuggingState;
import nz.ac.wgtn.swen225.lc.app.states.WelcomingState;
import nz.ac.wgtn.swen225.lc.persistency.FileBasedPersistenceImpl;
import nz.ac.wgtn.swen225.lc.persistency.Persistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * The main application class for the "Chips Challenge" game.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class Application extends JFrame implements ApplicationDebugger {

    // Persistence object for saving and loading game data
    private final Persistence persistence;

    // Input and action maps for key bindings
    private final InputMap inputMap;
    private final ActionMap actionMap;

    // Panels for displaying game content and stats
    private final JPanel mainPanel;
    private final JPanel rightPanel;
    private final JLabel levelNoLabel;
    private final JLabel timeLabel;
    private final JLabel chipsLeftLabel;

    // Current application state
    private ApplicationState state;

    /**
     * Constructs a new instance of the "Chips Challenge" application.
     */
    public Application() {
        super("Chips Challenge");

        // Initialize persistence
        this.persistence = new FileBasedPersistenceImpl();

        // Set frame properties
        setResizable(false);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

        // Create main panel for game display
        mainPanel = SwingHelper.addPanel(getContentPane(), 600, 600, 60, 60, 60, 60);

        // Create right panel for game stats and controls
        rightPanel = SwingHelper.addPanel(getContentPane(), 240, 600, 60, 0, 60, 60);

        // Create a panel for displaying level number, time, and chips left
        var statsPanel = SwingHelper.addPanel(rightPanel, 240, 240, 0, 0, 15, 0);
        statsPanel.setBackground(Color.LIGHT_GRAY);
        SwingHelper.addLabel(statsPanel, "LEVEL", 240, 20, SwingConstants.CENTER);
        levelNoLabel = SwingHelper.addLabel(statsPanel, "0", 240, 20, SwingConstants.CENTER);
        levelNoLabel.setOpaque(true);
        levelNoLabel.setBackground(Color.BLACK);
        levelNoLabel.setForeground(Color.WHITE);
        SwingHelper.addLabel(statsPanel, "TIME", 240, 20, SwingConstants.CENTER);
        timeLabel = SwingHelper.addLabel(statsPanel, "0", 240, 20, SwingConstants.CENTER);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setForeground(Color.WHITE);
        SwingHelper.addLabel(statsPanel, "CHIPS LEFT", 240, 20, SwingConstants.CENTER);
        chipsLeftLabel = SwingHelper.addLabel(statsPanel, "0", 240, 20, SwingConstants.CENTER);
        chipsLeftLabel.setOpaque(true);
        chipsLeftLabel.setBackground(Color.BLACK);
        chipsLeftLabel.setForeground(Color.WHITE);

        // Create a panel for displaying control tips
        var tipsPanel = SwingHelper.addPanel(rightPanel, 240, 315, 0, 0, 15, 0);
        tipsPanel.setBackground(Color.LIGHT_GRAY);
        tipsPanel.setLayout(new BoxLayout(tipsPanel, BoxLayout.PAGE_AXIS));
        SwingHelper.addLabel(tipsPanel, "==== Menu Controls ====", 240, 15, SwingConstants.LEFT, false);
        SwingHelper.addLabel(tipsPanel, "CTRL-1 -> Start level 1", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-2 -> Start level 2", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-R -> Load game", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "SPACE  -> Pause", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "ESC    -> Exit pause", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-S -> Save and exit", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-X -> Exit", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-P -> Load playback", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "==== Play Controls ====", 240, 15, SwingConstants.LEFT, false);
        SwingHelper.addLabel(tipsPanel, "LEFT   -> Move left", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "UP     -> Move up", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "RIGHT  -> Move right", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "DOWN   -> Move down", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "=== Playback Controls ===", 240, 15, SwingConstants.LEFT, false);
        SwingHelper.addLabel(tipsPanel, "1      -> Play", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "2      -> Pause", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "3      -> Stop", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "4      -> Jump", 240, 15, SwingConstants.LEFT, false);
        SwingHelper.addLabel(tipsPanel, "5      -> Fast forward", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "6      -> Fast reverse", 240, 15, SwingConstants.LEFT);

        // Set the initial application state to WelcomingState
        setApplicationState(new WelcomingState(this));

        // Initialize input maps and action maps
        inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = getRootPane().getActionMap();
        bindingBasicKeyStrokes();

        // Add window listener for closing the application
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                state.onStateExit();
                System.exit(0);
            }
        });

        // Pack and display the application frame
        pack();
        setVisible(true);
    }

    /**
     * Binds basic key strokes to corresponding actions.
     */
    private void bindingBasicKeyStrokes() {
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK),
                e -> state.onNewGame(1));
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK),
                e -> state.onNewGame(2));
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
                e -> state.onLoadGame());
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
                e -> state.onPauseGame());
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                e -> state.onExitPause());
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
                e -> state.onSaveAndExitGame());
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),
                e -> state.onExitGame());
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK),
                e -> state.onLoadReplay());
    }

    /**
     * Binds a key stroke to an action callback.
     *
     * @param keyStroke The key stroke to bind.
     * @param callback  The callback to execute when the key stroke is triggered.
     */
    public void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback) {
        inputMap.put(keyStroke, keyStroke.toString());
        actionMap.put(keyStroke.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.accept(e);
            }
        });
    }

    /**
     * Unbinds a key stroke from its associated action.
     *
     * @param keyStroke The key stroke to unbind.
     */
    public void unbindInputWithAction(KeyStroke keyStroke) {
        actionMap.remove(keyStroke.toString());
        inputMap.remove(keyStroke);
    }

    /**
     * Gets the persistence object for saving and loading game data.
     *
     * @return The persistence object.
     */
    public Persistence getPersistence() {
        return persistence;
    }

    /**
     * Gets the main panel for displaying game content.
     *
     * @return The main panel.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Gets the right panel for displaying game stats and controls.
     *
     * @return The right panel.
     */
    public JPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Gets the label displaying the current level number.
     *
     * @return The level number label.
     */
    public JLabel getLevelNoLabel() {
        return levelNoLabel;
    }

    /**
     * Gets the label displaying the current game time.
     *
     * @return The time label.
     */
    public JLabel getTimeLabel() {
        return timeLabel;
    }

    /**
     * Gets the label displaying the number of chips left.
     *
     * @return The chips left label.
     */
    public JLabel getChipsLeftLabel() {
        return chipsLeftLabel;
    }

    /**
     * Sets the current application state.
     *
     * @param state The new application state to set.
     */
    public void setApplicationState(ApplicationState state) {
        // Handle state transition
        if (this.state != null) {
            this.state.onStateExit();
        }
        this.state = state;
        if (this.state != null) {
            this.state.onStateEnter();
        }
    }

    @Override
    public GameEngine newGameInDebugMode(int levelNo) {
        // Create a new game in debug mode for the specified level
        state.onNewGameInDebugMode(levelNo);
        return ((DebuggingState) state).getGameEngine();
    }
}
