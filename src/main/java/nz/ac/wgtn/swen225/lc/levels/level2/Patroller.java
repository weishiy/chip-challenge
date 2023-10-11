package nz.ac.wgtn.swen225.lc.levels.level2;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Present an enemy that patrols around a route
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Patroller extends Enemy {

    private final Queue<Vector2D> routine = new LinkedList<>();
    private final int intervalInTicks;
    /**
     * Constructs a Patroller with the given position, patrol routine, and interval.
     *
     * @param position The initial position of the Patroller.
     * @param routine A list of waypoints that define the patrol route.
     * @param intervalInTicks The interval (in game ticks) at which the Patroller moves to the next waypoint.
     */
    public Patroller(Vector2D position, List<Vector2D> routine, int intervalInTicks) {
        super(position);
        this.routine.addAll(routine);
        this.intervalInTicks = intervalInTicks;
    }
    /**
     * Constructs a Patroller with the given ID, position, patrol routine, and interval.
     *
     * @param id The unique identifier for the Patroller.
     * @param position The initial position of the Patroller.
     * @param routine A list of waypoints that define the patrol route.
     * @param intervalInTicks The interval (in game ticks) at which the Patroller moves to the next waypoint.
     */
    public Patroller(int id, Vector2D position, List<Vector2D> routine, int intervalInTicks) {
        super(id, position);
        this.routine.addAll(routine);
        this.intervalInTicks = intervalInTicks;
    }

    /**
     * Determines the next move for the Patroller based on its predefined routine and interval.
     * The Patroller moves to the next waypoint in the routine when the specified interval is reached.
     *
     * @return The vector representing the next move of the Patroller.
     */
    @Override
    public Vector2D nextMove() {
        if (getGame().getTickNo() % intervalInTicks == 0) {
            var nextMove = routine.remove();
            routine.add(nextMove);
            return nextMove;
        } else {
            return Vector2D.ZERO;
        }
    }

    /**
     * Gets the predefined patrol routine of the Patroller.
     *
     * @return A list of waypoints defining the patrol route.
     */
    public List<Vector2D> getRoutine() {
        return List.copyOf(routine);
    }

    /**
     * Gets the interval (in game ticks) at which the Patroller moves to the next waypoint.
     *
     * @return The interval in game ticks.
     */
    public int getIntervalInTicks() {
        return intervalInTicks;
    }
}