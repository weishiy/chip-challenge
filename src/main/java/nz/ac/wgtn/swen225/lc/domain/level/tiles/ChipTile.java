package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;

public final class ChipTile extends Tile {
    private final Chip chip;

    public ChipTile(Vector2D position, Chip chip) {
        super(position);
        this.chip = chip;
    }

    @Override
    public boolean isEnterable(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEnter(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onExit(Player player) {
        throw new UnsupportedOperationException();
    }

    public Chip getChip() {
        return chip;
    }
}
