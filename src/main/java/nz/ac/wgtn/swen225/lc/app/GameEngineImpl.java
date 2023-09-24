package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.ChipsPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.events.CountDownEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.renderer.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameEngineImpl implements GameEngine {

    private final Game game; // The game instance managed by this class
    private final JComponent parentComponent; // The parent container component
    private final Component gameDisplay; // The game display component
    private final InputMap inputMap; // Input map for the keyboard input
    private final ActionMap actionMap; // Action map for binding keys to action
    private final Set<KeyStroke> boundKeyStrokes = new HashSet<>(); // Set to keep track of the bounded keystroke

    // Labels for displaying game information
    private final JLabel levelLabel;
    private final JLabel timeLabel;
    private final JLabel chipsLeftLabel;

    // Constructor to initialize the game engine
    public GameEngineImpl(Game game, JComponent parentComponent, JLabel levelLabel, JLabel timeLabel, JLabel chipsLeftLabel) {
        this.game = game;
        this.parentComponent = parentComponent;
        this.gameDisplay = this.createGameDisplay(); // Create the game display component
        this.inputMap = parentComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.actionMap = parentComponent.getActionMap();
        this.levelLabel = levelLabel;
        this.timeLabel = timeLabel;
        this.chipsLeftLabel = chipsLeftLabel;
    }

    // Create the game display component
    private Component createGameDisplay() {
        return new GameWindow(game, 600);
    }


    @Override
    public void onStart() {
        game.addListener(this); // Register this engine as a game listener
        this.addGameDisplayToParent(); // Add the game display to the parent component
        this.setLabels(); // Initialzie the labels displaying game informatin
    }

    // Add the game display to the parent component
    private void addGameDisplayToParent(){
        this.parentComponent.add(this.gameDisplay);
        this.gameDisplay.setEnabled(true);
    }

    // Initialize the labels displaying game informatin
    private void setLabels(){
        this.levelLabel.setText(Integer.toString(this.game.getLevel().getLevelNo()));
        this.timeLabel.setText(Integer.toString(this.game.getCountDown()));
        this.chipsLeftLabel.setText(Integer.toString(this.game.getChipsLeft()));
    }

    @Override
    public boolean isGameOver() {
        return this.game.isGameOver();
    }

    @Override
    public int getTickNo() {
        return this.game.getTickNo();
    }

    @Override
    public void update(Vector2D playerMovement) {
        var enemyMovementMap = getEnemyMovementMap(); // Get enemy movement information
        this.update(playerMovement, enemyMovementMap);
    }

    // Get a map of enemy movement
    private Map<Enemy, Vector2D> getEnemyMovementMap() {
        return this.game.getLevel().getEnemies()
                .stream().collect(Collectors.toMap(e -> e, Enemy::nextMove));
    }


    // Update the game state
    @Override
    public void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovement) {
        this.game.update(playerMovement, enemyMovement);
    }

    @Override
    public void onDestroy() {
        this.setLabels(); // Update labels displaying game information
        this.unbindKeyStrokes(); // Unbind keyboard input action
        this.disableGameDisplay(); // Disable the game display
        removeGameDisplayFromParent(); // Remove the game display from the parent component
        this.removeListener(); // Remove this engine as a game listener
    }

    // Unbind keyboard input action
    private void unbindKeyStrokes() {
        this.boundKeyStrokes.forEach(this::unbindKeyStroke);
        this.boundKeyStrokes.clear();
    }

    // Unbind a specific key stroke
    private void unbindKeyStroke (KeyStroke keyStroke) {
        this.actionMap.remove(keyStroke.toString());
        this.inputMap.remove(keyStroke);
    }

    // Disable the game display
    private void disableGameDisplay() {
        this.gameDisplay.setEnabled(false);
    }

    // Remove the game display from the parent component
    private void removeGameDisplayFromParent() {
        this.parentComponent.remove(gameDisplay);
    }

    // Remove this engine as a game listener
    private void removeListener() {
        this.game.removeListener(this);
    }

    @Override
    public void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback) {
        this.inputMap.put(keyStroke, keyStroke.toString()); // Bind a key stroke to its action
        this.actionMap.put(keyStroke.toString(), this.createAction(keyStroke, callback)); // Create an action for the key stroke
        this.boundKeyStrokes.add(keyStroke); // Add the key stroke to the bound set
    }

    // Create an abstract action for the key stroke
    private AbstractAction createAction(KeyStroke keyStroke, Consumer<ActionEvent> callback) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                callback.accept(event);
            }
        };
    }

    @Override
    public void unbindInputWithAction(KeyStroke keyStroke) {
        this.unbindKeyStroke(keyStroke);
        this.boundKeyStrokes.remove(keyStroke);

    }

    @Override
    public Container getGlassPane() {
        return null;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        // Handle game events, such as countdown and chip picked up events
        if(gameEvent instanceof CountDownEvent) {
            this.updateTimeLabel();
        } else if (gameEvent instanceof ChipsPickedUpEvent) {
            this.updateChipsLeftLabel();
        }
    }

    // Update the time label
    private void updateTimeLabel() {
        this.timeLabel.setText(Integer.toString(this.game.getCountDown()));
    }

    // Update the chips left label
    private void updateChipsLeftLabel() {
        this.chipsLeftLabel.setText(Integer.toString(this.game.getChipsLeft()));
    }


}
