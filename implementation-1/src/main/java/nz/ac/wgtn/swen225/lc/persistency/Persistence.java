package nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.recorder.Playback;

import java.io.File;
import java.util.List;

public interface Persistence {

    List<Integer> getAllLevelNos();
    Game loadGame(int levelNo);
    Game loadGame(File save);
    void saveGame(File save, Game game);

    Playback loadPlayback(File save);
    void savePlayback(File save, Playback playback);

}
