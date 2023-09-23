package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;

import javax.swing.*;

public abstract class AbstractApplicationState implements ApplicationState {

    private final Application application;

    public AbstractApplicationState(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }

    @Override
    public void onStateEnter() {
        // do nothing
    }

    @Override
    public void onNewGame(int levelNo) {
        var game = application.getPersistence().loadGame(levelNo);
        application.setApplicationState(new PlayingState(application, game));
    }

    @Override
    public void onLoadGame() {
        onPauseGame();

        var fileChooser = new JFileChooser();
        var result = fileChooser.showOpenDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION -> {
                var game = application.getPersistence().loadGame(fileChooser.getSelectedFile());
                application.setApplicationState(new PlayingState(application, game));
            }
            case JFileChooser.CANCEL_OPTION -> onExitPause();
        }
    }

    @Override
    public void onPauseGame() {
        // do nothing
    }

    @Override
    public void onExitPause() {
        // do nothing
    }

    @Override
    public void onSaveAndExitGame() {
        // do nothing
    }

    @Override
    public void onExitGame() {
        // do nothing
    }

    @Override
    public void onLoadReplay() {
        onStateExit();

        var fileChooser = new JFileChooser();
        var result = fileChooser.showOpenDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION -> {
                var playback = application.getPersistence().loadPlayback(fileChooser.getSelectedFile());
                application.setApplicationState(new PlaybackState(application, playback));
            }
            case JFileChooser.CANCEL_OPTION -> onExitPause();
        }
    }

    @Override
    public GameEngine onNewGameInDebugMode(int levelNo) {
        var game = application.getPersistence().loadGame(levelNo);
        application.setApplicationState(new DebuggingState(application, game));
        return null;
    }

    @Override
    public void onStateExit() {
        // do nothing
    }

}
