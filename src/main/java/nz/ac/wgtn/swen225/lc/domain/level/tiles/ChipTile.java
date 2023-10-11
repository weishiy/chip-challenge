package nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.events.ChipPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Presents a chip tile
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class ChipTile extends Tile {
    private final Chip chip;
    /**
     * Constructs a ChipTile object with the specified position and chip.
     *
     * @param position The position of the tile.
     * @param chip The chip contained within the tile.
     */
    public ChipTile(Vector2D position, Chip chip) {
        super(position);
        this.chip = chip;
    }
    /**
     * Constructs a ChipTile object with the specified ID, position, and chip.
     *
     * @param id The unique identifier for the tile.
     * @param position The position of the tile.
     * @param chip The chip contained within the tile.
     */
    public ChipTile(int id, Vector2D position, Chip chip) {
        super(id, position);
        this.chip = chip;
    }
    /**
     * Determines whether the player can enter this tile.
     *
     * @param player The player character attempting to enter the tile.
     * @return True, indicating that the tile is enterable by the player.
     * @throws IllegalStateException if the tile is not associated with a valid level.
     */
    @Override
    public boolean isEnterable(Player player) {
        if (getLevel() == null || !getLevel().getTiles().contains(this)) {
            throw new IllegalStateException("Stale tile being used!");
        }
        return true;
    }
    /**
     * Actions to be taken when the player enters this tile.
     *
     * When the player enters this tile, the chip is picked up by the player. The tile is removed
     * from the level and a ChipPickedUpEvent is fired.
     *
     * @param player The player character entering the tile.
     * @throws IllegalStateException if the tile is not associated with a valid level.
     */
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
    /**
     * Actions to be taken when the player exits this tile.
     *
     * For ChipTiles, no specific action is taken when the player exits.
     *
     * @param player The player character exiting the tile.
     */
    @Override
    public void onExit(Player player) {
        // do nothing
    }
    /**
     * Get the chip contained within this tile.
     *
     * @return The chip object within this tile.
     */
    public Chip getChip() {
        return chip;
    }
}
