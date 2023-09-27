package test.nz.ac.wgtn.swen225.lc.domain.level.tiles;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.ChipPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChipTileTest {
    @InjectMocks
    private ChipTile toTest;
    @Mock
    private Level mockLevel;
    @Mock
    private Vector2D mockPosition;
    @Mock
    private Chip mockChip;

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
        verify(mockPlayer).addChip(any(Chip.class));
        verify(mockLevel).removeTile(eq(toTest));
        verify(mockGame).fire(any(ChipPickedUpEvent.class));
        assertNull(toTest.getLevel());
    }

}
