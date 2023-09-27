package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Helper interface for making tiles for the maze.
 */
public interface Sprites {
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
                .findFirst().map(Sprites::makeTile).orElseGet(Sprites::emptyTile);

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
    static JComponent makeTile(Tile tile) throws IllegalArgumentException {
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
    static JComponent emptyTile() {
        //TODO: add image
        return new JLabel() {
            {
                setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            }
        };
    }


    /**
     * A component that represents the player.
     */
    class PlayerComponent extends EntityComponent {
        /**
         * Size of the default image.
         */
        private static final int DEFAULT_SIZE = 480;
        //TODO: Image made manually, should be taken from file
        /**
         * Default image.
         */
        private static final Image DEFAULT_PLAYER_IMAGE = new BufferedImage(DEFAULT_SIZE,
                DEFAULT_SIZE, BufferedImage.TYPE_INT_ARGB) {
            public static final int VERTICAL_BORDER = 30; // /1/16
            public static final int HORIZONTAL_BORDER = 60; // 2/16

            {
                Graphics2D g2d = createGraphics();
                g2d.setColor(Color.YELLOW);

                g2d.fillOval(HORIZONTAL_BORDER, VERTICAL_BORDER,
                        DEFAULT_SIZE - 2 * HORIZONTAL_BORDER, DEFAULT_SIZE - 2 * VERTICAL_BORDER);

                g2d.dispose();
            }
        };


        /**
         * The player being represented.
         */
        private final Player player;
        /**
         * The image representing the player.
         */
        private Image currentImage = DEFAULT_PLAYER_IMAGE; //FIXME: non-serializable

        /**
         * Constructor.
         *
         * @param player The player to reference.
         */
        public PlayerComponent(final Player player) {
            this.player = player; //FIXME: externally mutable

        }


        @Override
        protected final Image getCurrentImage() {
            return currentImage;
        }

        @Override
        public void update() {

        }
    }

    class EnemyComponent extends EntityComponent {
        /**
         * Length of default image.
         */
        private static final int IMAGE_SIZE = 480;
        /**
         * Default enemy image.
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

        /**
         * Reference to the enemy.
         */
        private final Enemy enemy;
        private Image currentImage = DEFAULT_ENEMY_IMAGE; //FIXME: non-serializable

        public EnemyComponent(final Enemy enemy) {
            this.enemy = enemy; //FIXME: externally mutable
            //TODO:stub
        }


        @Override
        protected Image getCurrentImage() {
            return currentImage;
        }

        /**
         * Interrogates the player's state to see if it should change.
         */
        @Override
        public void update() {
            //TODO: stub
        }
    }
}
