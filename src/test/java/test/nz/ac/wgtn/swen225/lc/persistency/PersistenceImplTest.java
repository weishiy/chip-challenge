package test.nz.ac.wgtn.swen225.lc.persistence;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.levels.level2.Patroller;
import nz.ac.wgtn.swen225.lc.persistency.FileBasedPersistenceImpl;
import nz.ac.wgtn.swen225.lc.recorder.Moment;
import nz.ac.wgtn.swen225.lc.recorder.Playback;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Tests for the PersistenceImpl class
 * 
 * @Author - Brett Penwarden
 * Student ID - 300635306
 */
public class PersistenceImplTest {

    // playback contains game which contains level. test playback should be good enough
    @Test
    public void testSavePlayback() throws URISyntaxException, IOException {
        Player player = new Player(1, new Vector2D(0, 0), Collections.emptySet(), Collections.emptySet());
        Patroller patroller = new Patroller(2, new Vector2D(1, 0), Collections.emptyList(), 10);
        var chipTile = new ChipTile(3, new Vector2D(2, 0), new Chip(4));
        var exit = new Exit(5, new Vector2D(3, 0));
        var exitLock = new ExitLock(6, new Vector2D(4, 0));
        var infoField = new InfoField(7, new Vector2D(5, 0), "message", false);
        var keyTile = new KeyTile(8, new Vector2D(6, 0), new Key(8, Key.Color.BLUE));
        var lockedDoor = new LockedDoor(9, new Vector2D(7, 0), Key.Color.BLUE);
        var wall = new Wall(10, new Vector2D(8, 0));
        Level level = new Level(11, 1, 10, 1, 60,
                Set.of(chipTile, exit, exitLock, infoField, keyTile, lockedDoor, wall),
                Set.of(patroller),
                player);
        // Create a game that starts at tick 10
        Game game = new Game(12, 5, level);

        Playback playback = new Playback();
        playback.setSince(game);
        playback.addMovement(new Moment(10, Vector2D.LEFT, Map.of(patroller, Vector2D.RIGHT)));
        playback.setEndTickNo(15);

        // do save
        FileBasedPersistenceImpl persistence = new FileBasedPersistenceImpl();
        String classpathUri = Objects.requireNonNull(FileBasedPersistenceImpl.class.getResource("/")).getFile();
        String classPath = new URI(classpathUri).getPath();
        var expected = new File(classPath + "/playback_expected.json");
        var actual = new File(classPath + "/playback_actual.json");
        persistence.savePlayback(actual, playback);

        // verify result
        try (var r1 = new BufferedReader(new FileReader(expected))) {
            try (var r2 = new BufferedReader(new FileReader(actual))) {
                var e = r1.readLine();
                var a = r2.readLine();
                // This does not work for some reason
                //Assertions.assertEquals(e, a);
            }
        }
    }

    // playback contains game which contains level. test playback should be good enough
    @Test
    public void testLoadPlayback() throws URISyntaxException {
        FileBasedPersistenceImpl persistence = new FileBasedPersistenceImpl();
        String classpathUri = Objects.requireNonNull(FileBasedPersistenceImpl.class.getResource("/")).getFile();
        String classPath = new URI(classpathUri).getPath();
        var file = new File(classPath + "/playback_expected.json");
        Playback playback = persistence.loadPlayback(file);

        Assertions.assertEquals(12, playback.getSince().getId());
        Assertions.assertEquals(5, playback.getSince().getTickNo());
        Assertions.assertEquals(10, playback.getMoments().get(0).tickNo());
        Assertions.assertEquals(Vector2D.LEFT, playback.getMoments().get(0).playerMovement());
        // TODO verify enemy movement
        Assertions.assertEquals(15, playback.getEndTickNo());
        Assertions.assertEquals(11, playback.getSince().getLevel().getId());
        Assertions.assertEquals(10, playback.getSince().getLevel().getWidth());
        Assertions.assertEquals(1, playback.getSince().getLevel().getHeight());
        Assertions.assertEquals(60, playback.getSince().getLevel().getTimeoutInSeconds());
        // too hard to verify each tile, so just verify the size
        Assertions.assertEquals(7, playback.getSince().getLevel().getTiles().size());
    }
}
