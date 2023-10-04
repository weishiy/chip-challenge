package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.DockUnlockedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.KeyConsumedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.LockedDoor;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class LockedDoorTest {

    private LockedDoor toTest;
    private Level mockLevel;
    private Vector2D mockPosition;
    private Key.Color mockColor;
    private Game mockGame;
    private Player mockPlayer;

    @BeforeEach
    public void before() {
        mockLevel = mock(Level.class);
        mockPosition = mock(Vector2D.class);
        mockColor = mock(Key.Color.class);
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);

        toTest = new LockedDoor(mockPosition, mockColor);
        toTest.setLevel(mockLevel);
        when(mockLevel.getTiles()).thenReturn(Set.of(toTest));
    }

    @Test
    public void whenPlayerHasTheRightKey_shouldAllowPlayerToEnter() {
        when(mockPlayer.getKeys()).thenReturn(Set.of(new Key(mockColor)));
        assertTrue(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void whenPlayerDoesNotHaveTheRightKey_shouldNotAllowPlayerToEnter() {
        when(mockPlayer.getKeys()).thenReturn(Set.of());
        assertFalse(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void testOnEnter() {
        when(mockPlayer.getKeys()).thenReturn(Set.of(new Key(mockColor)));
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.onEnter(mockPlayer);
        verify(mockPlayer).removeKey(any(Key.class));
        verify(mockGame).fire(any(KeyConsumedEvent.class));
        verify(mockLevel).removeTile(eq(toTest));
        verify(mockGame).fire(any(DockUnlockedEvent.class));
        assertNull(toTest.getLevel());
    }
}
