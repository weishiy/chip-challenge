package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;

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

/**
 * Helper interface for making tiles for the maze.
 */
final class TileMaker {

    //Memoization caches function calls, to avoid repeating calculations while keeping functional
    // orientation. Without memoization, some of these entities appear to flicker.
    /**
     * Stores references to scaled images.
     *
     * @see #makeSprite(Image)
     */
    private static final Map<ImageScaleMemoize, Icon> memoizeIcon = new WeakHashMap<>();

    /**
     * Stores references to players.
     *
     * @see #makePlayer(PlayerInfo)
     */
    private static final Map<PlayerInfo, JComponent> memoizePlayer = new WeakHashMap<>();

    /**
     * Stores references to enemies.
     *
     * @see #makeEnemy(EnemyInfo)
     */
    private static final Map<EnemyInfo, JComponent> memoizeEnemy = new WeakHashMap<>();

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
                .findFirst().map(TileMaker::makeTile).orElseGet(TileMaker::emptyTile);

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
    private static JComponent makeTile(final Tile tile) throws IllegalArgumentException {
        Objects.requireNonNull(tile);
        //TODO: Stub, final version should assign the label the image associated with the Tile.
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
    private static JComponent emptyTile() {
        //TODO: add image
        return new JLabel() {
            {
                setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            }
        };
    }

    static JComponent makePlayer(final PlayerInfo playerInfo) {
        return memoizePlayer.computeIfAbsent(playerInfo,
                p -> makeSprite(DefaultImages.DEFAULT_PLAYER_IMAGE));
    }

    static JComponent makeEnemy(final EnemyInfo enemyInfo) {
        return memoizeEnemy.computeIfAbsent(enemyInfo,
                e -> makeSprite(DefaultImages.DEFAULT_ENEMY_IMAGE));
    }

    private static JComponent makeSprite(final Image image) {
        return new JLabel() {
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
                Icon scaledIcon = memoizeIcon.computeIfAbsent(
                        new ImageScaleMemoize(image, getWidth(), getHeight()), r -> new ImageIcon(
                                r.notScaled().getScaledInstance(getWidth(), getHeight(),
                                        Image.SCALE_FAST)));

                setIcon(scaledIcon);
            }
        };
    }

    record ImageScaleMemoize(Image notScaled, int width, int height) {
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
