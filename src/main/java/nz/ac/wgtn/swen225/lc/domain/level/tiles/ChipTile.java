package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ChipPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a chip tile
 */
public final class ChipTile extends Tile {
    private final Chip chip;

    public ChipTile(Vector2D position, Chip chip) {
        super(position);
        this.chip = chip;
    }

    public ChipTile(int id, Vector2D position, Chip chip) {
        super(id, position);
        this.chip = chip;
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

        player.addChip(chip);

        // remove this tile from the level
        getLevel().removeTile(this);
        getGame().fire(new ChipPickedUpEvent(this, player));
        setLevel(null);
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

    public Chip getChip() {
        return chip;
    }
}
