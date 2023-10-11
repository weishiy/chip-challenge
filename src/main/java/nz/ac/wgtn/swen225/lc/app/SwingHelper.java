package nz.ac.wgtn.swen225.lc.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The `SwingHelper` class provides utility methods for creating and configuring Swing components and containers.
 *
 * @author Shuja M Syed
 * Student ID: 300592409
 */
public class SwingHelper {

    /**
     * Creates a JPanel with specified dimensions and margins, and adds it to the container.
     *
     * @param container     The container to which the JPanel will be added.
     * @param width         The width of the JPanel.
     * @param height        The height of the JPanel.
     * @param marginTop     The top margin of the JPanel.
     * @param marginLeft    The left margin of the JPanel.
     * @param marginBottom  The bottom margin of the JPanel.
     * @param marginRight   The right margin of the JPanel.
     * @return The created JPanel.
     */
    public static JPanel addPanel(Container container, int width, int height, int marginTop, int marginLeft, int marginBottom, int marginRight) {
        JPanel outerPanel = addPanel(container, marginLeft + width + marginRight, marginTop + height + marginBottom);
        outerPanel.setBorder(new EmptyBorder(marginTop, marginLeft, marginBottom, marginRight));
        return outerPanel;
    }

    /**
     * Creates a JPanel with specified dimensions and adds it to the container.
     *
     * @param container The container to which the JPanel will be added.
     * @param width     The width of the JPanel.
     * @param height    The height of the JPanel.
     * @return The created JPanel.
     */
    public static JPanel addPanel(Container container, int width, int height) {
        JPanel panel = initializePanel(width, height);
        addToContainer(container, panel);
        return panel;
    }

    /**
     * Creates a JLabel with specified text, dimensions, and alignment, and adds it to the container.
     *
     * @param container          The container to which the JLabel will be added.
     * @param text               The text to display on the JLabel.
     * @param width              The width of the JLabel.
     * @param height             The height of the JLabel.
     * @param horizontalAlignment The horizontal alignment of the JLabel's text.
     * @return The created JLabel.
     */
    public static JLabel addLabel(Container container, String text, int width, int height, int horizontalAlignment) {
        return addLabel(container, text, width, height, horizontalAlignment, true);
    }

    /**
     * Creates a JLabel with specified text, dimensions, alignment, and enabled state, and adds it to the container.
     *
     * @param container          The container to which the JLabel will be added.
     * @param text               The text to display on the JLabel.
     * @param width              The width of the JLabel.
     * @param height             The height of the JLabel.
     * @param horizontalAlignment The horizontal alignment of the JLabel's text.
     * @param enabled            The enabled state of the JLabel.
     * @return The created JLabel.
     */
    public static JLabel addLabel(Container container, String text, int width, int height, int horizontalAlignment, boolean enabled) {
        JLabel label = initializeLabel(text, width, height, horizontalAlignment, enabled);
        addToContainer(container, label);
        return label;
    }

    /**
     * Initializes a JPanel with specified dimensions.
     *
     * @param width  The width of the JPanel.
     * @param height The height of the JPanel.
     * @return The initialized JPanel.
     */
    private static JPanel initializePanel(int width, int height) {
        JPanel panel = new JPanel();
        Dimension dimension = new Dimension(width, height);
        panel.setMinimumSize(dimension);
        panel.setMaximumSize(dimension);
        panel.setPreferredSize(dimension);
        panel.setOpaque(true);
        return panel;
    }

    /**
     * Initializes a JLabel with specified text, dimensions, alignment, and enabled state.
     *
     * @param text               The text to display on the JLabel.
     * @param width              The width of the JLabel.
     * @param height             The height of the JLabel.
     * @param horizontalAlignment The horizontal alignment of the JLabel's text.
     * @param enabled            The enabled state of the JLabel.
     * @return The initialized JLabel.
     */
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

    /**
     * Adds a component to a container.
     *
     * @param container  The container to which the component will be added.
     * @param component  The component to be added.
     */
    private static void addToContainer(Container container, Component component) {
        container.add(component);
    }
}

