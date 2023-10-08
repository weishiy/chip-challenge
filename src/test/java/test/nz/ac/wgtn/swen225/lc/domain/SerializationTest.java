package test.nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.levels.level2.Patroller;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTest {

    @Test
    void testDeepCopyOf() {
        var game = createInstance();
        var copyOfGame = Game.deepCopyOf(game);
        assertNotSame(game, copyOfGame);
        assertEquals(game.getId(), copyOfGame.getId());
        assertEquals(game.getTickNo(), copyOfGame.getTickNo());
        assertTrue(copyOfGame.getListeners().isEmpty());          // Ensure listeners are not copied

        var level = game.getLevel();
        var copyOfLevel = copyOfGame.getLevel();
        assertNotSame(level, copyOfLevel);
        assertEquals(level.getId(), copyOfLevel.getId());
        assertEquals(copyOfGame, copyOfLevel.getGame());    // Ensure copyOfLevel.getGame() is referencing copyOfGame
        assertEquals(level.getLevelNo(), copyOfLevel.getLevelNo());
        assertEquals(level.getWidth(), copyOfLevel.getWidth());
        assertEquals(level.getHeight(), copyOfLevel.getHeight());
        assertEquals(level.getTimeoutInSeconds(), copyOfLevel.getTimeoutInSeconds());

        var tiles = level.getTiles();
        var copyOfTiles = copyOfLevel.getTiles();
        assertEquals(tiles.size(), copyOfTiles.size());
        tiles.forEach(t -> {
                    var matching = copyOfTiles.stream().filter(
                            t2 -> t.getId() == t2.getId()).findFirst().orElse(null);
                    assertNotNull(matching);
                    assertNotSame(t, matching);
                    assertEquals(t.getClass(), matching.getClass());
                    assertEquals(copyOfLevel, matching.getLevel());
                    assertEquals(t.getPosition(), matching.getPosition());
                    if (t instanceof ChipTile c) {
                        var chip = c.getChip();
                        var copyOfChip = ((ChipTile) matching).getChip();
                        assertNotSame(chip, copyOfChip);
                    } else if (t instanceof InfoField i) {
                        var copyOfInfoField = (InfoField) matching;
                        assertEquals(copyOfInfoField.getMessage(), copyOfInfoField.getMessage());
                        assertEquals(copyOfInfoField.isActive(), copyOfInfoField.isActive());
                    } else if (t instanceof KeyTile k) {
                        var key = k.getKey();
                        var copyOfKey = ((KeyTile) matching).getKey();
                        assertNotEquals(key, copyOfKey);
                        assertEquals(key.getColor(), copyOfKey.getColor());
                    } else if (t instanceof LockedDoor l) {
                        assertEquals(l.getColor(), ((LockedDoor) matching).getColor());
                    }
                }
        );

        var enemies = level.getEnemies();
        var copyOfEnemies = copyOfLevel.getEnemies();
        assertEquals(enemies.size(), copyOfEnemies.size());
        enemies.forEach(e -> {
            var matching = copyOfEnemies.stream().filter(
                    e2 -> e.getId() == e2.getId()).findFirst().orElse(null);
            assertNotNull(matching);
            assertNotSame(e, matching);
            assertEquals(e.getClass(), matching.getClass());
            assertEquals(copyOfLevel, matching.getLevel());
            assertEquals(e.getPosition(), matching.getPosition());
            // well we can't proceed further as implementations of Enemy is in different jars.
        });

        var player = level.getPlayer();
        var copyOfPlayer = copyOfLevel.getPlayer();
        assertNotSame(player, copyOfPlayer);
        assertEquals(player.getId(), copyOfPlayer.getId());
        assertEquals(copyOfLevel, copyOfPlayer.getLevel());
        assertEquals(player.getPosition(), copyOfPlayer.getPosition());
        assertEquals(player.getKeys().size(), copyOfPlayer.getKeys().size());
        assertEquals(player.getChips().size(), copyOfPlayer.getChips().size());
    }

    Game createInstance() {
        var level = new Level(1, 9, 9, 60);

        // map
        // row 0
        IntStream.range(2, 7).forEach(i -> level.addTile(new Wall(new Vector2D(i, 0))));
        // row 1
        level.addTile(new Wall(new Vector2D(2, 1)));
        level.addTile(new KeyTile(new Vector2D(5, 1), new Key(Key.Color.RED)));
        level.addTile(new Wall(new Vector2D(6, 1)));
        // row 2
        IntStream.range(0, 9).filter(i -> i != 3).forEach(i -> level.addTile(new Wall(new Vector2D(i, 2))));
        level.addTile(new InfoField(new Vector2D(3, 2), "A tip"));
        // row 3
        level.addTile(new Wall(new Vector2D(0, 3)));
        level.addTile(new Wall(new Vector2D(2, 3)));
        level.addTile(new Wall(new Vector2D(8, 3)));
        // row 4
        level.addTile(new Wall(new Vector2D(0, 4)));
        level.addTile(new Wall(new Vector2D(2, 4)));
        level.addTile(new Wall(new Vector2D(6, 4)));
        level.addTile(new Wall(new Vector2D(8, 4)));
        // row 5
        level.addTile(new Wall(new Vector2D(0, 5)));
        level.addTile(new LockedDoor(new Vector2D(2, 5), Key.Color.RED));
        level.addTile(new Wall(new Vector2D(6, 5)));
        level.addTile(new ChipTile(new Vector2D(7, 5), new Chip()));
        level.addTile(new Wall(new Vector2D(8, 5)));
        // row 6
        IntStream.range(0, 9).filter(i -> i != 5).forEach(i -> level.addTile(new Wall(new Vector2D(i, 6))));
        level.addTile(new ExitLock(new Vector2D(5, 6)));
        // row 7
        level.addTile(new Wall(new Vector2D(2, 7)));
        level.addTile(new Exit(new Vector2D(3, 7)));
        level.addTile(new Wall(new Vector2D(6, 7)));
        // row 8
        IntStream.range(2, 7).forEach(i -> level.addTile(new Wall(new Vector2D(i, 8))));

        // enemies
        level.addEnemy(new Patroller(new Vector2D(4, 3),
                List.of(Vector2D.RIGHT, Vector2D.DOWN, Vector2D.DOWN, Vector2D.LEFT, Vector2D.LEFT, Vector2D.UP, Vector2D.UP, Vector2D.RIGHT),
                10));
        level.addEnemy(new Patroller(new Vector2D(4, 5),
                List.of(Vector2D.LEFT, Vector2D.UP, Vector2D.UP, Vector2D.RIGHT, Vector2D.RIGHT, Vector2D.DOWN, Vector2D.DOWN, Vector2D.LEFT),
                10));

        // player
        var player = new Player(new Vector2D(4, 4));
        player.addKey(new Key(Key.Color.YELLOW));
        player.addChip(new Chip());
        level.setPlayer(player);

        var game = new Game();
        game.setLevel(level);
        game.addListener(gameEvent -> {
            // do nothing
        });
        return game;
    }

}
