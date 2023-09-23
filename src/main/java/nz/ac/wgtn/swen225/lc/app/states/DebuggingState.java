package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.app.GameEngineImpl;
import nz.ac.wgtn.swen225.lc.domain.Game;

public class DebuggingState implements ApplicationState {

    private final GameEngine gameEngine;

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
