package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import java.io.Serializable;
import java.util.Map;

/**
 * The `Moment` class represents a recorded moment in a game playback.
 * It includes the tick number, player movement, and enemy movement data.
 *
 * @param tickNo            The tick number indicating when the moment was recorded.
 * @param playerMovement    The movement vector representing the player's movement at the moment.
 * @param enemyMovementMap  A mapping of enemies to their respective movement vectors at the moment.
 *
 * @author Sajja Syed 300551462
 */
public record Moment(int tickNo, Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) implements Serializable {

    /**
     * Constructs a `Moment` object with the given parameters.
     *
     * @param tickNo            The tick number indicating when the moment was recorded.
     * @param playerMovement    The movement vector representing the player's movement at the moment.
     * @param enemyMovementMap  A mapping of enemies to their respective movement vectors at the moment.
     */
    public Moment(int tickNo, Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        this.tickNo = tickNo;
        this.playerMovement = playerMovement;
        this.enemyMovementMap = Map.copyOf(enemyMovementMap);
    }
}
