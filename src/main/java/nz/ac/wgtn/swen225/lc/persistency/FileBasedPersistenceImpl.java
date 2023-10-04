package nz.ac.wgtn.swen225.lc.persistency;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Chip;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.levels.level1.Patroller;
import nz.ac.wgtn.swen225.lc.recorder.Moment;
import nz.ac.wgtn.swen225.lc.recorder.Playback;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileBasedPersistenceImpl implements Persistence {

     @Override
    public List<Integer> getAllLevelNos() {
        return List.of(1, 2);
    }

    @Override
    public Game loadGame(int levelNo) {
        Level level;
        if (levelNo == 1) {
            level = new Level(1, 15, 14, 100);
            // map
            // row 0
            IntStream.range(2, 7).forEach(i -> level.addTile(new Wall(new Vector2D(i, 0))));
            IntStream.range(8, 13).forEach(i -> level.addTile(new Wall(new Vector2D(i, 0))));
            // row 1
            level.addTile(new Wall(new Vector2D(2, 1)));
            IntStream.range(6, 9).forEach(i -> level.addTile(new Wall(new Vector2D(i, 1))));
            level.addTile(new Wall(new Vector2D(12, 1)));
            // row 2
            level.addTile(new Wall(new Vector2D(2, 2)));
            level.addTile(new ChipTile(new Vector2D(4, 2), new Chip()));
            level.addTile(new Wall(new Vector2D(6, 2)));
            level.addTile(new Exit(new Vector2D(7, 2)));
            level.addTile(new Wall(new Vector2D(8, 2)));
            level.addTile(new ChipTile(new Vector2D(10, 2), new Chip()));
            level.addTile(new Wall(new Vector2D(12, 2)));
            // row 3
            IntStream.range(0, 5).forEach(i -> level.addTile(new Wall(new Vector2D(i, 3))));
            level.addTile(new LockedDoor(new Vector2D(5, 3), Key.Color.GREEN));
            level.addTile(new Wall(new Vector2D(6, 3)));
            level.addTile(new ExitLock(new Vector2D(7, 3)));
            level.addTile(new Wall(new Vector2D(8, 3)));
            level.addTile(new LockedDoor(new Vector2D(9, 3), Key.Color.GREEN));
            IntStream.range(10, 15).forEach(i -> level.addTile(new Wall(new Vector2D(i, 3))));
            // row 4
            level.addTile(new Wall(new Vector2D(0, 4)));
            level.addTile(new KeyTile(new Vector2D(2, 4), new Key(Key.Color.YELLOW)));
            level.addTile(new LockedDoor(new Vector2D(4, 4), Key.Color.BLUE));
            level.addTile(new LockedDoor(new Vector2D(10, 4), Key.Color.RED));
            level.addTile(new KeyTile(new Vector2D(12, 4), new Key(Key.Color.YELLOW)));
            level.addTile(new Wall(new Vector2D(14, 4)));
            // row 5
            level.addTile(new Wall(new Vector2D(0, 5)));
            level.addTile(new ChipTile(new Vector2D(2, 5), new Chip()));
            level.addTile(new Wall(new Vector2D(4, 5)));
            level.addTile(new KeyTile(new Vector2D(5, 5), new Key(Key.Color.BLUE)));
            level.addTile(new InfoField(new Vector2D(7, 5), "Collect chips to get past the chip socket. Use keys to open doors."));
            level.addTile(new KeyTile(new Vector2D(9, 5), new Key(Key.Color.RED)));
            level.addTile(new Wall(new Vector2D(10, 5)));
            level.addTile(new ChipTile(new Vector2D(12, 5), new Chip()));
            level.addTile(new Wall(new Vector2D(14, 5)));
            // row 6
            IntStream.range(0, 5).forEach(i -> level.addTile(new Wall(new Vector2D(i, 6))));
            level.addTile(new ChipTile(new Vector2D(5, 6), new Chip()));
            level.setPlayer(new Player(new Vector2D(7, 6)));
            level.addTile(new ChipTile(new Vector2D(9, 6), new Chip()));
            IntStream.range(10, 15).forEach(i -> level.addTile(new Wall(new Vector2D(i, 6))));
            // row 7
            level.addTile(new Wall(new Vector2D(0, 7)));
            level.addTile(new ChipTile(new Vector2D(2, 7), new Chip()));
            level.addTile(new Wall(new Vector2D(4, 7)));
            level.addTile(new KeyTile(new Vector2D(5, 7), new Key(Key.Color.BLUE)));
            level.addTile(new KeyTile(new Vector2D(9, 7), new Key(Key.Color.RED)));
            level.addTile(new Wall(new Vector2D(10, 7)));
            level.addTile(new ChipTile(new Vector2D(12, 7), new Chip()));
            level.addTile(new Wall(new Vector2D(14, 7)));
            // row 8
            level.addTile(new Wall(new Vector2D(0, 8)));
            level.addTile(new LockedDoor(new Vector2D(4, 8), Key.Color.RED));
            level.addTile(new ChipTile(new Vector2D(7, 8), new Chip()));
            level.addTile(new LockedDoor(new Vector2D(10, 8), Key.Color.BLUE));
            level.addTile(new Wall(new Vector2D(14, 8)));
            // row 9
            IntStream.range(0, 6).forEach(i -> level.addTile(new Wall(new Vector2D(i, 9))));
            level.addTile(new LockedDoor(new Vector2D(6, 9), Key.Color.YELLOW));
            level.addTile(new Wall(new Vector2D(7, 9)));
            level.addTile(new LockedDoor(new Vector2D(8, 9), Key.Color.YELLOW));
            IntStream.range(9, 15).forEach(i -> level.addTile(new Wall(new Vector2D(i, 9))));
            // row 10
            level.addTile(new Wall(new Vector2D(4, 10)));
            level.addTile(new Wall(new Vector2D(7, 10)));
            level.addTile(new Wall(new Vector2D(10, 10)));
            // row 11
            level.addTile(new Wall(new Vector2D(4, 11)));
            level.addTile(new ChipTile(new Vector2D(6, 11), new Chip()));
            level.addTile(new Wall(new Vector2D(7, 11)));
            level.addTile(new ChipTile(new Vector2D(8, 11), new Chip()));
            level.addTile(new Wall(new Vector2D(10, 11)));
            // row 12
            level.addTile(new Wall(new Vector2D(4, 12)));
            level.addTile(new KeyTile(new Vector2D(6, 12), new Key(Key.Color.GREEN)));
            level.addTile(new Wall(new Vector2D(7, 12)));
            level.addTile(new KeyTile(new Vector2D(8, 12), new Key(Key.Color.GREEN)));
            level.addTile(new Wall(new Vector2D(10, 12)));
            // row 13
            IntStream.range(4, 11).forEach(i -> level.addTile(new Wall(new Vector2D(i, 13))));
        } else if(levelNo == 2) {
            level = new Level(1, 9, 9, 60);

            // map
            // row 0
            IntStream.range(2, 7).forEach(i -> level.addTile(new Wall(new Vector2D(i, 0))));
            // row 1
            level.addTile(new Wall(new Vector2D(2, 1)));
            level.addTile(new KeyTile(new Vector2D(5, 1), new Key(Key.Color.RED)));
            level.addTile(new Wall(new Vector2D(6, 1)));
            // row 2
            IntStream.range(0, 9).filter(i -> i != 3).forEach(i -> level.addTile(new Wall(new Vector2D(i, 2))));
            level.addTile(new InfoField(new Vector2D(3, 2), "A tip"));
            // row 3
            level.addTile(new Wall(new Vector2D(0, 3)));
            level.addTile(new KeyTile(new Vector2D(1, 3), new Key(Key.Color.YELLOW)));
            level.addTile(new Wall(new Vector2D(2, 3)));
            level.addTile(new LockedDoor(new Vector2D(6, 3), Key.Color.YELLOW));
            level.addTile(new Wall(new Vector2D(8, 3)));
            // row 4
            level.addTile(new Wall(new Vector2D(0, 4)));
            level.addTile(new Wall(new Vector2D(2, 4)));
            level.addTile(new Wall(new Vector2D(6, 4)));
            level.addTile(new Wall(new Vector2D(8, 4)));
            // row 5
            level.addTile(new Wall(new Vector2D(0, 5)));
            level.addTile(new LockedDoor(new Vector2D(2, 5), Key.Color.RED));
            level.addTile(new Wall(new Vector2D(6, 5)));
            level.addTile(new ChipTile(new Vector2D(7, 5), new Chip()));
            level.addTile(new Wall(new Vector2D(8, 5)));
            // row 6
            IntStream.range(0, 9).filter(i -> i != 5).forEach(i -> level.addTile(new Wall(new Vector2D(i, 6))));
            level.addTile(new ExitLock(new Vector2D(5, 6)));
            // row 7
            level.addTile(new Wall(new Vector2D(2, 7)));
            level.addTile(new Exit(new Vector2D(3, 7)));
            level.addTile(new Wall(new Vector2D(6, 7)));
            // row 8
            IntStream.range(2, 7).forEach(i -> level.addTile(new Wall(new Vector2D(i, 8))));

            // enemies
            level.addEnemy(new Patroller(new Vector2D(4, 3),
                    List.of(Vector2D.RIGHT, Vector2D.DOWN, Vector2D.DOWN, Vector2D.LEFT, Vector2D.LEFT, Vector2D.UP, Vector2D.UP, Vector2D.RIGHT),
                    10));
            level.addEnemy(new Patroller(new Vector2D(4, 5),
                    List.of(Vector2D.LEFT, Vector2D.UP, Vector2D.UP, Vector2D.RIGHT, Vector2D.RIGHT, Vector2D.DOWN, Vector2D.DOWN, Vector2D.LEFT),
                    10));

            // player
            level.setPlayer(new Player(new Vector2D(4, 4)));
        } else {
            throw new IllegalArgumentException("unsupported level" + levelNo);
        }

        var game = new Game();
        game.setLevel(level);
        return game;
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
        // Use object serialization for testing for now. This needs to be changed to JSON serialization.
        try (var fin = new ByteArrayInputStream(Base64.getDecoder().decode(fromJson.get("game").getAsString()))) {
            try (var ois = new ObjectInputStream(fin)) {
                return (Game) ois.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Moment> toMoments(JsonArray fromJson, Game game) {
        return fromJson.asList().stream().map(m -> toMoment((JsonObject) m, game)).toList();
    }

    private Moment toMoment(JsonObject fromJson, Game game) {
        var tickNo = fromJson.get("tickNo").getAsInt();
        var playerMovement = toVector2D(fromJson.getAsJsonObject("playerMovement"));
        var enemyMovementMap = toEnemyMovementMap(fromJson.getAsJsonObject("enemyMovementMap"), game);
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
        playbackObject.add("moments", toJsonFromMoments(playback.getMoments()));
        playbackObject.addProperty("endTickNo", playback.getEndTickNo());
        return playbackObject;
    }

    private JsonObject toJsonObject(Game game) {
        // Use serialization for game object for temp. This needs to be changed later.
        try (var bos = new ByteArrayOutputStream()) {
            try (var oos = new ObjectOutputStream(bos)) {
                oos.writeObject(game);

                var gameObject = new JsonObject();
                gameObject.addProperty("game", Base64.getEncoder().encodeToString(bos.toByteArray()));
                return gameObject;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonArray toJsonFromMoments(List<Moment> moments) {
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
