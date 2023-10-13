package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.app.GameEngineImpl;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.*;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.recorder.DefaultRecorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.stream.Collectors;
import java.util.List;

/**
 * The `PlayingState` class represents the state of the game when actively playing.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class PlayingState extends AbstractApplicationState implements GameEventListener {

    private final Game game;
    private final GameEngine gameEngine;
    private final Recorder recorder;
    private final Timer timer;

    JLabel paused = new JLabel("Paused");

    private Vector2D playerMovement = Vector2D.ZERO;

    /**
     * Constructs a `PlayingState` object.
     *
     * @param application The game application instance.
     * @param game        The current game being played.
     */
    public PlayingState(Application application, Game game) {
        super(application);
        this.game = game;
        this.gameEngine = new GameEngineImpl(
                game,
                application.getMainPanel(),
                application.getLevelNoLabel(),
                application.getTimeLabel(),
                application.getChipsLeftLabel(),
                application.getInventoryPanel());
        recorder = new DefaultRecorder(getApplication().getPersistence(), game);
        timer = new Timer(1000 / Game.FRAME_RATE, e -> update());
        paused.setForeground(Color.WHITE);
        paused.setFont(new Font("Serif", Font.PLAIN, 50));
    }

    /**
     * Called when this state is entered. Starts the game engine, recorder, and timer.
     */
    @Override
    public void onStateEnter() {
        gameEngine.onStart();
        recorder.onStart();
        game.addListener(this);
        bindKeyStrokes();
        timer.start();
    }

    /**
     * Called when the game is paused. Stops the timer and displays a "Paused" message.
     */
    @Override
    public void onPauseGame() {
        timer.stop();
        gameEngine.getGlassPane().add(paused);
        gameEngine.getGlassPane().setVisible(true);
    }

    /**
     * Called when exiting the pause state. Resumes the timer and removes the "Paused" message.
     */
    @Override
    public void onExitPause() {
        timer.start();
        gameEngine.getGlassPane().remove(paused);
        gameEngine.getGlassPane().setVisible(false);
    }

    /**
     * Called when saving and exiting the game. Stops the timer and prompts the user to save the game.
     * Upon selecting a file to save, transitions to the welcoming state.
     */
    @Override
    public void onSaveAndExitGame() {
        timer.stop();

        var fileChooser = new JFileChooser();
        var result = fileChooser.showSaveDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION -> {
                getApplication().getPersistence().saveGame(fileChooser.getSelectedFile(), game);
                getApplication().setApplicationState(new WelcomingState(getApplication()));
            }
            case JFileChooser.CANCEL_OPTION -> timer.start();
        }
    }

    /**
     * Called when exiting the game. Stops the timer and transitions to the welcoming state.
     */
    @Override
    public void onExitGame() {
        timer.stop();
        getApplication().setApplicationState(new WelcomingState(getApplication()));
    }

    /**
     * Called when this state is exited. Stops the timer, unbinds key strokes, removes listeners,
     * and cleans up resources.
     */
    @Override
    public void onStateExit() {
        timer.stop();
        unbindKeyStrokes();
        game.removeListener(this);
        recorder.onDestroy();
        gameEngine.onDestroy();
    }

    /**
     * Handles game events, such as game over events.
     *
     * @param gameEvent The game event to handle.
     */
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof PlayerWonEvent) {
            int currentLevel = game.getLevel().getLevelNo();
            List<Integer> availableLevels = getApplication().getPersistence().getAllLevelNos();
            if (currentLevel == availableLevels.get(availableLevels.size() - 1)) {
                // Player won the last level.
                getApplication().setApplicationState(new GameOverState(getApplication(), true));
            } else {
                // Player won a level, advance to the next available level.
                int nextLevelNo = availableLevels.get(availableLevels.indexOf(currentLevel) + 1);
                onNewGame(nextLevelNo);
            }
        } else if (gameEvent instanceof PlayerDiedEvent || gameEvent instanceof TimeoutEvent) {
            // Player lost the game, go to game over state (player lost).
            getApplication().setApplicationState(new GameOverState(getApplication(), false));
        }
    }

    /**
     * Updates the game state, records player and enemy movements, and updates the game engine.
     */
    private void update() {
        var enemyMovementMap =
                game.getLevel()
                        .getEnemies()
                        .stream()
                        .collect(Collectors.toMap(e -> e, Enemy::nextMove));
        recorder.update(playerMovement, enemyMovementMap);
        gameEngine.update(playerMovement, enemyMovementMap);
        playerMovement = Vector2D.ZERO;
    }

    /**
     * Binds player movement to arrow keys.
     */
    private void bindKeyStrokes() {
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                e -> playerMovement = Vector2D.LEFT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                e -> playerMovement = Vector2D.UP);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                e -> playerMovement = Vector2D.RIGHT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                e -> playerMovement = Vector2D.DOWN);
    }

    /**
     * Unbinds player movement from arrow keys.
     */
    private void unbindKeyStrokes() {
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    }
}
