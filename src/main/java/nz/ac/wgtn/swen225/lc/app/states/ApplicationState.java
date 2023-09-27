package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.GameEngine;

public interface ApplicationState {
    void onStateEnter();
    void onNewGame(int levelNo);
    GameEngine onNewGameInDebugMode(int levelNo);
    void onLoadGame();
    void onPauseGame();
    void onExitPause();
    void onSaveAndExitGame();
    void onExitGame();
    void onLoadReplay();
    void onStateExit();

}
