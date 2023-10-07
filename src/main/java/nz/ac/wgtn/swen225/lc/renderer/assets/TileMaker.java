package nz.ac.wgtn.swen225.lc.renderer.assets;

import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Helper interface for making tiles for the maze.
 */
public final class TileMaker {

    /**
     * Objects are non-opaque tiles. They are rendered on a separate layer.
     */
    public static final Set<Class<? extends Tile>> OBJECTS = Set.of(ChipTile.class, ExitLock.class,
            InfoField.class, KeyTile.class, LockedDoor.class);
    /**
     * Maps tiles to suppliers of images representing those tiles.
     */
    private static final Map<Class<? extends Tile>, Supplier<Image>> TILE_MAPPING = Map.of(
            ChipTile.class, ImageLoader::getChip, Exit.class, ImageLoader::getExit, ExitLock.class,
            ImageLoader::getDoor /*TODO: Uses default door for exit lock. Should be different. */,
            InfoField.class, ImageLoader::getInfoIcon, KeyTile.class, ImageLoader::getKey,
            LockedDoor.class, ImageLoader::getDoor, Wall.class, ImageLoader::getWall);
    /**
     * Caches scaled images.
     *
     * <p>Caches scaling, to avoid excessive computation, to avoid repeating calculations while
     * keeping functional orientation.
     *
     * @see #makeSprite(Image, Object)
     */
    private static final Map<ScaleImageArgument, Icon> memoizeScaledIcon = new WeakHashMap<>();

    /**
     * Caches tile components.
     *
     * <p>Creating new <code>JComponent</code>  is expensive, and doing so regularly (especially
     * ones with images) results in flickering. This caching avoids this, while still being
     * outwardly functional.
     */
    private static final Map<NewComponentArgument, JComponent> cacheTileComponent =
            new WeakHashMap<>();

    private TileMaker() {
        //empty
    }

    /**
     * Creates a board representing the opaque tiles in the level.
     *
     * @param level The level to represent.
     * @return 2D array of JComponents. The first dimension is <code>level.getWidth()</code> and the
     * second dimension is <code>level.getHeight()</code>.
     */
    public static JComponent[][] makeBoard(final Level level) {
        Set<Tile> tiles = level.getTiles();
        BiFunction<Integer, Integer, JComponent> getTile = (x, y) -> tiles.stream()
                //Gets first tile that matches coordinates
                .filter(t -> {
                    Vector2D position = t.getPosition();
                    return position.x() == x && position.y() == y;
                })
                //If tile present, calls `makeTile`, else, calls `emptyTile`.
                .findFirst().map(TileMaker::makeBoardTile).orElseGet(
                        () -> TileMaker.emptyTile(new Vector2D(x, y)));

        int width = level.getWidth();
        int height = level.getHeight();

        var board = new JComponent[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                board[x][y] = getTile.apply(x, y);
            }
        }

        return board;
    }

    /**
     * From the given <code>Tile</code>, constructs a new <code>JComponent</code> representing it.
     *
     * <p>The specific subclass of <code>tile</code> provides information on what the tile will
     * appear as.
     *
     * @param tile The tile to make a component of.
     * @return A representation of the tile.
     * @throws IllegalArgumentException If <code>tile</code> doesn't match any known subclass of
     *                                  <code>Tile</code>.
     * @throws NullPointerException     If <code>tile</code> is null.
     */
    public static JComponent makeTile(final Tile tile) throws IllegalArgumentException {
        Objects.requireNonNull(tile);
        Supplier<Image> imageSupplier = TILE_MAPPING.get(tile.getClass());
        if (imageSupplier == null) {
            throw new IllegalArgumentException("The provided tile isn't recognised.");
        }
        return makeSprite(imageSupplier.get(), tile.getPosition());
    }

    /**
     * Makes a tile for the board layer.
     *
     * <p>If the tile refers to an opaque object, then returns a space tile.
     *
     * @param tile The tile to build the component from.
     * @return A component representing the given tile, or a space tile.
     */
    public static JComponent makeBoardTile(final Tile tile) {
        Objects.requireNonNull(tile);
        Supplier<Image> imageSupplier;
        if (OBJECTS.contains(tile.getClass())) {
            imageSupplier = ImageLoader::getSpace;
        } else {
            imageSupplier = TILE_MAPPING.get(tile.getClass());
        }

        if (imageSupplier == null) {
            throw new IllegalArgumentException("The provided tile isn't recognised.");
        }
        return makeSprite(imageSupplier.get(), tile.getPosition());
    }

    /**
     * Makes a vertical, up-down door.
     *
     * @param position Board position for identity.
     * @return Component representing this door.
     */
    public static DoorComponent makeUpDownDoor(final Vector2D position) {
        return makeDoor(ImageLoader.getUpDownDoor(), position);
    }

    /**
     * Makes a horizontal, left-right door.
     *
     * @param position Board position for identity.
     * @return Component representing this door.
     */
    public static DoorComponent makeLeftRightDoor(final Vector2D position) {
        return makeDoor(ImageLoader.getLeftRightDoor(), position);
    }

    /**
     * Creates component representing an empty tile.
     *
     * @param position Where the component resides, as used for identity.
     * @return A component representing an empty tile.
     */
    private static JComponent emptyTile(final Vector2D position) {
        return makeSprite(ImageLoader.getSpace(), position);
    }

    /**
     * Gets component representing the player.
     *
     * @param playerInfo Info describing this player.
     * @return Component representing this player.
     */
    public static JComponent makePlayer(final PlayerInfo playerInfo) {
        return makeSprite(ImageLoader.getPlayer(), playerInfo);
    }

    /**
     * Gets component representing an enemy.
     *
     * @param enemyInfo Info describing this enemy.
     * @return Component representing this enemy.
     */
    public static JComponent makeEnemy(final EnemyInfo enemyInfo) {
        return makeSprite(ImageLoader.getEnemy(), enemyInfo);
    }

    private static DoorComponent makeDoor(final Image image, final Object identity) {
        Objects.requireNonNull(image);
        Objects.requireNonNull(identity);

        //Casting, *should* be safe, so long as `image` is always an image to a door.
        return (DoorComponent) cacheTileComponent.computeIfAbsent(
                new NewComponentArgument(image, identity),
                componentArgument -> new DoorComponent() {
                    {
                        componentArgument.keepIconScaled(this);
                    }
                });
    }

    private static JComponent makeSprite(final Image image, final Object identity) {
        Objects.requireNonNull(image);
        Objects.requireNonNull(identity);

        return cacheTileComponent.computeIfAbsent(new NewComponentArgument(image, identity),
                componentArgument -> new JLabel() {
                    {
                        componentArgument.keepIconScaled(this);
                    }
                });

    }

    /**
     * Argument used to generate new tile component instances.
     *
     * @param image    The <code>Image</code> to display for the tile.
     * @param identity An object unique for this tile component. If <code>identity.equals(o)
     *                 </code>, then <code>o</code> corresponds to the same tile as this.
     */
    record NewComponentArgument(Image image, Object identity) {
        /**
         * Whenever this label is resized, updates the icons scale.
         *
         * @param label The label to keep updated.
         */
        public void keepIconScaled(final JLabel label) {
            label.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(final ComponentEvent e) {
                    updateIcon(label);
                }
            });
            updateIcon(label);
        }

        /**
         * Updates the icon for the label to a new scaling.
         *
         * @param label The label to be updated.
         */
        private void updateIcon(final JLabel label) {
            if (label.getWidth() <= 0 || label.getHeight() <= 0) {
                return; //Can't scale image if 0 dimensions.
            }
            Icon scaledIcon = memoizeScaledIcon.computeIfAbsent(
                    new ScaleImageArgument(image, label.getWidth(), label.getHeight()),
                    r -> new ImageIcon(r.notScaled()
                            .getScaledInstance(label.getWidth(), label.getHeight(),
                                    Image.SCALE_FAST)));

            label.setIcon(scaledIcon);
        }
    }

    /**
     * Arguments used to generate a scaled image.
     *
     * @param notScaled <code>Image</code> used as a base.
     * @param width     Width of scaling in pixels.
     * @param height    Height of scaling in pixels.
     */
    record ScaleImageArgument(Image notScaled, int width, int height) {
    }

    /**
     * Info describing an enemy sprite.
     *
     * @param identity Something to unique identify which enemy this is.
     */
    public record EnemyInfo(Object identity) {
        public EnemyInfo(final Enemy enemy) {
            //In order to keep different enemies separate, we keep reference as identity
            this((Object) enemy);
        }

    }

    /**
     * Info describing the player sprite.
     */
    public record PlayerInfo() {
        public PlayerInfo(final Player player) {
            this();
        }
    }

}
