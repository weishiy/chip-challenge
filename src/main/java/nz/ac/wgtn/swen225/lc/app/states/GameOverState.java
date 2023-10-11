package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;

import javax.swing.*;
import java.awt.*;

/**
 * The `GameOverState` class represents the state of the game when it has ended, either as a win or a loss.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class GameOverState extends AbstractApplicationState {

    private final JPanel gameoverPanel;

    /**
     * Constructs a `GameOverState` object.
     *
     * @param application The game application instance.
     * @param isPlayWon   A boolean indicating whether the player has won the game.
     */
    public GameOverState(Application application, boolean isPlayWon) {
        super(application);

        gameoverPanel = new JPanel();

        var dimension = new Dimension(600, 600);
        gameoverPanel.setMinimumSize(dimension);
        gameoverPanel.setMaximumSize(dimension);
        gameoverPanel.setPreferredSize(dimension);
        gameoverPanel.setBackground(Color.BLACK);

        var welcomeLabel = new JLabel(isPlayWon ? "You Won" : "You Lost");
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 45));
        welcomeLabel.setForeground(Color.WHITE);

        gameoverPanel.add(welcomeLabel);
    }

    /**
     * Called when this state is entered. Adds the game over panel to the main panel of the application.
     */
    @Override
    public void onStateEnter() {
        super.onStateEnter();
        getApplication().getMainPanel().add(gameoverPanel);
    }

    /**
     * Called when saving and exiting the game. Does nothing in this state.
     */
    @Override
    public void onSaveAndExitGame() {
        // do nothing
    }

    /**
     * Called when this state is exited. Removes the game over panel from the main panel of the application.
     */
    @Override
    public void onStateExit() {
        super.onStateExit();
        getApplication().getMainPanel().remove(gameoverPanel);
    }
}
