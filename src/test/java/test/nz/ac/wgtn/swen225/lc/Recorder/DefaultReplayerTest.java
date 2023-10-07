package test.nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.levels.level1.Patroller;
import nz.ac.wgtn.swen225.lc.recorder.DefaultReplayer;
import nz.ac.wgtn.swen225.lc.recorder.Moment;
import nz.ac.wgtn.swen225.lc.recorder.Playback;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

public class DefaultReplayerTest {

    @Test
    public void test() {
        Playback playback = new Playback();
        Patroller patroller = new Patroller(2, new Vector2D(3, 3), Collections.emptyList(), 10);
        playback.addMovement(new Moment(10, Vector2D.LEFT, Map.of(patroller, Vector2D.RIGHT)));
        playback.setEndTickNo(20);
        GameEngine engine = Mockito.mock(GameEngine.class);
        DefaultReplayer defaultReplayer = new DefaultReplayer(engine, playback);
        Mockito.when(engine.getTickNo()).thenReturn(10);
        defaultReplayer.update();
        ArgumentCaptor<Vector2D> c1 = ArgumentCaptor.forClass(Vector2D.class);
        ArgumentCaptor<Map> c2 = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(engine).update(c1.capture(), c2.capture());
        Assertions.assertEquals(Vector2D.LEFT, c1.getValue());
        Assertions.assertEquals(2, ((Enemy) c2.getValue().keySet().stream().findFirst().get()).getId());
        Assertions.assertEquals(Vector2D.RIGHT, c2.getValue().values().stream().findFirst().get());
    }

}
