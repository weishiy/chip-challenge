package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultReplayer implements Replayer {

    // Key Bindings for controlling the replay
    private static final KeyStroke PLAY_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_1, 0);
    private static final KeyStroke PAUSE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_2, 0);
    private static final KeyStroke JUMP_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_3, 0);
    private static final KeyStroke STOP_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_4, 0);
    private static final KeyStroke FAST_REVERSE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_5, 0);
    private static final KeyStroke FAST_FORWARD_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_6, 0);

    // Constants for controlling the playback speed
    private static final float MIN_SPEED = 0.25f;
    private static final float MAX_SPEED = 8f;

    private final GameEngine gameEngine;
    private final Map<Integer, Vector2D> playerMovements = new HashMap<>();
    private final Map<Integer, Map<Enemy, Vector2D>> enemyMovements = new HashMap<>();
    private final int endTick;
    private final Timer timer;

    private float currentPlaybackSpeed = 1f; // Playback speed (allowed values: 0.25f, 0.5f, 1f, 2f, 4f)

    public DefaultReplayer(GameEngine gameEngine, Playback playback) {
        this.gameEngine = gameEngine;

        //Initialize player and enemy movement from playback data
        this.playerMovements.putAll(
                playback.getMoments()
                        .stream()
                        .collect(Collectors.toMap(Moment::tickNo, Moment::playerMovement)));
        this.enemyMovements.putAll(
                playback.getMoments()
                        .stream()
                        .collect(Collectors.toMap(Moment::tickNo, Moment::enemyMovementMap)));
        this.endTick = playback.getEndTickNo();

        this.timer = new Timer(1000 / Game.FRAME_RATE, e -> this.update());
    }


    @Override
    public void onStart() {
        this.bindKeyStrokes(); // Bind keys to control replay action
        this.timer.start(); // Start the timer to advance the replay
    }

    @Override
    public void onDestroy() {
        this.timer.stop(); // Stop the timer
        this.unbindKeyStrokes(); // Unbind keys used for the replay control
    }

    private void update(){
        var currentTick = gameEngine.getTickNo();
        // Check if the replay has reached the end
        if(currentTick == endTick) {
            this.timer.stop();
            gameEngine.getGlassPane().add(new JLabel("Replay ended.")); // Display a message on the glass pane
            return;
        }

        // Get player and enemy movement for the current tick and update the game engine
        var playerMoment = playerMovements.getOrDefault(currentTick, null);
        var enemyMomentMap = enemyMovements.getOrDefault(currentTick, Map.of());
        gameEngine.update(playerMoment, enemyMomentMap);
    }

    private void bindPlayAction() {
        gameEngine.bindInputWithAction(PLAY_KEY, e -> {
            if (!timer.isRunning()) {
                timer.start(); // Start or resume the replay
            }
        });
    }

    private void bindPauseORStopAction(KeyStroke keyStroke) {
        gameEngine.bindInputWithAction(keyStroke, e -> {
            if (timer.isRunning()) {
                timer.stop(); // Pause or Stop the replay depending on the button pressed
            }
        });
    }

/*    private void bindStopAction() {
        gameEngine.bindInputWithAction(STOP_KEY, e -> {
            if (timer.isRunning()) {
                timer.stop();
            }
        });
    }*/

    private void bindJumpAction() {
        gameEngine.bindInputWithAction(JUMP_KEY, e -> {
            // TODO: Implement jump action
        });
    }

    private void bindFastReverseAction() {
        gameEngine.bindInputWithAction(FAST_REVERSE_KEY, e -> {
            currentPlaybackSpeed /= 2f;
            if (currentPlaybackSpeed < MIN_SPEED) {
                currentPlaybackSpeed = MIN_SPEED; // Ensure playback speed doesn't go below the minimum
            }
            timer.setDelay((int) (1000 / (Game.FRAME_RATE * currentPlaybackSpeed)));
        });
    }

    private void bindFastForwardAction() {
        gameEngine.bindInputWithAction(FAST_FORWARD_KEY, e -> {
            currentPlaybackSpeed *= 2f;
            if (currentPlaybackSpeed > MAX_SPEED) {
                currentPlaybackSpeed = MAX_SPEED; // Ensure playback speed doesn't exceed the maximum
            }
            timer.setDelay((int) (1000 / (Game.FRAME_RATE * currentPlaybackSpeed)));
        });
    }

    // Binds keys to action for controlling the replay
    private void bindKeyStrokes() {
        bindPlayAction(); // PLAY_KEY
        bindPauseORStopAction(PAUSE_KEY); // PAUSE_KEY
        bindPauseORStopAction(STOP_KEY); // STOP_KEY
        bindJumpAction(); // JUMP_KEY
        bindFastReverseAction(); // FAST_FORWARD_KEY
        bindFastForwardAction(); // REVERSE_FORWARD_KEY
    }

    private void unbindKeyStrokes() {
        gameEngine.unbindInputWithAction(PLAY_KEY);
        gameEngine.unbindInputWithAction(PAUSE_KEY);
        gameEngine.unbindInputWithAction(STOP_KEY);
        gameEngine.unbindInputWithAction(JUMP_KEY);
        gameEngine.unbindInputWithAction(FAST_REVERSE_KEY);
        gameEngine.unbindInputWithAction(FAST_FORWARD_KEY);
    }


}
