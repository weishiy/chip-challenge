package nz.ac.wgtn.swen225.lc.renderer.assets;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Tracks movements of entities, via <code>GameEventListener</code>.
 *
 * @author Jeremy Kanal-Scott 300624019
 */
public final class MovementTracker implements GameEventListener {
    /**
     * Enemies we know the orientation of.
     */
    private final Map<Enemy, Orientation> enemies = new WeakHashMap<>();
    /**
     * Orientation of the player, possibly null.
     */
    private Orientation playerOrientation;

    /**
     * Constructor.
     *
     * @param game The game to track movements on.
     */
    public MovementTracker(final Game game) {
        if (game != null) {
            game.addListener(this);
        }
    }

    /**
     * Gets the current player orientation, or default.
     *
     * @return The current player orientation.
     */
    public Orientation getPlayerOrientation() {
        return Objects.requireNonNullElse(playerOrientation, Orientation.DEFAULT_ORIENTATION);
    }

    /**
     * Returns the orientation of the given enemy.
     *
     * @param enemy The enemy queried.
     * @return The enemy's orientation.
     */
    public Orientation getEnemyOrientation(final Enemy enemy) {
        return enemies.getOrDefault(enemy, Orientation.DEFAULT_ORIENTATION);
    }

    @Override
    public void onGameEvent(final GameEvent gameEvent) {
        if (gameEvent instanceof PlayerMovedEvent) {
            onPlayerMoved((PlayerMovedEvent) gameEvent);
        } else if (gameEvent instanceof EnemyMovedEvent) {
            onEnemyMoved((EnemyMovedEvent) gameEvent);
        }
    }

    private void onPlayerMoved(final PlayerMovedEvent playerMoved) {
        playerOrientation = Orientation.getOrientation(playerMoved.from(), playerMoved.to());
    }

    private void onEnemyMoved(final EnemyMovedEvent enemyMoved) {
        enemies.put(enemyMoved.enemy(),
                Orientation.getOrientation(enemyMoved.from(), enemyMoved.to()));
    }

    /**
     * Possible orientations on the board.
     */
    public enum Orientation {

        UP, DOWN, LEFT, RIGHT;

        /**
         * Default orientation, when orientation unknown.
         *
         * <p>Most cases, sprite should be facing user.
         */
        private static final Orientation DEFAULT_ORIENTATION = DOWN;

        /**
         * How corresponds change in locations with an orientation.
         */
        private static final Map<Vector2D, Orientation> DIRECTION_MAP = Map.of(Vector2D.UP, UP,
                Vector2D.DOWN, DOWN, Vector2D.LEFT, LEFT, Vector2D.RIGHT, RIGHT);

        private static Orientation getOrientation(final Vector2D from, final Vector2D to) {
            final Vector2D direction = to.subtract(from);
            return DIRECTION_MAP.getOrDefault(direction, DEFAULT_ORIENTATION);
        }
    }
}
