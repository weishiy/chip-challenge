package nz.ac.wgtn.swen225.lc.renderer;

import javax.swing.*;
import java.awt.*;

public class MazeTester {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var mazeFrame = new JFrame();
            mazeFrame.setLayout(null);
            var maze = new Maze();
            maze.setLevel(new TestingLevels.LevelA());
            mazeFrame.add(maze);

            mazeFrame.setMinimumSize(new Dimension(100, 100));
            mazeFrame.setVisible(true);

            var controlFrame = new JFrame();
            var setTileLength = new JPanel() {
                {
                    var label = new JLabel("Set Tile Length: ");
                    add(label);
                    var field = new JFormattedTextField(50);
                    field.addPropertyChangeListener("value",
                            e -> maze.setTileLength((Integer) field.getValue()));
                    add(field);
                }
            };
            controlFrame.add(setTileLength);
            controlFrame.pack();

            controlFrame.setVisible(true);
        });
    }

}
