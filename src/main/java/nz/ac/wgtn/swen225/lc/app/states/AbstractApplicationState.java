package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;

import javax.swing.*;

/**
 * The `AbstractApplicationState` class provides a base implementation for application states.
 * Subclasses can inherit common behavior and override specific methods as needed.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public abstract class AbstractApplicationState implements ApplicationState {

    private final Application application;

    /**
     * Constructs an `AbstractApplicationState` object.
     *
     * @param application The game application instance.
     */
    public AbstractApplicationState(Application application) {
        this.application = application;
    }

    /**
     * Gets the game application instance associated with this state.
     *
     * @return The game application instance.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateEnter() {
        // Default implementation: do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewGame(int levelNo) {
        var game = application.getPersistence().loadGame(levelNo);
        application.setApplicationState(new PlayingState(application, game));
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPauseGame() {
        // Default implementation: do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExitPause() {
        // Default implementation: do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveAndExitGame() {
        // Default implementation: do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExitGame() {
        // Default implementation: do nothing
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public GameEngine onNewGameInDebugMode(int levelNo) {
        var game = application.getPersistence().loadGame(levelNo);
        application.setApplicationState(new DebuggingState(application, game));
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateExit() {
        // Default implementation: do nothing
    }
}
