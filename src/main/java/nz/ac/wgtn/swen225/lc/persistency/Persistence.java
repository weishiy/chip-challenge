package nz.ac.wgtn.swen225.lc.persistency;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.recorder.Playback;

import java.io.File;
import java.util.List;

/**
 * Persistence interface 
 * 
 * Persistence is responsible for serializing java objects into json texts and saving them in disk, vice versa.
 *  
 * @Author - Brett Penwarden
 * Student ID - 300635306
 */
public interface Persistence {
    List<Integer> getAllLevelNos(); // returns all level numbers in a list
    Game loadGame(int levelNo); // Creates a new Game object at a provided level and returns it
    Game loadGame(File save);  // Loads a previous game object (in json format), deserializes it back toa game object, and returns the latter
    void saveGame(File save, Game game);  // Serialize the game object into a json text and save the text to disk

    Playback loadPlayback(File save);  // Load a previously saved Playback object (in json format)
    void savePlayback(File save, Playback playback); // Serialize the Playback object into a json text and save the text to disk
}
