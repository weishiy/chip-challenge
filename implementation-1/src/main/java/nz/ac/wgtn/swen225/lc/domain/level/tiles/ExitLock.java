package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ExitLockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents an exit lock tile
 */
public final class ExitLock extends Tile {

    public ExitLock(Vector2D position) {
        this(null, position);
    }

    public ExitLock(Level level, Vector2D position) {
        super(level, position);
    }

    @Override
    public boolean isEnterable(Player player) {
        return getLevel().getTiles().stream().noneMatch(t -> t instanceof ChipTile);
    }

    @Override
    public void onEnter(Player player) {
        if (!isEnterable(player)) {
            throw new IllegalStateException("Illegal movement!");
        }

        // remove this tile from the level
        getLevel().removeTile(this);
        getGame().fire(new ExitLockUnlockedEvent(this, player));
        setLevel(null);
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
