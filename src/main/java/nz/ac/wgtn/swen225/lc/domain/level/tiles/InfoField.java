package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldPressedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldReleasedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

/**
 * Presents an info field tile
 */
public final class InfoField extends Tile {
    private final String message;
    private boolean active = false;

    public InfoField(Vector2D position, String message) {
        this(null, position, message);
    }

    public InfoField(Level level, Vector2D position, String message) {
        super(level, position);
        this.message = message;
    }

    @Override
    public boolean isEnterable(Player player) {
        return true;
    }

    @Override
    public void onEnter(Player player) {
        active = true;
        getGame().fire(new InfoFieldPressedEvent(this, player));
    }

    @Override
    public void onExit(Player player) {
        active = false;
        getGame().fire(new InfoFieldReleasedEvent(this, player));
    }

    public String getMessage() {
        return message;
    }

    public boolean isActive() {
        return active;
    }

}
