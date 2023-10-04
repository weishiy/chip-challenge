package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playback implements Serializable {

    private Game since;  // A snapshot of a game
    private final List<Moment> moments = new ArrayList<>();
    private int endTickNo;

    public Game getSince() {
        return since;
    }

    public void setSince(Game since) {
        this.since = since;
    }

    public List<Moment> getMoments() {
        return List.copyOf(moments);
    }

    public void addMovement(Moment moment) {
        moments.add(moment);
    }

    public int getEndTickNo() {
        return endTickNo;
    }

    public void setEndTickNo(int endTickNo) {
        this.endTickNo = endTickNo;
    }

}
