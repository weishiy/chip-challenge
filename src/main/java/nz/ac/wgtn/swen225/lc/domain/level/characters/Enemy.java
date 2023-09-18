package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;

public abstract class Enemy extends Character {

    public Enemy(Vector2D position) {
        super(position);
    }

    public abstract Vector2D nextMove();

}
