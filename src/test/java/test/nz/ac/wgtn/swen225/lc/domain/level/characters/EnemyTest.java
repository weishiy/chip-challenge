package test.nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.EnemyMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EnemyTest {
    @Test
    public void testSetPosition() {
        var mockLevel = mock(Level.class);
        var mockGame = mock(Game.class);
        var toTest = new Enemy(mockLevel, Vector2D.ZERO) {
            @Override
            public Vector2D nextMove() {
                return null;
            }
        };
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.setPosition(Vector2D.ZERO);
        verify(mockGame).fire(any(EnemyMovedEvent.class));
    }
}
