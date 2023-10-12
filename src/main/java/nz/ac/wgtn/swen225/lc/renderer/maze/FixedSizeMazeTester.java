package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.renderer.TestingLevels;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point for displaying FixedSizeMaze.
 */
public final class FixedSizeMazeTester {


    private FixedSizeMazeTester() { //empty
    }

    /**
     * Entry point.
     *
     * @param args Not used.
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Level level = getLevel(args);

            var mazeFrame = new JFrame();
            mazeFrame.setLayout(null);
            mazeFrame.setLocationByPlatform(true);
            mazeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            var maze = new FixedSizeMaze();
            maze.setLevel(level);
            mazeFrame.add(maze);

            final int delay = 1000 / 120;
            new Timer(delay, e -> maze.render()).start();

            final var minDimension = new Dimension(100, 100);
            mazeFrame.setMinimumSize(minDimension);
            mazeFrame.setVisible(true);

            var controlFrame = getControlFrame(maze);

            controlFrame.setVisible(true);
        });
    }

    private static JFrame getControlFrame(final FixedSizeMaze maze) {
        var controlFrame = new JFrame();
        var setTileLength = new JPanel() {
            {
                var label = new JLabel("Set Tile Length: ");
                add(label);
                var field = new JFormattedTextField(0);
                field.addPropertyChangeListener("value",
                        e -> maze.setTileLength((Integer) field.getValue()));
                add(field);
            }
        };
        controlFrame.add(setTileLength);
        controlFrame.pack();
        return controlFrame;
    }

    private static Level getLevel(final String[] args) {
        Level level;
        if (args.length == 1) {
            final String letter = args[0];
            if (letter.equalsIgnoreCase("A")) {
                level = new TestingLevels.LevelA();
            } else if (letter.equalsIgnoreCase("B")) {
                level = new TestingLevels.LevelB();
            } else {
                //Default case.
                level = new TestingLevels.LevelA();
            }
        } else {
            //Default case.
            level = new TestingLevels.LevelA();
        }
        return level;
    }

}
