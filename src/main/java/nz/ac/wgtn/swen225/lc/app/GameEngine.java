package nz.ac.wgtn.swen225.lc.app;


import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The GameEngine interface defines methods for managing the game state and input handling.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public interface GameEngine extends GameEventListener {

    /**
     * Called when the game engine is started.
     */
    void onStart();

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Gets the current tick number in the game.
     *
     * @return The tick number.
     */
    int getTickNo();

    /**
     * Updates the game state based on the player's movement.
     *
     * @param playerMovement The movement vector of the player.
     */
    void update(Vector2D playerMovement);

    /**
     * Updates the game state based on the player's and enemies' movement.
     *
     * @param playerMovement The movement vector of the player.
     * @param enemyMovement  A map of enemy movement vectors.
     */
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovement);

    /**
     * Called when the game engine is destroyed or finished.
     */
    void onDestroy();

    /**
     * Binds a keyboard input key stroke to an action.
     *
     * @param keyStroke The key stroke to bind.
     * @param callback  The action to perform when the key is pressed.
     */
    void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback);

    /**
     * Unbinds a keyboard input key stroke from its action.
     *
     * @param keyStroke The key stroke to unbind.
     */
    void unbindInputWithAction(KeyStroke keyStroke);

    /**
     * Allows accessing the player position.
     *
     * @return The game instance.
     */
    Vector2D getPlayerPos();

    /**
     * Gets the glass pane component, which may be used for overlays or GUI elements.
     *
     * @return The glass pane component.
     */
    Container getGlassPane();
}
