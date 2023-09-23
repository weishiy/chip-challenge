package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.app.GameEngineImpl;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.events.GameOverEvent;
import nz.ac.wgtn.swen225.lc.domain.events.PlayerWonEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.recorder.DefaultRecorder;
import nz.ac.wgtn.swen225.lc.recorder.Recorder;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.stream.Collectors;

public class PlayingState extends AbstractApplicationState implements GameEventListener {

    private final Game game;
    private final GameEngine gameEngine;
    private final Recorder recorder;
    private final Timer timer;

    private Vector2D playerMovement = Vector2D.ZERO;

    public PlayingState(Application application, Game game) {
        super(application);
        this.game = game;
        this.gameEngine = new GameEngineImpl(
                game,
                application.getMainPanel(),
                application.getLevelNoLabel(),
                application.getTimeLabel(),
                application.getChipsLeftLabel());
        recorder = new DefaultRecorder(getApplication().getPersistence(), game);
        timer = new Timer(1000 / Game.FRAME_RATE, e -> update());
    }

    @Override
    public void onStateEnter() {
        gameEngine.onStart();
        recorder.onStart();
        game.addListener(this);
        bindKeyStrokes();

        timer.start();
    }

    @Override
    public void onPauseGame() {
        timer.stop();
    }

    @Override
    public void onExitPause() {
        timer.start();
    }

    @Override
    public void onSaveAndExitGame() {
        timer.stop();

        var fileChooser = new JFileChooser();
        var result = fileChooser.showSaveDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION -> {
                getApplication().getPersistence().saveGame(fileChooser.getSelectedFile(), game);
                getApplication().setApplicationState(new WelcomingState(getApplication()));
            }
            case JFileChooser.CANCEL_OPTION -> timer.start();
        }
    }

    @Override
    public void onExitGame() {
        timer.stop();
        getApplication().setApplicationState(new WelcomingState(getApplication()));
    }

    @Override
    public void onStateExit() {
        timer.stop();

        unbindKeyStrokes();
        game.removeListener(this);
        recorder.onDestroy();
        gameEngine.onDestroy();
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent g) {
            getApplication().setApplicationState(new GameOverState(getApplication(), g instanceof PlayerWonEvent));
        }
    }

    private void update() {
        var enemyMovementMap =
                game.getLevel()
                        .getEnemies()
                        .stream()
                        .collect(Collectors.toMap(e -> e, Enemy::nextMove));
        recorder.update(playerMovement, enemyMovementMap);
        gameEngine.update(playerMovement, enemyMovementMap);
        playerMovement = Vector2D.ZERO;
    }

    private void bindKeyStrokes() {
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                e -> playerMovement = Vector2D.LEFT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                e -> playerMovement = Vector2D.UP);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                e -> playerMovement = Vector2D.RIGHT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                e -> playerMovement = Vector2D.DOWN);
    }

    private void unbindKeyStrokes() {
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    }

}
