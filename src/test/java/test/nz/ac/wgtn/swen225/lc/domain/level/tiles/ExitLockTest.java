package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.ExitLockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ExitLock;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExitLockTest {

    private ExitLock toTest;
    private Level mockLevel;
    private Vector2D mockPosition;
    private Game mockGame;
    private Player mockPlayer;

    @BeforeEach
    public void before() {
        mockLevel = mock(Level.class);
        mockPosition = mock(Vector2D.class);
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);

        toTest = new ExitLock(mockPosition);
        toTest.setLevel(mockLevel);
    }

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
