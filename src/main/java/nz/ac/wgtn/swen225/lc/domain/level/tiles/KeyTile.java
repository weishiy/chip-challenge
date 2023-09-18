package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

public final class KeyTile extends Tile {

    private final Key key;

    public KeyTile(Vector2D position, Key key) {
        super(position);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

}
