package nz.ac.wgtn.swen225.lc.renderer;

import javax.swing.*;

public class MazeTester {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var mazeFrame = new JFrame();
            var maze = new Maze();
            maze.setLevel(new TestingLevel());
            mazeFrame.add(maze);
            mazeFrame.pack();

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
