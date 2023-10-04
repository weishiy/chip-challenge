package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a key tile
 */
public final class KeyTile extends Tile {

    private final Key key;

    public KeyTile(Vector2D position, Key key) {
        super(position);
        this.key = key;
    }

    public KeyTile(int id, Vector2D position, Key key) {
        super(id, position);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public boolean isEnterable(Player player) {
        if (getLevel() == null || !getLevel().getTiles().contains(this)) {
            throw new IllegalStateException("Stale tile being used!");
        }
        return true;
    }

    @Override
    public void onEnter(Player player) {
        if (getLevel() == null || !getLevel().getTiles().contains(this)) {
            throw new IllegalStateException("Stale tile being used!");
        }

        player.addKey(key);
        getLevel().removeTile(this);
        getGame().fire(new KeyPickedUpEvent(this, player));
        setLevel(null);
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
