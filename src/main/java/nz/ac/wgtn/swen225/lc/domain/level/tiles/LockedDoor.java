package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.DockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class LockedDoor extends Tile {
    private final Key.Color color;

    public LockedDoor(Vector2D position, Key.Color color) {
        super(position);
        this.color = color;
    }

    public Key.Color getColor() {
        return color;
    }

    @Override
    public boolean isEnterable(Player player) {
        return player.getKeys().stream().anyMatch(k -> k.getColor() == color);
    }

    public void onEnter(Player player) {
        if (!isEnterable(player)) {
            throw new IllegalStateException("Illegal movement!");
        }

        var matchingKey = player.getKeys().stream().filter(k -> k.getColor() == color).findAny().orElse(null);
        assert matchingKey != null;
        player.removeKey(matchingKey);
        // remove this tile from the level
        getLevel().removeTile(this);
        setLevel(null);

        getGame().fire(new DockUnlockedEvent(this, player));
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }
}
