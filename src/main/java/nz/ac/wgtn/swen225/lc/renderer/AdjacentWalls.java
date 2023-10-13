package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.util.Set;
import java.util.function.Function;

/**
 * Records which walls are adjacent to a given position.
 *
 * @param wallAbove If a wall is above this position.
 * @param wallBelow If a wall is below this position.
 * @param wallLeft  If a wall is left of this position.
 * @param wallRight If a wall is right of this position.
 *
 * @author Jeremy Kanal-Scott 300624019
 */
public record AdjacentWalls(boolean wallAbove, boolean wallBelow, boolean wallLeft,
                            boolean wallRight) {
    /**
     * Calculates the adjacency of walls to a position.
     *
     * @param wallPositions Set detailing where walls are.
     * @param position      Where to judge adjacency from.
     * @return <code>AdjacentWalls</code> instance detailing adjacency.
     */
    public static AdjacentWalls calculateAdjacentWalls(final Set<Vector2D> wallPositions,
                                                       final Vector2D position) {
        final Function<Vector2D, Boolean> isWallPresentFromOffset =
                offset -> wallPositions.contains(position.add(offset));

        boolean wallAbove = isWallPresentFromOffset.apply(Vector2D.UP);
        boolean wallBelow = isWallPresentFromOffset.apply(Vector2D.DOWN);

        boolean wallLeft = isWallPresentFromOffset.apply(Vector2D.LEFT);
        boolean wallRight = isWallPresentFromOffset.apply(Vector2D.RIGHT);

        return new AdjacentWalls(wallAbove, wallBelow, wallLeft, wallRight);
    }

    /**
     * Tells which straight passages through this position are available.
     *
     * @return <code>Passage</code> enum expressing the available route.
     */
    public Passage getPassage() {
        if ((wallAbove && wallBelow) && !(wallLeft || wallRight)) {
            return Passage.HORIZONTAL_PASSAGE;
        } else if (!(wallAbove || wallBelow) && (wallLeft && wallRight)) {
            return Passage.VERTICAL_PASSAGE;
        } else {
            return Passage.NO_PASSAGE;
        }
    }

    /**
     * Straight routes through some position.
     *
     * <p>There must be walls on the sides of the passage, but not on the ends.
     */
    public enum Passage {
        HORIZONTAL_PASSAGE, VERTICAL_PASSAGE, NO_PASSAGE
    }
}
