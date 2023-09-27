package nz.ac.wgtn.swen225.lc.recorder;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.persistency.Persistence;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultRecorder implements Recorder {

    private final Persistence persistence;
    private final Playback playback;
    private final Game game;
    private final Map<Integer, Enemy> enemyMap;

    public DefaultRecorder(Persistence persistence, Game game) {
        this.persistence = persistence;
        this.game = game;
        this.playback = new Playback();
        var gameSnapShot = Game.deepCopyof(this.game);
        this.playback.setSince(gameSnapShot);
        this.enemyMap = gameSnapShot.getLevel().getEnemiesAsMap();
    }


    /**
     * Start the recording process
     */
    @Override
    public void onStart() {
        // TODO: Implement initialization
    }

    /**
     * Record the game state at a specific moment
     *
     * @param playerMovement The movement of the player character
     * @param enemyMovementMap A map of enemy character and their movement
     */
    @Override
    public void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap) {
        if(playerNotMoved(playerMovement) && enemyNotMoved(enemyMovementMap)) {
            return;
        }

        this.playback.addMovement(
                new Moment(
                        game.getTickNo(), playerMovement,
                        enemyMovementMap.entrySet().stream().collect(Collectors.toMap(
                                e -> enemyMap.get(e.getKey().getId()), Map.Entry::getValue
                        ))));
    }

    /**
     * Stop the recording process and save the recorded gameplay
     */
    @Override
    public void onDestroy() {
        try {
            playback.setEndTickNo(game.getTickNo());
            var dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            var timeStamp = dateFormat.format(new Date());
            var classpathUri = Objects.requireNonNull(DefaultRecorder.class.getResource("/")).getFile();
            var classPath = new URI(classpathUri).getPath();
            var pathname = classPath + "/playback_" + timeStamp + ".json";
            this.persistence.savePlayback(new File(pathname), playback);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Check if the player character has not moved
     * @param playerMovement
     * @return
     */
    private boolean playerNotMoved(Vector2D playerMovement) {
        return Vector2D.ZERO.equals(playerMovement);
    }


    private boolean enemyNotMoved(Map<Enemy, Vector2D> enemyMovementMap) {
    return enemyMovementMap.isEmpty() || enemyMovementMap.values().stream().allMatch(Vector2D.ZERO::equals);
    }
}
