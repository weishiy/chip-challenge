package nz.ac.wgtn.swen225.lc.domain.level.items;

import nz.ac.wgtn.swen225.lc.domain.Entity;

/**
 * Presents a chip, which is used by {@link nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile} and
 * {@link nz.ac.wgtn.swen225.lc.domain.level.characters.Player}
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public final class Chip extends Entity {

    /**
     * Constructs a chip object without a specific ID.
     */
    public Chip() {
        super();
    }


    /**
     * Constructs a chip object without a specific ID.
     */
    public Chip(int id) {
        super(id);
    }
}
