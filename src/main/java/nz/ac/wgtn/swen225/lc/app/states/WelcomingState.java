package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;

import javax.swing.*;
import java.awt.*;

/**
 * The `WelcomingState` class represents the initial state of the game application,
 * displaying a welcome message and the game title.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class WelcomingState extends AbstractApplicationState {

    private final JPanel welcomePanel;

    /**
     * Constructs a `WelcomingState` object.
     *
     * @param application The game application instance.
     */
    public WelcomingState(Application application) {
        super(application);

        welcomePanel = new JPanel();

        var dimension = new Dimension(600, 600);
        welcomePanel.setMinimumSize(dimension);
        welcomePanel.setMaximumSize(dimension);
        welcomePanel.setPreferredSize(dimension);
        welcomePanel.setBackground(Color.BLACK);

        var welcomeLabel = new JLabel("Chips Challenge");
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 45));
        welcomeLabel.setForeground(Color.WHITE);

        welcomePanel.add(welcomeLabel);
    }

    /**
     * Called when this state is entered. Adds the welcome panel to the main panel of the application.
     */
    @Override
    public void onStateEnter() {
        super.onStateEnter();
        getApplication().getMainPanel().add(welcomePanel);
    }

    /**
     * Called when the game is saved and exited. Does nothing in this state.
     */
    @Override
    public void onSaveAndExitGame() {
        // do nothing
    }

    /**
     * Called when this state is exited. Removes the welcome panel from the main panel of the application.
     */
    @Override
    public void onStateExit() {
        super.onStateExit();
        getApplication().getMainPanel().remove(welcomePanel);
    }
}