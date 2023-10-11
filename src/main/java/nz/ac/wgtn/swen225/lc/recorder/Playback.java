package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Playback class represents a recorded playback of moments within a game.
 * It stores a snapshot of the game state at the beginning, a list of moments
 * representing movements and actions during the playback, and an end tick number.
 *
 * @author Sajja Syed 300551462
 */
public class Playback implements Serializable {

    private Game since;  // A snapshot of a game
    private final List<Moment> moments = new ArrayList<>();
    private int endTickNo;

    /**
     * Get the snapshot of the game state at the beginning of the playback.
     *
     * @return The game state snapshot.
     */
    public Game getSince() {
        return since;
    }

    /**
     * Set the snapshot of the game state at the beginning of the playback.
     *
     * @param since The game state snapshot.
     */
    public void setSince(Game since) {
        this.since = since;
    }

    /**
     * Get the list of recorded moments representing movements and actions during the playback.
     *
     * @return A list of recorded moments.
     */
    public List<Moment> getMoments() {
        return List.copyOf(moments);
    }

    /**
     * Add a recorded moment to the list of moments.
     *
     * @param moment The recorded moment to add.
     */
    public void addMovement(Moment moment) {
        moments.add(moment);
    }

    /**
     * Get the end tick number of the playback.
     *
     * @return The end tick number.
     */
    public int getEndTickNo() {
        return endTickNo;
    }

    /**
     * Set the end tick number of the playback.
     *
     * @param endTickNo The end tick number.
     */
    public void setEndTickNo(int endTickNo) {
        this.endTickNo = endTickNo;
    }
}
