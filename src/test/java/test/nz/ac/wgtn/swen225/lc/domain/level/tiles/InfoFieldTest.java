package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldPressedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.InfoFieldReleasedEvent;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Exit;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InfoFieldTest {
    @InjectMocks
    private InfoField toTest;
    @Mock
    private Level mockLevel;
    @Mock
    private Vector2D mockPosition;

    @Mock
    private Game mockGame;
    @Mock
    private Player mockPlayer;

    @Test
    public void testIsEnterable() {
        assertTrue(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void testOnEnter() {
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.onEnter(mockPlayer);
        assertTrue(toTest.isActive());
        verify(mockGame).fire(any(InfoFieldPressedEvent.class));
    }

    @Test
    public void testOnExit() {
        when(mockLevel.getGame()).thenReturn(mockGame);
        toTest.onExit(mockPlayer);
        assertFalse(toTest.isActive());
        verify(mockGame).fire(any(InfoFieldReleasedEvent.class));
    }
}
