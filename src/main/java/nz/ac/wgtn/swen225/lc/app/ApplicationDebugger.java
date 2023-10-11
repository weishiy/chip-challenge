package nz.ac.wgtn.swen225.lc.app;

/**
 * The ApplicationDebugger interface defines a contract for creating a new game in debug mode.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public interface ApplicationDebugger {

    /**
     * Creates a new game in debug mode for the specified level.
     *
     * @param levelNo The level number for the new game.
     * @return A GameEngine instance representing the new game in debug mode.
     */
    GameEngine newGameInDebugMode(int levelNo);
}
