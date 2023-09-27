package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.KeyPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
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

@ExtendWith(MockitoExtension.class)
public class KeyTileTest {
    @InjectMocks
    private KeyTile toTest;
    @Mock
    private Level mockLevel;
    @Mock
    private Vector2D mockPosition;
    @Mock
    private Key mockKey;

    @Mock
    private Game mockGame;
    @Mock
    private Player mockPlayer;

    @BeforeEach
    public void before() {
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
