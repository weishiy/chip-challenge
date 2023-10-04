package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Wall;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WallTest {
    @InjectMocks
    private Wall toTest;
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
        assertFalse(toTest.isEnterable(mockPlayer));
    }

    @Test
    public void testOnEnter() {
        assertThrows(IllegalStateException.class, () -> toTest.onEnter(mockPlayer));
    }

    @Test
    public void testOnExit() {
        assertThrows(IllegalStateException.class, () -> toTest.onExit(mockPlayer));
    }
}
