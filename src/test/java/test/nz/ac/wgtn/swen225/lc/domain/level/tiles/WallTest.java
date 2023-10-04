package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.LockedDoor;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Wall;
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
import static org.mockito.Mockito.mock;

public class WallTest {

    private Wall toTest;
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

        toTest = new Wall(mockPosition);
        toTest.setLevel(mockLevel);
        when(mockLevel.getTiles()).thenReturn(Set.of(toTest));
    }

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
