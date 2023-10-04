package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.ExitLockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ExitLock;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExitLockTest {
    @InjectMocks
    private ExitLock toTest;
    @Mock
    private Level mockLevel;
    @Mock
    private Vector2D mockPosition;

    @Mock
    private Game mockGame;
    @Mock
    private Player mockPlayer;

    @Test
    public void whenNoChipsLeft_shouldAllowPlayerToEnter() {
        when(mockLevel.getTiles()).thenReturn(Set.of());
        assertTrue(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void whenHasChipsLeft_shouldNotAllowPlayerToEnter() {
        when(mockLevel.getTiles()).thenReturn(Set.of(mock(ChipTile.class)));
        assertFalse(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void testOnEnter() {
        when(mockLevel.getTiles()).thenReturn(Set.of());
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.onEnter(mockPlayer);
        verify(mockLevel).removeTile(eq(toTest));
        verify(mockGame).fire(any(ExitLockUnlockedEvent.class));
        assertNull(toTest.getLevel());
    }
}
