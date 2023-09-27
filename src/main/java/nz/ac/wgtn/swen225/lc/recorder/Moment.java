package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import java.io.Serializable;
import java.util.Map;

public record Moment(int tickNo, Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) implements Serializable {

    public Moment(int tickNo, Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        this.tickNo = tickNo;
        this.playerMovement = playerMovement;
        this.enemyMovementMap = Map.copyOf(enemyMovementMap);
    }

}
