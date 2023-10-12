package nz.ac.wgtn.swen225.lc.renderer.assets;

import javax.swing.*;
import java.awt.*;

/**
 * A component that represents a door.
 *
 * <p>Uses custom scaling, since the door sprite is larger than a tile.
 */
public class DoorComponent extends JLabel {

    {
        setOpaque(false);
    }

    /**
     * Sets bounds, scaled up.
     *
     * <p>Doubles the size of the bounds, and positions closer to the origin.
     *
     * @param bounds The unscaled bounds, corresponding to the placement of tiles on the board.
     */
    public void setDoorBounds(final Rectangle bounds) {
        final int scaleBy = 2; //The door sprite is twice as large scaled as tile it occupies.
        //Since the door sprite is twice as large, it must be regressed by a fourth to be centered
        //in the same location.
        final double unscaledOffset = -1.0 / 4.0;

        int x = (int) (bounds.x + scaleBy * bounds.width * unscaledOffset);
        int y = (int) (bounds.y + scaleBy * bounds.height * unscaledOffset);

        int width = bounds.width * scaleBy;
        int height = bounds.height * scaleBy;

        setBounds(x, y, width, height);
    }
}
