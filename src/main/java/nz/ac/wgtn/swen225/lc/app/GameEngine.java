package nz.ac.wgtn.swen225.lc.app;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.events.GameEventListener;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.function.Consumer;

public interface GameEngine extends GameEventListener {
    void onStart();
    boolean isGameOver();
    int getTickNo();
    void update(Vector2D playerMovement);
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovement);
    void onDestroy();
    void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback);
    void unbindInputWithAction(KeyStroke keyStroke);

    // once again just a small change to allow fuzzer to access player positions
    Game getGame();
    Container getGlassPane();
}
