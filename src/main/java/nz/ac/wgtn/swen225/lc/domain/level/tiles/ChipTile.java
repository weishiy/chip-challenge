package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ChipsPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public final class ChipTile extends Tile {
    private final Chip chip;

    public ChipTile(Vector2D position, Chip chip) {
        super(position);
        this.chip = chip;
    }

    @Override
    public boolean isEnterable(Player player) {
        return true;
    }

    @Override
    public void onEnter(Player player) {
        player.addChip(chip);
        getLevel().removeTile(this);
        getGame().fire(new ChipsPickedUpEvent(this, player));
    }

    @Override
    public void onExit(Player player) {
        // do nothing
    }

    public Chip getChip() {
        return chip;
    }
}
