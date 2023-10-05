package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;
import nz.ac.wgtn.swen225.lc.renderer.assets.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
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
     * Maps tiles to suppliers of images representing those tiles.
     */
    private static final Map<Class<? extends Tile>, Supplier<Image>> TILE_MAPPING = Map.of(
            ChipTile.class, ImageLoader::getChip /*TODO:load image on top of empty space */,
            Exit.class, ImageLoader::getExit, ExitLock.class, ImageLoader::getDoor /*TODO: Uses default door for exit lock. Should be
      different. */, InfoField.class, ImageLoader::getInfoIcon, KeyTile.class, ImageLoader::getKey,
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
    private static final Map<NewComponentArgument, JComponent> cacheTileComponent = new WeakHashMap<>();

    private TileMaker() {
        //empty
    }

    /**
     * Creates a board representing the tiles in the level.
     *
     * @param level The level to represent.
     * @return 2D array of JComponents. The first dimension is <code>level.getWidth()</code> and the
     * second dimension is <code>level.getHeight()</code>.
     */
    static JComponent[][] makeBoard(final Level level) {
        Set<Tile> tiles = level.getTiles();
        BiFunction<Integer, Integer, JComponent> getTile = (x, y) -> tiles.stream()
                //Gets first tile that matches coordinates
                .filter(t -> {
                    Vector2D position = t.getPosition();
                    return position.x() == x && position.y() == y;
                })
                //If tile present, calls `makeTile`, else, calls `emptyTile`.
                .findFirst().map(tile -> makeTile(tile, new Vector2D(x, y))).orElseGet(
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
     * @param tile     The tile to make a component of.
     * @param position The location of the tile, used as its identity.
     * @return A representation of the tile.
     * @throws IllegalArgumentException If <code>tile</code> doesn't match any known subclass of
     *                                  <code>Tile</code>.
     * @throws NullPointerException     If <code>tile</code> is null.
     */
    private static JComponent makeTile(final Tile tile, final Vector2D position)
            throws IllegalArgumentException {
        Objects.requireNonNull(tile);
        Supplier<Image> imageSupplier = TILE_MAPPING.get(tile.getClass());
        if (imageSupplier == null) {
            throw new IllegalArgumentException("The provided tile isn't recognised.");
        }
        return makeSprite(imageSupplier.get(), position);
    }

    private static JComponent simpleTile(final Object tile) {
        return new JLabel(tile.getClass().getSimpleName()) {
            {
                setBorder(BorderFactory.createLineBorder(Color.BLUE));
                setToolTipText(getText());
            }
        };
    }

    /**
     * Creates component representing an empty tile.
     *
     * @return A component representing an empty tile.
     */
    private static JComponent emptyTile(final Vector2D position) {
        return makeSprite(ImageLoader.getSpace(), position);
    }

    static JComponent makePlayer(final PlayerInfo playerInfo) {
        return makeSprite(DefaultImages.DEFAULT_PLAYER_IMAGE, playerInfo);
    }

    static JComponent makeEnemy(final EnemyInfo enemyInfo) {
        return makeSprite(DefaultImages.DEFAULT_ENEMY_IMAGE, enemyInfo);
    }


    private static JComponent makeSprite(final Image image, final Object identity) {
        Objects.requireNonNull(image);
        Objects.requireNonNull(identity);

        return cacheTileComponent.computeIfAbsent(new NewComponentArgument(image, identity),
                componentArgument -> new JLabel() {
                    {
                        addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(final ComponentEvent e) {
                                updateIcon();
                            }
                        });
                        updateIcon();
                    }

                    private void updateIcon() {
                        if (getWidth() <= 0 || getHeight() <= 0) {
                            return; //Can't scale image if 0 dimensions.
                        }
                        Icon scaledIcon = memoizeScaledIcon.computeIfAbsent(
                                new ScaleImageArgument(componentArgument.image, getWidth(),
                                        getHeight()), r -> new ImageIcon(r.notScaled()
                                        .getScaledInstance(getWidth(), getHeight(),
                                                Image.SCALE_FAST)));

                        setIcon(scaledIcon);
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

    record EnemyInfo(Enemy enemy) {

    }

    record PlayerInfo(Player player) {

    }

    private record DefaultImages() {
        /**
         * Size of our default images.
         */
        private static final int IMAGE_SIZE = 480;
        /**
         * The default image representing a player.
         */
        private static final Image DEFAULT_PLAYER_IMAGE = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE,
                BufferedImage.TYPE_INT_ARGB) {
            public static final int VERTICAL_BORDER = 30; // /1/16
            public static final int HORIZONTAL_BORDER = 60; // 2/16

            {
                Graphics2D g2d = createGraphics();
                g2d.setColor(Color.YELLOW);

                g2d.fillOval(HORIZONTAL_BORDER, VERTICAL_BORDER, IMAGE_SIZE - 2 * HORIZONTAL_BORDER,
                        IMAGE_SIZE - 2 * VERTICAL_BORDER);

                g2d.dispose();
            }
        };
        /**
         * The default image representing an enemy.
         */
        private static final Image DEFAULT_ENEMY_IMAGE = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE,
                BufferedImage.TYPE_INT_ARGB) {
            public static final int BORDER = 40;
            public static final int THICKNESS = 30;

            {
                Graphics2D g2d = createGraphics();

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(THICKNESS));
                g2d.drawLine(0, 0, IMAGE_SIZE, IMAGE_SIZE);
                g2d.drawLine(IMAGE_SIZE, 0, 0, IMAGE_SIZE);

                g2d.setColor(Color.RED);
                g2d.fillOval(BORDER, BORDER, IMAGE_SIZE - BORDER * 2, IMAGE_SIZE - BORDER * 2);
                g2d.dispose();
            }
        };
    }
}
