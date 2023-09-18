package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.events.*;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game extends Entity implements Serializable {

    public static final int FRAME_RATE = 10;

    private Level level;
    private int tickNo = 0;
    private boolean gameOver = false;

    private transient List<GameEventListener> listeners = new ArrayList<>();

    public Game() {
        super();
    }

    public void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        throw new UnsupportedOperationException();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.level.setGame(this);
    }

    public int getTickNo() {
        return tickNo;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void addListener(GameEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void fire(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent) {
            gameOver = true;
        }
        listeners.forEach(l -> l.onGameEvent(gameEvent));
    }

    public int getCountDown() {
        return getLevel().getTimeoutInSeconds() - tickNo / FRAME_RATE;
    }

    public int getChipsLeft() {
        return (int) getLevel().getTiles().stream().filter(t -> t instanceof ChipTile).count();
    }

}
