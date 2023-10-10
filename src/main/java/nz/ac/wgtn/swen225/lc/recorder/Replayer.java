package nz.ac.wgtn.swen225.lc.recorder;

/**
 * The Replayer interface defines methods for starting and destroying a replayer.
 * Implementations of this interface can be used to record and replay different actions
 * or events in the program.
 *
 * @author Sajja Syed 300551462
 */
public interface Replayer {

    /**
     * Called when the replayer is started.
     * Implementations should perform any necessary setup or initialization here.
     */
    void onStart();

    /**
     * Called when the replayer is being destroyed or stopped.
     * Implementations should perform any cleanup or resource release here.
     */
    void onDestroy();
}
