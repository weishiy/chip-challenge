package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;

/**
 * The Main class is the entry point of the Chip's Challenge game application.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class Main {

    /**
     * The main method for starting the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
