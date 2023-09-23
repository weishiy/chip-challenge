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

public class PlaybackState extends AbstractApplicationState implements GameEventListener {

    private final GameEngine gameEngine;
    private final Replayer replayer;
    private final Game game;

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

    @Override
    public void onStateEnter() {
        gameEngine.onStart();
        replayer.onStart();
        game.addListener(this);
    }

    @Override
    public void onStateExit() {
        game.removeListener(this);
        replayer.onDestroy();
        gameEngine.onDestroy();
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent g) {
            getApplication().setApplicationState(new GameOverState(getApplication(), g instanceof PlayerWonEvent));
        }
    }

}
