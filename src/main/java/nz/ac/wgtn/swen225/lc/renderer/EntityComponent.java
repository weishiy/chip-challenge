package nz.ac.wgtn.swen225.lc.renderer;

import javax.swing.*;
import java.awt.*;

abstract class EntityComponent extends JLabel {
    /**
     * Constructor.
     */
    EntityComponent() {

        setOpaque(false);
    }

    /**
     * Queries the image this entity should be showing.
     *
     * @return The image representing this entity.
     */
    protected abstract Image getCurrentImage();

    /**
     * Interrogates the entity's state to see if it should change.
     */
    public abstract void update();
    /*
     * I've tried to resize automatically, but it doesn't work.
     * For some reason, adding a component listener doesn't seem to detect any but the first
     * resizing. Everytime after that it doesn't work.
     *
     * As a workaround, call onResize whenever this component has been resized.
     */

    /**
     * Tell this component that it has been resized.
     *
     * <p>Workaround for failure of component listener. It's up to the user to call this
     * method after this component has been resized.
     */
    public void onResize() {
        int width = getWidth();
        int height = getHeight();

        //If image doesn't fit, don't draw it.
        if (width <= 0 || height <= 0) {
            setIcon(null);
            return;
        }

        Icon newIcon = new ImageIcon(
                getCurrentImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        setIcon(newIcon);
    }
}
