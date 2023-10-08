package test.nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.levels.level2.Patroller;
import nz.ac.wgtn.swen225.lc.persistency.Persistence;
import nz.ac.wgtn.swen225.lc.recorder.DefaultRecorder;
import nz.ac.wgtn.swen225.lc.recorder.Playback;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DefaultRecorderTest {

    @Test
    public void test() {
        Persistence persistence = Mockito.mock(Persistence.class);
        Player player = new Player(1, new Vector2D(6, 6), Collections.emptySet(), Collections.emptySet());
        Patroller patroller = new Patroller(2, new Vector2D(3, 3), Collections.emptyList(), 10);
        Level level = new Level(3, 1, 10, 10, 60, Collections.emptySet(), Set.of(patroller), player);
        // Create a game that starts at tick 10
        Game game = new Game(4, 10, level);
        DefaultRecorder recorder = new DefaultRecorder(persistence, game);

        System.out.println("current tick no is: " + game.getTickNo());
        recorder.update(Vector2D.LEFT, Map.of(patroller, Vector2D.RIGHT));
        game.update(Vector2D.LEFT, Map.of(patroller, Vector2D.RIGHT));
        // 9 empty ticks
        for (int i = 0; i < 9; i++) {
            recorder.update(Vector2D.ZERO, Collections.emptyMap());
            game.update(Vector2D.ZERO, Collections.emptyMap());
        }
        System.out.println("current tick no is: " + game.getTickNo());
        recorder.onDestroy();

        ArgumentCaptor<Playback> captor = ArgumentCaptor.forClass(Playback.class);
        // Verify that persistence.savePlayback(File, Playback) is called once. Also, make Mockito capture the 2nd
        // argument
        Mockito.verify(persistence).savePlayback(Mockito.any(), captor.capture());
        Playback playback = captor.getValue();
        // Verify since is tick 10
        Assertions.assertEquals(10, playback.getSince().getTickNo());
        // Verify that only non ZERO vectors are recorded
        Assertions.assertEquals(1, playback.getMoments().size());
        Vector2D pm = playback.getMoments().get(0).playerMovement();
        Assertions.assertEquals(Vector2D.LEFT, pm);
        Map<Enemy, Vector2D> ems = playback.getMoments().get(0).enemyMovementMap();
        Assertions.assertEquals(2, ems.keySet().stream().findFirst().get().getId());
        Assertions.assertEquals(Vector2D.RIGHT, ems.values().stream().findFirst().get());
    }

}
