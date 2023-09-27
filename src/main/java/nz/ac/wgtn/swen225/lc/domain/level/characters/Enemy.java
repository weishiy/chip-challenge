package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

public abstract class Enemy extends Character {

    public Enemy(Vector2D position) {
        this(null, position);
    }

    public Enemy(Level level, Vector2D position) {
        super(level, position);
    }

    public abstract Vector2D nextMove();

    @Override
    public void setPosition(Vector2D position) {
        var oldPosition = getPosition();
        super.setPosition(position);
        getGame().fire(new EnemyMovedEvent(this, oldPosition, getPosition()));
    }
}
