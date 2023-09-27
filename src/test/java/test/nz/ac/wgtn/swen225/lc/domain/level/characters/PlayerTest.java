package test.nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerMovedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

public class PlayerTest {

    @Test
    public void testSetPosition() {
        var mockLevel = mock(Level.class);
        var mockGame = mock(Game.class);
        var toTest = new Player(mockLevel, Vector2D.ZERO, Set.of(), Set.of());
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.setPosition(Vector2D.ZERO);
        verify(mockGame).fire(any(PlayerMovedEvent.class));
    }

}
