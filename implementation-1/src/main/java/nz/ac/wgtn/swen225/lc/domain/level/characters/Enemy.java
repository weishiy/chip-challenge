package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * A parent class for Enemy. All enemies should extend this class and provide an implementation of {@link #nextMove()}
 */
public abstract class Enemy extends Character {

    public Enemy(Vector2D position) {
        this(null, position);
    }

    public Enemy(Level level, Vector2D position) {
        super(level, position);
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
