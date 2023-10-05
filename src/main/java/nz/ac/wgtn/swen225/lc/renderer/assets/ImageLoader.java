package nz.ac.wgtn.swen225.lc.renderer.assets;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Loads images corresponding to different assets used in game.
 */
public final class ImageLoader {

    /**
     * Images that have been loaded.
     */
    private static final Map<URI, Image> loadedImages = new HashMap<>();

    private ImageLoader() {
        //empty
    }

    private static Image getImage(final URI uri) {
        return loadedImages.computeIfAbsent(uri, uri1 -> {
            try {
                return ImageIO.read(uri1.toURL());
            } catch (IOException e) {
                throw new RuntimeException("Unable to read image.", e);
            }
        });
    }

    /**
     * Returns image for a door.
     *
     * @return Image of a door.
     */
    public static Image getDoor() {
        return getImage(Resources.DOOR_CLOSED_DOWN);
    }

    /**
     * Returns image for a wall.
     *
     * @return Image of a wall.
     */
    public static Image getWall() {
        return getImage(Resources.WALL_00);
    }

    /**
     * Returns image of enemy.
     *
     * @return Image of enemy.
     */
    public static Image getEnemy() {
        return getImage(Resources.DOG_DOWN_1);
    }

    /**
     * Returns image for empty space.
     *
     * @return Image of empty space.
     */
    public static Image getSpace() {
        return getImage(Resources.SPACE);
    }

    /**
     * Returns image of chip.
     *
     * @return Image of chip.
     */
    public static Image getChip() {
        return getImage(Resources.RED_GEM);
    }

    /**
     * Returns image of exit tile.
     *
     * @return Image representing level exit.
     */
    public static Image getExit() {
        return getImage(Resources.STAIRCASE_1);
    }

    private static final class Resources {
        /**
         * Default door image.
         */
        private static final URI DOOR_CLOSED_DOWN = getResource(
                "/images/doors/door_closed_down.png");
        /**
         * Default wall image.
         */
        private static final URI WALL_00 = getResource("/images/walls/wall_00.png");

        /**
         * Default enemy.
         */
        private static final URI DOG_DOWN_1 = getResource("/images/enemies/dog/dog_down_1.png");

        /**
         * Default space.
         */
        private static final URI SPACE = getResource("/images/spaces/space.png");

        /**
         * Default chip.
         */
        private static final URI RED_GEM = getResource("/images/keys_gems/red_gem.png");

        /**
         * Default exit.
         */
        private static final URI STAIRCASE_1 = getResource("/images/exits/staircase_1.png");

        private static URI getResource(final String name) {
            URL url = Objects.requireNonNull(ImageLoader.class.getResource(name),
                    () -> "Couldn't find required resource: " + name);
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                throw new RuntimeException("Couldn't format path as URI.", e);
            }
        }
    }
}
