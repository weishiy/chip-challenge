package nz.ac.wgtn.swen225.lc.domain.level;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Level extends Entity implements Serializable {

    private final int levelNo;
    private final int width;
    private final int height;
    private final int timeoutInSeconds;

    private final Set<Tile> tiles = new HashSet<>();
    private final Set<Enemy> enemies = new HashSet<>();
    private Player player;

    private Game game;

    public Level(int levelNo, int width, int height, int timeoutInSeconds) {
        super();
        this.levelNo = levelNo;
        this.width = width;
        this.height = height;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public Set<Tile> getTiles() {
        return Set.copyOf(tiles);
    }

    private Tile getTile(Vector2D position) {
        return tiles.stream().filter(t -> Objects.equals(t.getPosition(), position)).findAny().orElse(null);
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
        tile.setLevel(this);
    }

    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    public Set<Enemy> getEnemies() {
        return Set.copyOf(enemies);
    }

    public Map<Integer, Enemy> getEnemiesAsMap() {
        return enemies.stream().collect(Collectors.toMap(Entity::getId, e -> e));
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        enemy.setLevel(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setLevel(this);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void movePlayer(Vector2D movement) {
        if (movement != null && movement != Vector2D.ZERO) {
            var oldPosition = player.getPosition();
            var newPosition = oldPosition.add(movement);

            if (newPosition.x() < 0 || newPosition.x() >= width ||
                    newPosition.y() < 0 || newPosition.y() >= width) {
                throw new IllegalArgumentException("Player went outside the board");
            }

            var oldTile = getTile(oldPosition);
            var newTile = getTile(newPosition);
            if (newTile == null || newTile.isEnterable(player)) {
                if (oldTile != null) {
                    oldTile.onExit(player);
                }
                player.setPosition(newPosition);
                if (newTile != null) {
                    newTile.onEnter(player);
                }
            }
        }
    }

    public void move(Enemy enemy, Vector2D movement) {
        if (movement != null && movement != Vector2D.ZERO) {
            var newPosition = enemy.getPosition().add(movement);
            if (newPosition.x() < 0 || newPosition.x() >= width ||
                    newPosition.y() < 0 || newPosition.y() >= width) {
                throw new IllegalArgumentException("Enemy went outside the board");
            }
            enemy.setPosition(newPosition);
        }
    }

}
