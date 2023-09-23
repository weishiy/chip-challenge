package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;

import javax.swing.*;
import java.awt.*;

public class WelcomingState extends AbstractApplicationState {

    private final JPanel welcomePanel;

    public WelcomingState(Application application) {
        super(application);

        welcomePanel = new JPanel();

        var dimension = new Dimension(600, 600);
        welcomePanel.setMinimumSize(dimension);
        welcomePanel.setMaximumSize(dimension);
        welcomePanel.setPreferredSize(dimension);
        welcomePanel.setBackground(Color.BLACK);

        var welcomeLabel = new JLabel("Chips Challange");
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 45));
        welcomeLabel.setForeground(Color.WHITE);

        welcomePanel.add(welcomeLabel);
    }

    @Override
    public void onStateEnter() {
        super.onStateEnter();
        getApplication().getMainPanel().add(welcomePanel);
    }

    @Override
    public void onSaveAndExitGame() {
        // do nothing
    }

    @Override
    public void onStateExit() {
        super.onStateExit();
        getApplication().getMainPanel().remove(welcomePanel);
    }
}
