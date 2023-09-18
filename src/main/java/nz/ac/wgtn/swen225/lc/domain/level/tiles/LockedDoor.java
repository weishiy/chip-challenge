package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

public final class LockedDoor extends Tile {
    private final Key.Color color;

    public LockedDoor(Vector2D position, Key.Color color) {
        super(position);
        this.color = color;
    }

    public Key.Color getColor() {
        return color;
    }
}
