package nz.ac.wgtn.swen225.lc.domain;

import nz.ac.wgtn.swen225.lc.domain.events.*;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ChipTile;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Game extends Entity {

    public static final int FRAME_RATE = 10;

    private Level level;
    private int tickNo = 0;
    private boolean gameOver = false;

    private transient List<GameEventListener> listeners = new ArrayList<>();
    private transient List<GameEventListener> listenersToAdd = new ArrayList<>();
    private transient List<GameEventListener> listenersToRemove = new ArrayList<>();

    public Game() {
        super();
    }

    public void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        if (gameOver) {
            throw new IllegalStateException("Game is over");
        }

        // null check and handling
        if (playerMovement == null) {
            playerMovement = Vector2D.ZERO;
        }
        if (enemyMovementMap == null) {
            enemyMovementMap = Map.of();
        }
        var emm = enemyMovementMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue() != null ? e.getValue() : Vector2D.ZERO));

        // update player position and enemy position if possible
        level.movePlayer(playerMovement);
        emm.forEach(level::move);
        if (level.getEnemies().stream().anyMatch(e -> e.getPosition().equals(level.getPlayer().getPosition()))) {
            fire(new PlayerDiedEvent(level.getPlayer()));
        }

        // update counters
        tickNo++;
        if (tickNo % FRAME_RATE == 0) {
            fire(new CountDownEvent(getCountDown()));
        }
        fire(new TickEvent(tickNo));
        if (getCountDown() <= 0) {
            fire(new TimeoutEvent());
        }

        // To avoid java.util.ConcurrentModificationException
        listeners.removeAll(listenersToRemove);
        listenersToRemove.clear();
        listeners.addAll(listenersToAdd);
        listenersToAdd.clear();
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

    public List<GameEventListener> getListeners() {
        return List.copyOf(listeners);
    }

    public static Game deepCopyof(Game game) {
        // make a deep copy with serialization
        try (var bos = new ByteArrayOutputStream()) {
            try (var oos = new ObjectOutputStream(bos)) {
                oos.writeObject(game);
                oos.flush();

                var byteData = bos.toByteArray();
                var bais = new ByteArrayInputStream(byteData);
                return (Game) new ObjectInputStream(bais).readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // As these lists are transient, we need to create them manually.
        this.listeners = new ArrayList<>();
        this.listenersToAdd = new ArrayList<>();
        this.listenersToRemove = new ArrayList<>();
    }
}
