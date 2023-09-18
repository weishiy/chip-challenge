package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import java.util.Map;

public interface Recorder {
    void onStart();
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap);
    void onDestroy();
}
