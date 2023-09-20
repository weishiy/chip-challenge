package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ExitLockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class ExitLock extends Tile {

    public ExitLock(Vector2D position) {
        super(position);
    }

    @Override
    public boolean isEnterable(Player player) {
        return getLevel().getTiles().stream().noneMatch(t -> t instanceof ChipTile);
    }

    @Override
    public void onEnter(Player player) {
        getLevel().removeTile(this);
        getGame().fire(new ExitLockUnlockedEvent(this, player));
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
