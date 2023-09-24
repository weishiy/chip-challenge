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

public class Application extends JFrame implements ApplicationDebugger {

    private final Persistence persistence;

    private final InputMap inputMap;
    private final ActionMap actionMap;
    private final JPanel mainPanel;
    private final JPanel rightPanel;
    private final JLabel levelNoLabel;
    private final JLabel timeLabel;
    private final JLabel chipsLeftLabel;

    private ApplicationState state;

    public Application() {
        super("Chips Challenge");

        this.persistence = new FileBasedPersistenceImpl();

        setResizable(false);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

        mainPanel = SwingHelper.addPanel(getContentPane(), 600, 600, 60, 60, 60, 60);

        rightPanel = SwingHelper.addPanel(getContentPane(), 240, 600, 60, 0, 60, 60);
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

        var tipsPanel = SwingHelper.addPanel(rightPanel, 240, 315, 0, 0, 15, 0);
        tipsPanel.setBackground(Color.LIGHT_GRAY);
        tipsPanel.setLayout(new BoxLayout(tipsPanel, BoxLayout.PAGE_AXIS));
        SwingHelper.addLabel(tipsPanel, "==== Menu Controls ====", 240, 15, SwingConstants.LEFT, false);
        SwingHelper.addLabel(tipsPanel, "CTRL-1 -> Start level 1", 240, 15, SwingConstants.LEFT);
        SwingHelper.addLabel(tipsPanel, "CTRL-2 -> Start level 2", 240, 15, SwingConstants.LEFT, false);
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

        setApplicationState(new WelcomingState(this));

        inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = getRootPane().getActionMap();
        bindingBasicKeyStrokes();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                state.onStateExit();
                System.exit(0);
            }
        });

        pack();
        setVisible(true);
    }

    private void bindingBasicKeyStrokes() {
        bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK),
                e -> state.onNewGame(1));
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

    public void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback) {
        inputMap.put(keyStroke, keyStroke.toString());
        actionMap.put(keyStroke.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.accept(e);
            }
        });
    }

    public void unbindInputWithAction(KeyStroke keyStroke) {
        actionMap.remove(keyStroke.toString());
        inputMap.remove(keyStroke);
    }

    public Persistence getPersistence() {
        return persistence;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public JLabel getLevelNoLabel() {
        return levelNoLabel;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public JLabel getChipsLeftLabel() {
        return chipsLeftLabel;
    }

    public void setApplicationState(ApplicationState state) {
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
        state.onNewGameInDebugMode(levelNo);
        return ((DebuggingState) state).getGameEngine();
    }

}
