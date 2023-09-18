package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Player extends Character {

    private final Set<Key> keys = new HashSet<>();
    private final Set<Chip> chips = new HashSet<>();

    public Player(Vector2D position) {
        this(position, Collections.emptySet(), Collections.emptySet());
    }

    public Player(Vector2D position, Set<Key> keys, Set<Chip> chips) {
        super(position);
        this.keys.addAll(keys);
        this.chips.addAll(chips);
    }

    public Set<Key> getKeys() {
        return Set.copyOf(keys);
    }

    public void addKey(Key key) {
        keys.add(key);
    }

    public void removeKey(Key key) {
        keys.remove(key);
    }

    public Set<Chip> getChips() {
        return Set.copyOf(chips);
    }

    public void addChip(Chip chip) {
        chips.add(chip);
    }

    public void removeChip(Chip chip) {
        chips.remove(chip);
    }

}
