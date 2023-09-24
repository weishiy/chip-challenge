package nz.ac.wgtn.swen225.lc.levels.level1;

import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Patroller extends Enemy {

    private final Queue<Vector2D> routine = new LinkedList<>();
    private final int intervalInTicks;

    public Patroller(Vector2D position, List<Vector2D> routine, int intervalInTicks) {
        super(position);
        this.routine.addAll(routine);
        this.intervalInTicks = intervalInTicks;
    }

    @Override
    public Vector2D nextMove() {
        if (getGame().getTickNo() % intervalInTicks == 0) {
            var nextMove = routine.remove();
            routine.add(nextMove);
            return nextMove;
        } else {
            return Vector2D.ZERO;
        }
    }

}