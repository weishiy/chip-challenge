package nz.ac.wgtn.swen225.lc.app.states;

import nz.ac.wgtn.swen225.lc.app.Application;

import javax.swing.*;
import java.awt.*;

public class GameOverState extends AbstractApplicationState {

    private final JPanel gameoverPanel;

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

    @Override
    public void onStateEnter() {
        super.onStateEnter();
        getApplication().getMainPanel().add(gameoverPanel);
    }

    @Override
    public void onSaveAndExitGame() {
        // do nothing
    }

    @Override
    public void onStateExit() {
        super.onStateExit();
        getApplication().getMainPanel().remove(gameoverPanel);
    }
}
