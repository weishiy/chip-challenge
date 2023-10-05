package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.KeyTile;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class KeyTileTest {

    private KeyTile toTest;
    private Level mockLevel;
    private Vector2D mockPosition;
    private Key mockKey;
    private Game mockGame;
    private Player mockPlayer;

    @BeforeEach
    public void before() {
        mockLevel = mock(Level.class);
        mockPosition = mock(Vector2D.class);
        mockKey = mock(Key.class);
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);

        toTest = new KeyTile(mockPosition, mockKey);
        toTest.setLevel(mockLevel);
        when(mockLevel.getTiles()).thenReturn(Set.of(toTest));
    }

    @Test
    public void testIsEnterable() {
        assertTrue(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void testOnEnter() {
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.onEnter(mockPlayer);
        verify(mockPlayer).addKey(any(Key.class));
        verify(mockLevel).removeTile(eq(toTest));
        verify(mockGame).fire(any(KeyPickedUpEvent.class));
        assertNull(toTest.getLevel());
    }

}
