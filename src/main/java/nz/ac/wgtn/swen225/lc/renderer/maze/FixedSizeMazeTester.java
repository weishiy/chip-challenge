package nz.ac.wgtn.swen225.lc.renderer.maze;

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
            var mazeFrame = new JFrame();
            mazeFrame.setLayout(null);
            mazeFrame.setLocationByPlatform(true);
            mazeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            var maze = new FixedSizeMaze();
            maze.setLevel(new TestingLevels.LevelA());
            mazeFrame.add(maze);


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

}
