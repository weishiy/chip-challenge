package nz.ac.wgtn.swen225.lc.renderer.assets;

import nz.ac.wgtn.swen225.lc.renderer.AdjacentWalls;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Loads images corresponding to different assets used in game.
 *
 * @author Jeremy Kanal-Scott 300624019
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
     * Returns image for a door, to traverse up and down.
     *
     * @return Image of a door.
     */
    public static Image getUpDownDoor() {
        return getImage(Resources.DOOR_CLOSED_DOWN);
    }

    /**
     * Returns image for a door, to traverse left and right.
     *
     * @return Image of a door.
     */
    public static Image getLeftRightDoor() {
        return getImage(Resources.DOOR_CLOSED_LEFT);
    }

    /**
     * Returns default image for a wall.
     *
     * @return Image of a wall.
     */
    public static Image getWall() {
        return getImage(Resources.WALL_00);
    }

    /**
     * Returns image for a wall depending on adjacency.
     *
     * @param adjacentWalls indicates which adjacent tiles are walls.
     * @return Image for appropriate wall.
     */
    public static Image getWall(final AdjacentWalls adjacentWalls) {
        //Wall images are arranged in 4-by-4 square, indexed 0-15, depending on the presence of
        //adjacent walls.
        final boolean above = adjacentWalls.wallAbove(), below = adjacentWalls.wallBelow();

        //The row of the image grid. Commented out sections of the logic indicates unnecessary
        // calculations, but included for clarity.
        final int row;
        if (!above && !below) {
            row = 0;
        } else if (!above /*&& below*/) {
            row = 1;
        } else if (/*above &&*/below) {
            row = 2;
        } else /*if (above && !below)*/ {
            row = 3;
        }

        final boolean left = adjacentWalls.wallLeft(), right = adjacentWalls.wallRight();

        //The column of the image grid.
        final int column;
        if (!left && !right) {
            column = 0;
        } else if (!left /*&& right*/) {
            column = 1;
        } else if (/*left &&*/right) {
            column = 2;
        } else /*if (left && !right)*/ {
            column = 3;
        }

        final int index = 4 * row + column;

        return getImage(Resources.WALLS.get(index));
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
     * Returns image of enemy with orientation
     *
     * @param orientation The facing of the enemy.
     * @return Image representing the enemy.
     */
    public static Image getEnemy(MovementTracker.Orientation orientation) {
        return getImage(Resources.DOG_ORIENTATIONS.get(orientation));
    }

    /**
     * Returns image of player.
     *
     * @return Image of player.
     */
    public static Image getPlayer() {
        return getImage(Resources.PLAYER_DOWN_1);
    }

    /**
     * Returns player sprite with given orientation.
     *
     * @param orientation The orientation of the player.
     * @return Image representing the player.
     */
    public static Image getPlayer(final MovementTracker.Orientation orientation) {
        return getImage(Resources.PLAYER_ORIENTATIONS.get(orientation));
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
     * Returns image of info icon.
     *
     * @return Image of info icon.
     */
    public static Image getInfoIcon() {
        return getImage(Resources.INFO_ICON);
    }

    /**
     * Returns image of key.
     *
     * @return Image of key.
     */
    public static Image getKey() {
        return getImage(Resources.KEY);
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
         * Up-down door image.
         */
        private static final URI DOOR_CLOSED_DOWN = getResource(
                "/images/doors/door_closed_down.png");

        /**
         * Left-right door image.
         */
        private static final URI DOOR_CLOSED_LEFT = getResource(
                "/images/doors/door_closed_left.png");

        /**
         * Default wall image.
         */
        private static final URI WALL_00 = getResource("/images/walls/wall_00.png");

        /**
         * Different wall images, that correspond to which walls are adjacent.
         *
         * <p>There are 16 images, because each of the 4 sides may contain an adjacent wall. (2^4)
         */
        private static final List<URI> WALLS = IntStream.range(0, 16).mapToObj(
                "/images/walls/wall_%02d.png"::formatted).map(Resources::getResource).toList();

        /**
         * Default enemy.
         */
        private static final URI DOG_DOWN_1 = getResource("/images/enemies/dog/dog_down_1.png");

        /**
         * Default player sprite.
         */
        private static final URI PLAYER_DOWN_1 = getResource("/images/player/player_down_1.png");

        /**
         * Dog sprites for given orientations.
         */
        private static final Map<MovementTracker.Orientation, URI> DOG_ORIENTATIONS =
                //Stream operations for "shortness"
                //Map of orientation to string fragments
                Map.of(MovementTracker.Orientation.UP, "dog_up_1", MovementTracker.Orientation.DOWN,
                                "dog_down_1", MovementTracker.Orientation.LEFT, "dog_left_1",
                                MovementTracker.Orientation.RIGHT, "dog_right_1")
                        //Maps string fragments to URI, recollects into map
                        .entrySet().stream().map(entry -> Map.entry(entry.getKey(),
                                getResource("/images/enemies/dog/%s.png".formatted(entry.getValue()))))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /**
         * Player sprites for given orientations.
         */
        private static final Map<MovementTracker.Orientation, URI> PLAYER_ORIENTATIONS =
                //Stream operations for "shortness"
                //Map of orientation to string fragments
                Map.of(MovementTracker.Orientation.UP, "up_1", MovementTracker.Orientation.DOWN,
                                "down_1", MovementTracker.Orientation.LEFT, "left_1",
                                MovementTracker.Orientation.RIGHT, "right_1")
                        //Maps string fragments to URI, recollects into map
                        .entrySet().stream().map(entry -> Map.entry(entry.getKey(),
                                getResource("/images/player/player_%s.png".formatted(entry.getValue()))))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /**
         * Default space.
         */
        private static final URI SPACE = getResource("/images/spaces/space.png");

        /**
         * Default chip.
         */
        private static final URI RED_GEM = getResource("/images/keys_gems/red_gem.png");

        /**
         * Info icon.
         */
        private static final URI INFO_ICON = getResource("/images/keys_gems/info_icon.png");

        /**
         * Key for opening exit door.
         */
        private static final URI KEY = getResource("/images/keys_gems/key.png");

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
