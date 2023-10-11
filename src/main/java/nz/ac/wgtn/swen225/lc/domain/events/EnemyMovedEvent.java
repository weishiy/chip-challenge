package nz.ac.wgtn.swen225.lc.domain.events;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

/**
 * Fires when position of an enemy changed
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public record EnemyMovedEvent(Enemy enemy, Vector2D from, Vector2D to) implements GameEvent {
}
