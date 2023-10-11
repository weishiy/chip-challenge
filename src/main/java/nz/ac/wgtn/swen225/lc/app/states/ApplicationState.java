package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.GameEngine;

/**
 * The `ApplicationState` interface defines the common methods that all application states should implement.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public interface ApplicationState {

    /**
     * Called when entering the state. Performs initialization tasks for the state.
     */
    void onStateEnter();

    /**
     * Starts a new game with the specified level number.
     *
     * @param levelNo The level number to start a new game.
     */
    void onNewGame(int levelNo);

    /**
     * Starts a new game in debugging mode with the specified level number.
     *
     * @param levelNo The level number to start a new game in debugging mode.
     * @return The game engine for the new game in debugging mode.
     */
    GameEngine onNewGameInDebugMode(int levelNo);

    /**
     * Loads a saved game.
     */
    void onLoadGame();

    /**
     * Pauses the current game.
     */
    void onPauseGame();

    /**
     * Exits the pause state and resumes the game.
     */
    void onExitPause();

    /**
     * Saves the current game state and exits the game.
     */
    void onSaveAndExitGame();

    /**
     * Exits the current game without saving.
     */
    void onExitGame();

    /**
     * Loads a replay of a previous game session.
     */
    void onLoadReplay();

    /**
     * Called when exiting the state. Performs cleanup tasks for the state.
     */
    void onStateExit();
}
