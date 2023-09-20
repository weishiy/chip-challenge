package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

public abstract class Enemy extends Character {

    public Enemy(Vector2D position) {
        super(position);
    }

    public abstract Vector2D nextMove();

    @Override
    public void setPosition(Vector2D position) {
        var oldPosition = getPosition();
        super.setPosition(position);
        getGame().fire(new EnemyMovedEvent(this, oldPosition, getPosition()));
    }
}
