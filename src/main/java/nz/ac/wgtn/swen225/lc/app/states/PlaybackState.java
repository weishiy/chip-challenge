package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.app.GameEngineImpl;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.events.GameOverEvent;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.recorder.DefaultReplayer;
import nz.ac.wgtn.swen225.lc.recorder.Playback;
import nz.ac.wgtn.swen225.lc.recorder.Replayer;

/**
 * The `PlaybackState` class represents the state of the game when playing a recorded playback.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class PlaybackState extends AbstractApplicationState implements GameEventListener {

    private final GameEngine gameEngine;
    private final Replayer replayer;
    private final Game game;

    /**
     * Constructs a `PlaybackState` object.
     *
     * @param application The game application instance.
     * @param playback    The recorded gameplay to be played back.
     */
    public PlaybackState(Application application, Playback playback) {
        super(application);
        game = playback.getSince();
        this.gameEngine = new GameEngineImpl(
                game,
                application.getMainPanel(),
                application.getLevelNoLabel(),
                application.getTimeLabel(),
                application.getChipsLeftLabel());
        this.replayer = new DefaultReplayer(gameEngine, playback);
    }

    /**
     * Called when this state is entered. Starts the game engine and replayer and adds a listener for game events.
     */
    @Override
    public void onStateEnter() {
        gameEngine.onStart();
        replayer.onStart();
        game.addListener(this);
    }

    /**
     * Called when this state is exited. Removes the listener for game events, stops the replayer,
     * and cleans up the game engine.
     */
    @Override
    public void onStateExit() {
        game.removeListener(this);
        replayer.onDestroy();
        gameEngine.onDestroy();
    }

    /**
     * Handles game events, such as game over events.
     *
     * @param gameEvent The game event to handle.
     */
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent g) {
            // Transition to the GameOverState when a game over event occurs
            getApplication().setApplicationState(new GameOverState(getApplication(), g instanceof PlayerWonEvent));
        }
    }
}