package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SwingHelper {

    // Creates a JPanel and adds it to the container with specified dimensions and margins.
    public static JPanel addPanel(Container container, int width, int height, int marginTop, int marginLeft, int marginBottom, int marginRight) {
        JPanel outerPanel = addPanel(container, marginLeft + width + marginRight, marginTop + height + marginBottom);
        outerPanel.setBorder(new EmptyBorder(marginTop, marginLeft, marginBottom, marginRight));
        return outerPanel;
    }

    // Creates a JPanel with specified dimensions and adds it to the container.
    public static JPanel addPanel(Container container, int width, int height) {
        JPanel panel = initializePanel(width, height);
        addToContainer(container, panel);
        return panel;
    }

    // Creates a JLabel and adds it to the container with specified text, dimensions, and alignment.
    public static JLabel addLabel(Container container, String text, int width, int height, int horizontalAlignment) {
        return addLabel(container, text, width, height, horizontalAlignment, true);
    }

    // Creates a JLabel with specified text, dimensions, alignment, and enabled state, and adds it to the container.
    public static JLabel addLabel(Container container, String text, int width, int height, int horizontalAlignment, boolean enabled) {
        JLabel label = initializeLabel(text, width, height, horizontalAlignment, enabled);
        addToContainer(container, label);
        return label;
    }

    // Initializes a JPanel with specified dimensions.
    private static JPanel initializePanel(int width, int height) {
        JPanel panel = new JPanel();
        Dimension dimension = new Dimension(width, height);
        panel.setMinimumSize(dimension);
        panel.setMaximumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setOpaque(true);
        return panel;
    }

    // Initializes a JLabel with specified text, dimensions, alignment, and enabled state.
    private static JLabel initializeLabel(String text, int width, int height, int horizontalAlignment, boolean enabled) {
        JLabel label = new JLabel(text, horizontalAlignment);
        Dimension dimension = new Dimension(width, height);
        label.setMinimumSize(dimension);
        label.setMaximumSize(dimension);
        label.setPreferredSize(dimension);
        label.setEnabled(enabled);
        label.setFont(new Font(Font.MONOSPACED, enabled ? Font.BOLD : Font.PLAIN, 12));
        return label;
    }

    // Adds a component to a container.
    private static void addToContainer(Container container, Component component) {
        container.add(component);
    }
}

