package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import java.util.Map;

/**
 * The Recorder interface defines methods for recording and managing game actions or events.
 * Implementations of this interface can be used to record player movements and enemy movements
 * within a game.
 *
 * @author Sajja Syed 300551462
 */
public interface Recorder {

    /**
     * Called when the recording starts.
     * Implementations should perform any necessary setup or initialization here.
     */
    void onStart();

    /**
     * Updates the recorder with player and enemy movements during gameplay.
     *
     * @param playerMovement      The movement vector representing the player's movement.
     * @param enemyMovementMap   A mapping of enemies to their respective movement vectors.
     */
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap);

    /**
     * Called when the recording is being stopped or destroyed.
     * Implementations should perform any cleanup or resource release here.
     */
    void onDestroy();
}
