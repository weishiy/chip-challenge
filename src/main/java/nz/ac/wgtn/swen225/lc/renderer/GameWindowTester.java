package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;

import javax.swing.*;
import java.awt.*;

public class GameWindowTester {
    private Game game = new Game();

    private Level level = new TestingLevels.LevelA();
    private GameWindow window = new GameWindow(game);

    {
        game.setLevel(level);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            var tester = new GameWindowTester();

            var frame = new JFrame();
            frame.setMinimumSize(new Dimension(100, 100));
            frame.add(tester.window);

            tester.window.setEnabled(true);

            frame.setVisible(true);
        });
    }
}
