package nz.ac.wgtn.swen225.lc.utils;

import java.io.Serializable;

/**
 * Presents a 2D vector
 * @param x
 * @param y
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record Vector2D(int x, int y) implements Serializable {

    public static final Vector2D ZERO = new Vector2D(0, 0); // To reduce null checks in the game
    public static final Vector2D LEFT = new Vector2D(-1, 0);
    public static final Vector2D UP = new Vector2D(0, -1);
    public static final Vector2D RIGHT = new Vector2D(1, 0);
    public static final Vector2D DOWN = new Vector2D(0, 1);

    /**
     * Adds another vector to this vector, resulting in a new vector.
     *
     * @param another The vector to add to this vector.
     * @return A new vector that is the result of the addition.
     */
    public Vector2D add(Vector2D another) {
        return new Vector2D(x + another.x, y + another.y);
    }

    /**
     * Subtracts another vector from this vector, resulting in a new vector.
     *
     * @param another The vector to subtract from this vector.
     * @return A new vector that is the result of the subtraction.
     */
    public Vector2D subtract(Vector2D another) {
        return new Vector2D(x - another.x, y - another.y);
    }
}
