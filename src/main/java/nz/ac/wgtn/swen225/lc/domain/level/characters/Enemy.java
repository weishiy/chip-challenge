package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * A parent class for Enemy. All enemies should extend this class and provide an implementation of {@link #nextMove()}
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public abstract class Enemy extends Character {
    /**
     * Constructs an enemy object with the given position.
     * @param position the position of enemy
     */
    public Enemy(Vector2D position) {
        super(position);
    }


    /**
     * Constructs an enemy object with the given ID and position.
     *
     * @param id The unique identifier for the enemy.
     * @param position The position of the enemy.
     */
    public Enemy(int id, Vector2D position) {
        super(id, position);
    }

    /**
     * Different enemies can have different implementations. e.g. Patroller patrols along a fixed route, Wanderer moves
     * randomly.
     * <p>
     * @return the moment for enemy in the next tick. Returns Vector2D.Zero if enemy doesn't want to move in the next
     *         tick
     */
    public abstract Vector2D nextMove();

    /**
     * Moves the enemy to a new position and fires {@link EnemyMovedEvent}
     * <p>
     * @param position
     */
    @Override
    public void setPosition(Vector2D position) {
        var oldPosition = getPosition();
        super.setPosition(position);
        getGame().fire(new EnemyMovedEvent(this, oldPosition, getPosition()));
    }
}
