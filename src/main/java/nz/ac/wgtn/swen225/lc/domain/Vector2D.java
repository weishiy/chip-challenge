package nz.ac.wgtn.swen225.lc.domain;

import java.io.Serializable;

public record Vector2D(int x, int y) implements Serializable {

    public static final Vector2D ZERO = new Vector2D(0, 0); // To reduce null checks in the game
    public static final Vector2D LEFT = new Vector2D(-1, 0);
    public static final Vector2D UP = new Vector2D(0, -1);
    public static final Vector2D RIGHT = new Vector2D(1, 0);
    public static final Vector2D DOWN = new Vector2D(0, 1);

    public Vector2D add(Vector2D another) {
        return new Vector2D(x + another.x, y + another.y);
    }

    public Vector2D subtract(Vector2D another) {
        return new Vector2D(x - another.x, y - another.y);
    }
}
