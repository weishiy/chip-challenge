package nz.ac.wgtn.swen225.lc.renderer;

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

//Removed code since resizeable maze can't be resized by tile length anymore.
//            var controlFrame = new JFrame();
//            var setTileLength = new JPanel() {
//                {
//                    var label = new JLabel("Set Tile Length: ");
//                    add(label);
//                    var field = new JFormattedTextField(50);
//                    field.addPropertyChangeListener("value",
//                            e -> maze.setTileLength((Integer) field.getValue()));
//                    add(field);
//                }
//            };
//            controlFrame.add(setTileLength);
//            controlFrame.pack();
//
//            controlFrame.setVisible(true);
        });
    }

}
