package nz.ac.wgtn.swen225.lc.persistency;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.levels.level2.Patroller;
import nz.ac.wgtn.swen225.lc.recorder.Moment;
import nz.ac.wgtn.swen225.lc.recorder.Playback;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class FileBasedPersistenceImpl implements Persistence {

     @Override
    public List<Integer> getAllLevelNos() {
        return List.of(1, 2);
    }

    @Override
    public Game loadGame(int levelNo) {
        Game game = new Game();
        game.setLevel(toLevel(read(getLevelJsonFile(levelNo))));
        return game;
    }

    private File getLevelJsonFile(int levelNo) {
        try {
            String classpathUri = Objects.requireNonNull(FileBasedPersistenceImpl.class.getResource("/")).getFile();
            String classPath = new URI(classpathUri).getPath();
            String levelJsonFile = classPath + "/levels/level" + levelNo + ".json";
            return new File(levelJsonFile);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game loadGame(File save) {
        return toGame(read(save));
    }

    @Override
    public Playback loadPlayback(File save) {
        return toPlayback(read(save));
    }

    private JsonObject read(File save) {
        try (var reader = new BufferedReader(new FileReader(save))) {
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Playback toPlayback(JsonObject fromJson) {
        var playback = new Playback();
        playback.setSince(toGame(fromJson.getAsJsonObject("since")));
        toMoments(fromJson.getAsJsonArray("moments"), playback.getSince()).forEach(playback::addMovement);
        playback.setEndTickNo(fromJson.get("endTickNo").getAsInt());
        return playback;
    }

    private Game toGame(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        int tickNo = fromJson.get("tickNo").getAsInt();
        Level level = toLevel(fromJson.getAsJsonObject("level"));
        return new Game(id, tickNo, level);
    }

    private Level toLevel(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        int levelNo = fromJson.get("levelNo").getAsInt();
        int width = fromJson.get("width").getAsInt();
        int height = fromJson.get("height").getAsInt();
        int timeoutInSeconds = fromJson.get("timeoutInSeconds").getAsInt();
        Set<Tile> tiles = toTiles(fromJson.getAsJsonArray("tiles"));
        Set<Enemy> enemies = toEnemies(fromJson.getAsJsonArray("enemies"));
        Player player = toPlayer(fromJson.getAsJsonObject("player"));
        return new Level(id, levelNo, width, height, timeoutInSeconds, tiles, enemies, player);
    }

    private Player toPlayer(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        Vector2D position = toVector2D(fromJson.getAsJsonObject("position"));
        Set<Key> keys = toKeys(fromJson.getAsJsonArray("keys"));
        Set<Chip> chips = toChips(fromJson.getAsJsonArray("chips"));
        return new Player(id, position, keys, chips);
    }

    private Set<Key> toKeys(JsonArray fromJson) {
        HashSet<Key> keys = new HashSet<Key>();
        fromJson.forEach(e -> keys.add(toKey((JsonObject) e)));
        return keys;
    }

    private Key toKey(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        Key.Color color = Key.Color.valueOf(fromJson.get("color").getAsString());
        return new Key(id, color);
    }

    private Set<Chip> toChips(JsonArray fromJson) {
        HashSet chips = new HashSet<Chip>();
        fromJson.forEach(e -> chips.add(toChip((JsonObject) e)));
        return chips;
    }

    private Chip toChip(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        return new Chip(id);
    }

    private Set<Enemy> toEnemies(JsonArray fromJson) {
        HashSet enemies = new HashSet<Enemy>();
        fromJson.forEach(e -> enemies.add(toEnemy((JsonObject) e)));
        return enemies;
    }

    private Enemy toEnemy(JsonObject jsonObject) {
        int id = jsonObject.get("id").getAsInt();
        Vector2D position = toVector2D(jsonObject.getAsJsonObject("position"));
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "Patroller" -> {
                List<Vector2D> routine = toRoutine(jsonObject.getAsJsonArray("routine"));
                int intervalInTicks = jsonObject.get("intervalInTicks").getAsInt();
                return new Patroller(id, position, routine, intervalInTicks);
            }
            default -> throw new IllegalArgumentException("Unsupported enemy type: " + type);
        }
    }

    private ArrayList<Vector2D> toRoutine(JsonArray e) {
        ArrayList<Vector2D> routine = new ArrayList<Vector2D>();
        e.forEach(e2 -> routine.add(toVector2D(e2.getAsJsonObject())));
        return routine;
    }

    private Set<Tile> toTiles(JsonArray fromJson) {
        HashSet<Tile> tiles = new HashSet<Tile>();
        fromJson.forEach(e -> tiles.add(toTile((JsonObject) e)));
        return tiles;
    }

    private Tile toTile(JsonObject jsonObject) {
        int id = jsonObject.get("id").getAsInt();
        Vector2D position = toVector2D(jsonObject.getAsJsonObject("position"));
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "InfoField" -> {
                var message = jsonObject.get("message").getAsString();
                var active = jsonObject.get("active").getAsBoolean();
                return new InfoField(id, position, message, active);
            }
            case "KeyTile" -> {
                var color = jsonObject.get("color").getAsString();
                return new KeyTile(id, position, new Key(Key.Color.valueOf(color)));
            }
            case "LockedDoor" -> {
                var color = jsonObject.get("color").getAsString();
                return new LockedDoor(id, position, Key.Color.valueOf(color));
            }
            case "ChipTile" -> {
                return new ChipTile(id, position, new Chip());
            }
            case "Exit" -> {
                return new Exit(id, position);
            }
            case "ExitLock" -> {
                return new ExitLock(id, position);
            }
            case "Wall" -> {
                return new Wall(id, position);
            }
            default -> throw new IllegalArgumentException("Unsupported enemy type: " + type);
        }
    }

    private List<Moment> toMoments(JsonArray fromJson, Game game) {
        return fromJson.asList().stream().map(m -> toMoment((JsonObject) m, game)).toList();
    }

    private Moment toMoment(JsonObject fromJson, Game game) {
        int tickNo = fromJson.get("tickNo").getAsInt();
        Vector2D playerMovement = toVector2D(fromJson.getAsJsonObject("playerMovement"));
        Map<Enemy, Vector2D> enemyMovementMap = toEnemyMovementMap(fromJson.getAsJsonObject("enemyMovementMap"), game);
        return new Moment(tickNo, playerMovement, enemyMovementMap);
    }

    private Map<Enemy, Vector2D> toEnemyMovementMap(JsonObject fromJson, Game game) {
        var enemyMap = game.getLevel().getEnemiesAsMap();
        return fromJson.asMap().entrySet().stream().collect(Collectors.toMap(
                e -> enemyMap.get(Integer.parseInt(e.getKey())),
                e -> toVector2D((JsonObject) e.getValue())
        ));
    }

    private Vector2D toVector2D(JsonObject fromJson) {
        var x = fromJson.get("x").getAsInt();
        var y = fromJson.get("y").getAsInt();
        return new Vector2D(x, y);
    }

    @Override
    public void saveGame(File save, Game game) {
        write(save, toJsonObject(game).toString());
    }

    @Override
    public void savePlayback(File save, Playback playback) {
        write(save, toJsonObject(playback).toString());
    }

    private void write(File save, String text) {
        try (var writer = new BufferedWriter(new FileWriter(save))) {
            writer.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonObject toJsonObject(Playback playback) {
        var playbackObject = new JsonObject();
        playbackObject.add("since", toJsonObject(playback.getSince()));
        playbackObject.add("moments", toJsonObjectFromMoments(playback.getMoments()));
        playbackObject.addProperty("endTickNo", playback.getEndTickNo());
        return playbackObject;
    }

    private JsonObject toJsonObject(Game game) {
        JsonObject gameObject = new JsonObject();
        gameObject.addProperty("id", game.getId());
        gameObject.addProperty("tickNo", game.getTickNo());
        gameObject.add("level", toJsonObject(game.getLevel()));
        return gameObject;
    }

    private JsonObject toJsonObject(Level level) {
        JsonObject gameObject = new JsonObject();
        gameObject.addProperty("id", level.getId());
        gameObject.addProperty("levelNo", level.getLevelNo());
        gameObject.addProperty("width", level.getWidth());
        gameObject.addProperty("height", level.getHeight());
        gameObject.addProperty("timeoutInSeconds", level.getTimeoutInSeconds());
        gameObject.add("tiles", toJsonArrayFromTiles(level.getTiles()));
        gameObject.add("enemies", toJsonArrayFromEnemies(level.getEnemies()));
        gameObject.add("player", toJsonObject(level.getPlayer()));
        return gameObject;
    }

    private JsonArray toJsonArrayFromTiles(Set<Tile> tiles) {
        JsonArray jsonArray = new JsonArray();
        tiles.forEach(t -> jsonArray.add(toJsonObject(t)));
        return jsonArray;
    }

    private JsonObject toJsonObject(Tile tile) {
        JsonObject tileObject = new JsonObject();
        tileObject.addProperty("id", tile.getId());
        tileObject.add("position", toJsonObject(tile.getPosition()));
        String type = tile.getClass().getSimpleName();
        tileObject.addProperty("type", type);
        switch (type) {
            case "InfoField" -> {
                tileObject.addProperty("message", ((InfoField) tile).getMessage());
                tileObject.addProperty("active", ((InfoField) tile).isActive());
            }
            case "KeyTile" -> tileObject.addProperty("color", ((KeyTile) tile).getKey().getColor().toString());

            case "LockedDoor" -> tileObject.addProperty("color", ((LockedDoor) tile).getColor().toString());
            case "ChipTile", "Exit", "ExitLock", "Wall" -> {
                // do nothing
            }
            default -> throw new IllegalArgumentException("Unsupported enemy type: " + tile.getClass());
        }
        return tileObject;
    }

    private JsonArray toJsonArrayFromEnemies(Set<Enemy> enemies) {
        JsonArray jsonArray = new JsonArray();
        enemies.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    private JsonObject toJsonObject(Enemy enemy) {
        JsonObject enemyObject = new JsonObject();
        enemyObject.addProperty("id", enemy.getId());
        enemyObject.add("position", toJsonObject(enemy.getPosition()));
        String type = enemy.getClass().getSimpleName();
        enemyObject.addProperty("type", type);
        switch (type) {
            case "Patroller" -> {
                Patroller patroller = (Patroller) enemy;
                enemyObject.add("routine", toJsonArrayFromRoutine(patroller.getRoutine()));
                enemyObject.addProperty("intervalInTicks", patroller.getIntervalInTicks());
            }
            default -> throw new IllegalArgumentException("Unsupported enemy type: " + enemy.getClass());
        }
        return enemyObject;
    }

    private JsonArray toJsonArrayFromRoutine(List<Vector2D> routine) {
        JsonArray jsonArray = new JsonArray();
        routine.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    private JsonObject toJsonObject(Player player) {
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("id", player.getId());
        playerObject.add("position", toJsonObject(player.getPosition()));
        playerObject.add("chips", toJsonArrayFromChips(player.getChips()));
        playerObject.add("keys", toJsonArrayFromKeys(player.getKeys()));
        return playerObject;
    }

    private JsonArray toJsonArrayFromKeys(Set<Key> keys) {
        JsonArray jsonArray = new JsonArray();
        keys.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    private JsonObject toJsonObject(Key key) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", key.getId());
        jsonObject.addProperty("color", key.getColor().toString());
        return jsonObject;
    }

    private JsonArray toJsonArrayFromChips(Set<Chip> chips) {
        JsonArray jsonArray = new JsonArray();
        chips.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    private JsonObject toJsonObject(Chip chip) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", chip.getId());
        return jsonObject;
    }

    private JsonArray toJsonObjectFromMoments(List<Moment> moments) {
        var momentArray = new JsonArray();
        moments.forEach(m -> momentArray.add(toJsonObject(m)));
        return momentArray;
    }

    private JsonObject toJsonObject(Moment moment) {
        var momentObject = new JsonObject();
        momentObject.addProperty("tickNo", moment.tickNo());
        momentObject.add("playerMovement", toJsonObject(moment.playerMovement()));
        momentObject.add("enemyMovementMap", toJsonObjectFromEnemyMovementMap(moment.enemyMovementMap()));
        return momentObject;
    }

    private JsonObject toJsonObjectFromEnemyMovementMap(Map<Enemy, Vector2D> enemyMovementMap) {
        var enemyMovementMapObject = new JsonObject();
        enemyMovementMap.forEach((k, v) -> enemyMovementMapObject.add(Integer.toString(k.getId()), toJsonObject(v)));
        return enemyMovementMapObject;
    }

    private JsonObject toJsonObject(Vector2D movement) {
        var movementObject = new JsonObject();
        movementObject.addProperty("x", movement.x());
        movementObject.addProperty("y", movement.y());
        return movementObject;
    }

}

