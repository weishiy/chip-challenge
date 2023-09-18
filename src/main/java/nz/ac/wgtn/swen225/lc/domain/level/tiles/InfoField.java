package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class InfoField extends Tile {
    private String message;
    private boolean active = false;

    public InfoField(Vector2D position) {
        super(position);
    }

    public String getMessage() {
        return message;
    }

    public boolean isActive() {
        return active;
    }

}
