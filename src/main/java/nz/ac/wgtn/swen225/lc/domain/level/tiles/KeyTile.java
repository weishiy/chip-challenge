package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class KeyTile extends Tile {

    private final Key key;

    public KeyTile(Vector2D position, Key key) {
        super(position);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public boolean isEnterable(Player player) {
        return true;
    }

    @Override
    public void onEnter(Player player) {
        player.addKey(key);
        getLevel().removeTile(this);
        getGame().fire(new KeyPickedUpEvent(this, player));
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

}
