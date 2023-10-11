package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.app.GameEngineImpl;
import nz.ac.wgtn.swen225.lc.domain.Game;

/**
 * The `DebuggingState` class represents the state of the game in debugging mode.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class DebuggingState implements ApplicationState {

    private final GameEngine gameEngine;

    /**
     * Constructs a `DebuggingState` object.
     *
     * @param application The game application instance.
     * @param game        The current game being played in debugging mode.
     */
    public DebuggingState(Application application, Game game) {
        this.gameEngine = new GameEngineImpl(
                game,
                application.getMainPanel(),
                application.getLevelNoLabel(),
                application.getTimeLabel(),
                application.getChipsLeftLabel());
    }

    @Override
    public void onStateEnter() {
        gameEngine.onStart();
    }

    @Override
    public void onNewGame(int level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onLoadGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPauseGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onExitPause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onSaveAndExitGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onExitGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onLoadReplay() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onStateExit() {
        gameEngine.onDestroy();
    }

    @Override
    public GameEngine onNewGameInDebugMode(int levelNo) {
        throw new UnsupportedOperationException();
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

}
