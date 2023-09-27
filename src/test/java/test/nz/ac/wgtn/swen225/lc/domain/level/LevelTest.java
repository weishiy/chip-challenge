package test.nz.ac.wgtn.swen225.lc.domain.level;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LevelTest {

    private Level toTest;
    private Game mockGame;
    private Player mockPlayer;
    private Enemy mockEnemy;
    private Tile mockOldTile;
    private Tile mockNewTile;

    @BeforeEach
    public void before() {
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);
        mockEnemy = mock(Enemy.class);
        mockOldTile = mock(Tile.class);
        mockNewTile = mock(Tile.class);
        toTest = new Level(1, 100, 100, 100);
        toTest.setGame(mockGame);
        toTest.setPlayer(mockPlayer);
        toTest.addEnemy(mockEnemy);
        toTest.addTile(mockOldTile);
        toTest.addTile(mockNewTile);
    }

    @Test
    public void shouldNotMoveCharacterOutOfBoundary() {
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockPlayer.getPosition()).thenReturn(Vector2D.ZERO);
            toTest.movePlayer(Vector2D.LEFT);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockPlayer.getPosition()).thenReturn(Vector2D.ZERO);
            toTest.movePlayer(Vector2D.UP);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockPlayer.getPosition()).thenReturn(new Vector2D(99, 99));
            toTest.movePlayer(Vector2D.RIGHT);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockPlayer.getPosition()).thenReturn(new Vector2D(99, 99));
            toTest.movePlayer(Vector2D.DOWN);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            when(mockEnemy.getPosition()).thenReturn(Vector2D.ZERO);
            toTest.move(mockEnemy, Vector2D.LEFT);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockEnemy.getPosition()).thenReturn(Vector2D.ZERO);
            toTest.move(mockEnemy, Vector2D.UP);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockEnemy.getPosition()).thenReturn(new Vector2D(99, 99));
            toTest.move(mockEnemy, Vector2D.RIGHT);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockEnemy.getPosition()).thenReturn(new Vector2D(99, 99));
            toTest.move(mockEnemy, Vector2D.DOWN);
        });
    }

    @Test
    public void testMovePlayer() {
        when(mockPlayer.getPosition()).thenReturn(new Vector2D(10, 10));
        when(mockOldTile.getPosition()).thenReturn(new Vector2D(10, 10));
        when(mockNewTile.getPosition()).thenReturn(new Vector2D(9, 10));
        when(mockNewTile.isEnterable(any())).thenReturn(true);
        toTest.movePlayer(Vector2D.LEFT);
        verify(mockOldTile).onExit(eq(mockPlayer));
        verify(mockPlayer).setPosition(new Vector2D(9, 10));
        verify(mockNewTile).onEnter(eq(mockPlayer));
    }

    @Test
    public void testMovePlayerWithZeroVector() {
        when(mockPlayer.getPosition()).thenReturn(Vector2D.ZERO);
        when(mockOldTile.getPosition()).thenReturn(Vector2D.ZERO);
        toTest.movePlayer(Vector2D.ZERO);
        verify(mockOldTile, never()).onExit(any());
        verify(mockPlayer, never()).setPosition(any());
        verify(mockOldTile, never()).onEnter(any());
    }

    @Test
    public void testMoveEnemy() {
        when(mockEnemy.getPosition()).thenReturn(new Vector2D(10, 10));
        toTest.move(mockEnemy, Vector2D.RIGHT);
        verify(mockEnemy).setPosition(new Vector2D(11, 10));
    }

    @Test
    public void testMoveEnemyWithZeroVector() {
        toTest.move(mockEnemy, Vector2D.ZERO);
        verify(mockEnemy, never()).setPosition(any());
    }

}
