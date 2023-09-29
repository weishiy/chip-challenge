package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.renderer.TestingLevels;

import javax.swing.*;
import java.awt.*;

/**
 * Entry point for displaying ResizeableMaze.
 */
public final class ResizeableMazeTester {

    private ResizeableMazeTester() { //empty
    }

    /**
     * Entry point.
     *
     * @param args Not used.
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            var mazeFrame = new JFrame();
            mazeFrame.setLayout(new BorderLayout());
            mazeFrame.setLocationByPlatform(true);
            mazeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            var maze = new ResizeableMaze();
            maze.setLevel(new TestingLevels.LevelA());
            mazeFrame.add(maze, BorderLayout.CENTER);


            final var minDimension = new Dimension(100, 100);
            mazeFrame.setMinimumSize(minDimension);
            mazeFrame.setVisible(true);

            final int delay = 1000 / 120;

            new Timer(delay, e -> maze.render()).start();
        });
    }

}
