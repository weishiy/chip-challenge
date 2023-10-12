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

/** 
 * FileBasedPersistenceImpl for the "Chips Challenge game"
 * @author - Brett Penwarden
 * Student id - 300635306
 */
public class FileBasedPersistenceImpl implements Persistence {

    /**
     * Retrieves a list of all available level numbers in the game.
     * 
     * @return A list of all level numbers
     */
     @Override
    public List<Integer> getAllLevelNos() {
        return List.of(1, 2);
    }

    /** 
     * Loads a game for the specified level number
     * 
     * @param levelNo The level number for which to load the game
     * @return Game object representing the loaded game
     */
    @Override
    public Game loadGame(int levelNo) {
        Game game = new Game();
        game.setLevel(toLevel(read(getLevelJsonFile(levelNo))));
        return game;
    }

    /** 
     * Returns Json file from the specified level
     * 
     * @param levelNo - the level
     * @return File object
     */
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

    /**
     * Loads game from a saved file
     * 
     * @param save - the saved file
     * @return - Game object from a saved file
     */
    @Override
    public Game loadGame(File save) {
        return toGame(read(save));
    }

    /**
     * Loads a playback from a saved file
     * 
     * @param save the file from which to load the playback
     * @return A playback object representing the loaded playback
     */
    @Override
    public Playback loadPlayback(File save) {
        return toPlayback(read(save));
    }

    /**
     * Reads the contents of a JSON file and returns a JsonObject representation
     * 
     * @param save - The JSON file to be read
     * @return A JsonObject containing the data from the file.
     * @throws - RuntimeException if an IOException occurs during file reading
     */
    private JsonObject read(File save) {
        try (var reader = new BufferedReader(new FileReader(save))) {
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts JsonObject into a Playback Object
     * 
     * @param fromJson The JsonObject to be converted.
     * @return A Playback object created from the JsonObject.
     */
    private Playback toPlayback(JsonObject fromJson) {
        var playback = new Playback();
        playback.setSince(toGame(fromJson.getAsJsonObject("since")));
        toMoments(fromJson.getAsJsonArray("moments"), playback.getSince()).forEach(playback::addMovement);
        playback.setEndTickNo(fromJson.get("endTickNo").getAsInt());
        return playback;
    }

    /**
     * Converts Json object to a Game object.
     * 
     * @param - fromJson the JsonObject to be converted
     * @return - A game object created from the JsonObject
     */
    private Game toGame(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        int tickNo = fromJson.get("tickNo").getAsInt();
        Level level = toLevel(fromJson.getAsJsonObject("level"));
        return new Game(id, tickNo, level);
    }

    /**
     * Converts a Json object into a Level object
     * 
     * @param fromJson - The JsonObjected to be converted
     * @return Level object created from Json Object
     */
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

    /**
     * Converts JsonObject into a Player object
     * 
     * @param fromJson - Json object to convert to Player
     * @return Player based on the JsonObject
     */
    private Player toPlayer(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        Vector2D position = toVector2D(fromJson.getAsJsonObject("position"));
        Set<Key> keys = toKeys(fromJson.getAsJsonArray("keys"));
        Set<Chip> chips = toChips(fromJson.getAsJsonArray("chips"));
        return new Player(id, position, keys, chips);
    }

    /**
     * Converts JsonArray to a Set of Keys
     * 
     * @param fromJson - JsonArray to convert
     * @return Set of Keys
     */
    private Set<Key> toKeys(JsonArray fromJson) {
        HashSet<Key> keys = new HashSet<Key>();
        fromJson.forEach(e -> keys.add(toKey((JsonObject) e)));
        return keys;
    }

    /**
     * Converts JsonObject into Key object
     * 
     * @param fromJson - JsonObject to convert
     * @return Key object based on fromJson
     */
    private Key toKey(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        Key.Color color = Key.Color.valueOf(fromJson.get("color").getAsString());
        return new Key(id, color);
    }

    /**
     * Converts JsonArray into Set of Chips
     * 
     * @param fromJson - Json Array to convert
     * @return Set<Chip>
     */
    private Set<Chip> toChips(JsonArray fromJson) {
        HashSet chips = new HashSet<Chip>();
        fromJson.forEach(e -> chips.add(toChip((JsonObject) e)));
        return chips;
    }

    /**
     * Converts Json Object into Chip object
     * 
     * @param fromJson - JsonObject to convert 
     * @return Chip object based on JsonObject
     */
    private Chip toChip(JsonObject fromJson) {
        int id = fromJson.get("id").getAsInt();
        return new Chip(id);
    }

    /**
     * Converts JsonArray into Set of Enemys
     * 
     * @param fromJson - JsonArray to convert
     * @return Set of Enemy objects
     */
    private Set<Enemy> toEnemies(JsonArray fromJson) {
        HashSet enemies = new HashSet<Enemy>();
        fromJson.forEach(e -> enemies.add(toEnemy((JsonObject) e)));
        return enemies;
    }

    /**
     * Convert single JsonObject into Enemy object
     * 
     * @param fromJson - JsonObject to convert
     * @return Enemy object based on JsonObject
     */
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

    /**
     * Converts JsonArray into a List of Vector 2D objects
     * 
     * @param e - JsonArray
     * @return ArrayList<Vector2D>
     */
    private ArrayList<Vector2D> toRoutine(JsonArray e) {
        ArrayList<Vector2D> routine = new ArrayList<Vector2D>();
        e.forEach(e2 -> routine.add(toVector2D(e2.getAsJsonObject())));
        return routine;
    }

    /**
     * Converts JsonArray into tiles for the game
     * 
     * @param fromJson - JsonArray to convert
     * @return Set<Tile>
     */
    private Set<Tile> toTiles(JsonArray fromJson) {
        HashSet<Tile> tiles = new HashSet<Tile>();
        fromJson.forEach(e -> tiles.add(toTile((JsonObject) e)));
        return tiles;
    }

    /**
     * Converts a JsonObject into a Tile object, there are many different types of tiles
     * 
     * @param jsonObject
     * @return Tile object based on the jsonObject
     */
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

    /**
     * Converts JsonArray from a specific game into a List of Moments
     * 
     * @param fromJson - JsonArray to be converted
     * @param game - the game object being used
     * @return List<Moment>
     */
    private List<Moment> toMoments(JsonArray fromJson, Game game) {
        return fromJson.asList().stream().map(m -> toMoment((JsonObject) m, game)).toList();
    }

    /**
     * Converts a single JsonObject into a Moment Object in the game
     * 
     * @param fromJson - JsonObject to convert
     * @param game - the current game
     * @return Moment object based on the above
     */
    private Moment toMoment(JsonObject fromJson, Game game) {
        int tickNo = fromJson.get("tickNo").getAsInt();
        Vector2D playerMovement = toVector2D(fromJson.getAsJsonObject("playerMovement"));
        Map<Enemy, Vector2D> enemyMovementMap = toEnemyMovementMap(fromJson.getAsJsonObject("enemyMovementMap"), game);
        return new Moment(tickNo, playerMovement, enemyMovementMap);
    }

    /**
     * Converts a JsonObject to a map of Enemy objects to Vector2D representing Enemy movements
     * 
     * @param fromJson - the JsonObject
     * @param game - the game object being used
     * @return Map<Enemy, Vector2D>
     */
    private Map<Enemy, Vector2D> toEnemyMovementMap(JsonObject fromJson, Game game) {
        var enemyMap = game.getLevel().getEnemiesAsMap();
        return fromJson.asMap().entrySet().stream().collect(Collectors.toMap(
                e -> enemyMap.get(Integer.parseInt(e.getKey())),
                e -> toVector2D((JsonObject) e.getValue())
        ));
    }

    /**
     * Converts JsonObject into Vector2D object
     * 
     * @param fromJson - JsonObject to convert
     * @return Vector2D object
     */
    private Vector2D toVector2D(JsonObject fromJson) {
        var x = fromJson.get("x").getAsInt();
        var y = fromJson.get("y").getAsInt();
        return new Vector2D(x, y);
    }

    /**
     * Saves a game object to a file used in other methods in the class
     * 
     * @param save - the File where the game should be saved
     * @param game The object to be saved
     */
    @Override
    public void saveGame(File save, Game game) {
        write(save, toJsonObject(game).toString());
    }

    /**
     * Saves a Playback object to a JSON file
     * 
     * @param save - The file where the playback should be saved
     * @param playback - the Playback object to be saved
     */
    @Override
    public void savePlayback(File save, Playback playback) {
        write(save, toJsonObject(playback).toString());
    }

    /**
     * Writes the given text to a file
     * 
     * @param save - The file to save the text to 
     * @param text - the Text that you want to save
     * @throws RuntimeException if there is problem in file writing
     */
    private void write(File save, String text) {
        try (var writer = new BufferedWriter(new FileWriter(save))) {
            writer.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cibverts Playback object to JsonObject
     * 
     * @param playback - the Playback object to be converted
     * @return JsonObject which was made with the playback
     */
    private JsonObject toJsonObject(Playback playback) {
        var playbackObject = new JsonObject();
        playbackObject.add("since", toJsonObject(playback.getSince()));
        playbackObject.add("moments", toJsonObjectFromMoments(playback.getMoments()));
        playbackObject.addProperty("endTickNo", playback.getEndTickNo());
        return playbackObject;
    }

    /**
     * Converts game object to JsonObject
     * 
     * @param game - the game object to convert
     * @return JsonObject
     */
    private JsonObject toJsonObject(Game game) {
        JsonObject gameObject = new JsonObject();
        gameObject.addProperty("id", game.getId());
        gameObject.addProperty("tickNo", game.getTickNo());
        gameObject.add("level", toJsonObject(game.getLevel()));
        return gameObject;
    }

    /**
     * Convert level into JsonObject
     * 
     * @param level - the level to convert
     * @return JsonObject
     */
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

    /**
     * Converts a set of tiles into a JsonArray
     * 
     * @param tiles
     * @return JsonArray
     */
    private JsonArray toJsonArrayFromTiles(Set<Tile> tiles) {
        JsonArray jsonArray = new JsonArray();
        tiles.forEach(t -> jsonArray.add(toJsonObject(t)));
        return jsonArray;
    }

    /**
     * Converts Tile into JsonObject
     * 
     * @param tile - the tile to convert
     * @return JsonObject
     */
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

    /**
     * Converts a set of enemies into a JsonArray
     * 
     * @param enemies - set of enemy objects to convert
     * @return JsonArray
     */
    private JsonArray toJsonArrayFromEnemies(Set<Enemy> enemies) {
        JsonArray jsonArray = new JsonArray();
        enemies.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    /**
     * Converts single Enemy object into JsonObject
     * 
     * @param enemy - Enemy object to convert
     * @return JsonObject
     */
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

    /**
     * Converts a List of Vector2D objects into a JsonArray
     * 
     * @param routing - the list of Vector2D objects
     * @return JsonArray
     */
    private JsonArray toJsonArrayFromRoutine(List<Vector2D> routine) {
        JsonArray jsonArray = new JsonArray();
        routine.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    /**
     * Converts Player object into JsonObject
     * 
     * @param player - the player to convert
     * @return JsonObject
     */
    private JsonObject toJsonObject(Player player) {
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("id", player.getId());
        playerObject.add("position", toJsonObject(player.getPosition()));
        playerObject.add("chips", toJsonArrayFromChips(player.getChips()));
        playerObject.add("keys", toJsonArrayFromKeys(player.getKeys()));
        return playerObject;
    }

    /**
     * Converts a set of keys into a jsonArray
     * 
     * @param keys - the set of keys
     * @return JsonArray
     */
    private JsonArray toJsonArrayFromKeys(Set<Key> keys) {
        JsonArray jsonArray = new JsonArray();
        keys.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    /**
     * Converts a single key into a JsonObject
     * 
     * @param key - the key object to convert
     * @return jsonObject
     */
    private JsonObject toJsonObject(Key key) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", key.getId());
        jsonObject.addProperty("color", key.getColor().toString());
        return jsonObject;
    }

    /**
     * Converts a set of chip objects into a JsonArray
     * 
     * @param chips - set of Chip objects
     * @return JsonArray
     */
    private JsonArray toJsonArrayFromChips(Set<Chip> chips) {
        JsonArray jsonArray = new JsonArray();
        chips.forEach(e -> jsonArray.add(toJsonObject(e)));
        return jsonArray;
    }

    /**
     * Converts a single Chip object into a JsonObject
     * 
     * @param chip - the chip to convert
     * @return JsonObject
     */
    private JsonObject toJsonObject(Chip chip) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", chip.getId());
        return jsonObject;
    }

    /**
     * Convert a List of Moment object sinto a JsonArray
     * 
     * @param moments
     * @return JsonArray
     */
    private JsonArray toJsonObjectFromMoments(List<Moment> moments) {
        var momentArray = new JsonArray();
        moments.forEach(m -> momentArray.add(toJsonObject(m)));
        return momentArray;
    }

    /**
     * Converts a single moment object into a JsonObject
     * 
     * @param moment - the Moment object to convert
     * @return JsonObject
     */
    private JsonObject toJsonObject(Moment moment) {
        var momentObject = new JsonObject();
        momentObject.addProperty("tickNo", moment.tickNo());
        momentObject.add("playerMovement", toJsonObject(moment.playerMovement()));
        momentObject.add("enemyMovementMap", toJsonObjectFromEnemyMovementMap(moment.enemyMovementMap()));
        return momentObject;
    }

    /**
     * Converts a map of Enemy to Vector2D objects with enemy movements into a JsonObject
     */
    private JsonObject toJsonObjectFromEnemyMovementMap(Map<Enemy, Vector2D> enemyMovementMap) {
        var enemyMovementMapObject = new JsonObject();
        enemyMovementMap.forEach((k, v) -> enemyMovementMapObject.add(Integer.toString(k.getId()), toJsonObject(v)));
        return enemyMovementMapObject;
    }

    /**
     * Converts a Vector2D object into a JsonObject
     * 
     * @param movement - the Vector2D object
     * @return JsonObject
     */
    private JsonObject toJsonObject(Vector2D movement) {
        var movementObject = new JsonObject();
        movementObject.addProperty("x", movement.x());
        movementObject.addProperty("y", movement.y());
        return movementObject;
    }

}

