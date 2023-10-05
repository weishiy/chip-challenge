package nz.ac.wgtn.swen225.lc.domain.events;

/**
 * Listener that handles game events
 */
public interface GameEventListener {

    void onGameEvent(GameEvent gameEvent);

}
