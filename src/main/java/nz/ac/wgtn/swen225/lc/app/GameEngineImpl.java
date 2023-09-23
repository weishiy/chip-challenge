package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.ChipsPickedUpEvent;
import nz.ac.wgtn.swen225.lc.domain.events.CountDownEvent;
import nz.ac.wgtn.swen225.lc.domain.events.GameEvent;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.renderer.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameEngineImpl implements GameEngine {

    private final Game game;

    private final JComponent anchorPane;
    private final Component gameWindow;
    private final InputMap inputMap;
    private final ActionMap actionMap;
    private final Set<KeyStroke> keyStrokesBound = new HashSet<>();

    private final JLabel levelNoLabel;
    private final JLabel timeLabel;
    private final JLabel chipsLeftLabel;

    public GameEngineImpl(Game game, JComponent anchorPane, JLabel levelNoLabel, JLabel timeLabel, JLabel chipsLeftLabel) {
        this.game = game;

        this.anchorPane = anchorPane;
        this.gameWindow = new GameWindow(game, 600);
        this.inputMap = anchorPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.actionMap = anchorPane.getActionMap();

        this.levelNoLabel = levelNoLabel;
        this.timeLabel = timeLabel;
        this.chipsLeftLabel = chipsLeftLabel;
    }

    @Override
    public void onStart() {
        game.addListener(this);
        anchorPane.add(gameWindow);
        gameWindow.setEnabled(true);

        this.levelNoLabel.setText(Integer.toString(game.getLevel().getLevelNo()));
        this.timeLabel.setText(Integer.toString(game.getCountDown()));
        this.chipsLeftLabel.setText(Integer.toString(game.getChipsLeft()));
    }

    @Override
    public boolean isGameOver() {
        return game.isGameOver();
    }

    @Override
    public int getTickNo() {
        return game.getTickNo();
    }

    @Override
    public void update(Vector2D playerMovement) {
        var enemyMovementMap =
                game.getLevel()
                        .getEnemies()
                        .stream()
                        .collect(Collectors.toMap(e -> e, Enemy::nextMove));
        update(playerMovement, enemyMovementMap);
    }

    @Override
    public void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        game.update(playerMovement, enemyMovementMap);
    }

    @Override
    public void onDestroy() {
        this.levelNoLabel.setText(Integer.toString(game.getLevel().getLevelNo()));
        this.timeLabel.setText(Integer.toString(game.getCountDown()));
        this.chipsLeftLabel.setText(Integer.toString(game.getChipsLeft()));

        keyStrokesBound.forEach(ks -> {
            actionMap.remove(ks.toString());
            inputMap.remove(ks);
        });
        keyStrokesBound.clear();

        gameWindow.setEnabled(false);
        anchorPane.remove(gameWindow);
        game.removeListener(this);
    }

    @Override
    public void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback) {
        inputMap.put(keyStroke, keyStroke.toString());
        actionMap.put(keyStroke.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.accept(e);
            }
        });

        keyStrokesBound.add(keyStroke);
    }

    @Override
    public void unbindInputWithAction(KeyStroke keyStroke) {
        actionMap.remove(keyStroke.toString());
        inputMap.remove(keyStroke);

        keyStrokesBound.remove(keyStroke);
    }

    @Override
    public Container getGlassPane() {
        return null;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof CountDownEvent) {
            timeLabel.setText(Integer.toString(game.getCountDown()));
        } else if (gameEvent instanceof ChipsPickedUpEvent) {
            chipsLeftLabel.setText(Integer.toString(game.getChipsLeft()));
        }
    }

}
