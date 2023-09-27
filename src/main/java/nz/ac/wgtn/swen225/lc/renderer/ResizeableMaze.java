package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Renders the tiles and characters on a level.
 */
class ResizeableMaze extends JLayeredPane {

    /**
     * Layer which contains tiles, not entities.
     */
    private final MazeBoard board = new MazeBoard();

    /**
     * The layer which contains entities.
     */
    private final MazeEntities entities = new MazeEntities();
    /**
     * The level this maze is rendering.
     */
    private Level level;

    /**
     * Constructor.
     */
    ResizeableMaze() {
        setLayer(board, 1);
        add(board);

        setLayer(entities, 2);
        add(entities);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                render();
            }
        });
    }

    /**
     * Sets the level to render.
     *
     * @param level The new level, or <code>null</code>.
     */
    public void setLevel(final Level level) {
        this.level = level;
        render();
    }

    /**
     * Indicates whether a level is set.
     *
     * @return True if a level has been set to non-null.
     */
    public boolean isLevelSet() {
        return level != null;
    }

    /**
     * Updates to account to changes in level.
     *
     * <p>If <code>level</code> isn't set, or was set to <code>null</code>, doesn't render any
     * tiles.
     */
    public void render() {
        board.render();
        entities.render();
    }

    /**
     * Determines the length of a tile.
     *
     * <p>Internally used to space out and align objects.
     *
     * @return The tile length.
     */
    protected int getTileLength() {
        assert level != null;
        int ratioX = getWidth() / level.getWidth();
        int ratioY = getHeight() / level.getHeight();
        return Math.min(ratioX, ratioY);
        //To fit inside size, choose lower of fitting ratios.
    }

    /**
     * Returns restricted size of maze to keep each tile square.
     *
     * <p>Requires this maze to currently have a non-null level set.
     *
     * @return The calculated size.
     */
    protected Dimension getCroppedSize() {
        Objects.requireNonNull(level);
        return new Dimension(level.getWidth() * getTileLength(),
                level.getHeight() * getTileLength());
    }

    /**
     * Renders the entirety of the tiles on the level.
     */
    private class MazeBoard extends JPanel {
        /**
         * The layout manager for this component.
         */
        private final GridLayout layout = new GridLayout();

        /**
         * Constructor.
         */
        MazeBoard() {
            setLayout(layout);
        }

        /**
         * Updates to account to changes in level.
         *
         * <p>If <code>level</code> isn't set, or was set to <code>null</code>, doesn't render any
         * tiles.
         */
        public void render() {
            if (level != null) {
                setSize(getCroppedSize());
            }
            updateTiles();
        }

        /*
         * Adds tiles to this panel.
         */
        private void updateTiles() {
            removeAll();


            if (level != null) {
                int rows = level.getHeight();
                int columns = level.getWidth();

                layout.setRows(rows);
                layout.setColumns(columns);

                JComponent[][] tiles = Sprites.makeBoard(level);
                assert tiles.length == columns;
                assert tiles[0].length == rows;

                int tileLength = getTileLength();

                //GridLayout fills rows then columns
                for (int y = 0; y < rows; ++y) {
                    for (int x = 0; x < columns; ++x) {
                        JComponent tile = tiles[x][y];
                        tile.setMaximumSize(new Dimension(tileLength, tileLength));
                        tile.setSize(tileLength, tileLength);
                        add(tile);
                    }
                }

                revalidate(); //Inserted tiles, changed layout
            }
        }
    }

    /**
     * Renders entities, such as players and enemies.
     */
    private class MazeEntities extends JPanel {
        /**
         * Shows the enemies and the components that represent them.
         */
        private final Map<Enemy, Sprites.EnemyComponent> enemyComponents = new HashMap<>();
        /**
         * Current player.
         */
        private Player player;
        /**
         * Component representing the current player.
         *
         * <p>Existence should coincide with <code>player</code>. In other words, it only should
         * be non-null whenever <code>player</code> is non-null.
         */
        private Sprites.PlayerComponent playerComponent;

        MazeEntities() {
            setLayout(null);
            setOpaque(false);
        }

        /*
         * Creates bounds for an object with its top-left at <code>position</code> and square
         * length on <code>length</code>.
         */
        private static Rectangle makeBounds(final Vector2D position, final int length) {
            int left = position.x() * length;
            int top = position.y() * length;

            return new Rectangle(left, top, length, length);
        }

        /**
         * Update entities to account to changes in level.
         */
        public void render() {
            if (level == null) {
                //If no level, act as if empty level.
                checkPlayer(null);
                checkEnemies(Set.of());
                revalidate();
                return;
            }

            setSize(getCroppedSize());

            final Player newPlayer = level.getPlayer();
            final Set<Enemy> newEnemies = Set.copyOf(level.getEnemies());

            //Validate existing entities
            checkPlayer(newPlayer);
            checkEnemies(newEnemies);

            if (player != null) {
                playerComponent.setBounds(makeBounds(player.getPosition(), getTileLength()));
                playerComponent.onResize();
                add(playerComponent);
            }

            for (var entry : enemyComponents.entrySet()) {
                Sprites.EnemyComponent component = entry.getValue();
                component.setBounds(makeBounds(entry.getKey().getPosition(), getTileLength()));
                component.onResize();
                add(component);
            }

            revalidate(); //Inserted/removed elements
        }

        /**
         * Removes any enemies not in <code>newEnemies</code>, adds any new enemies from
         * <code>newEnemies</code>.
         *
         * @param newEnemies The set of enemies that should exist.
         */
        private void checkEnemies(final Set<Enemy> newEnemies) {
            //Remove any enemies that are no longer with us
            for (var iterator = enemyComponents.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Enemy, Sprites.EnemyComponent> entry = iterator.next();
                if (!newEnemies.contains(entry.getKey())) {
                    remove(entry.getValue());
                    iterator.remove();
                }
            }
            //Add enemies that we don't have already
            for (Enemy enemy : newEnemies) {
                enemyComponents.computeIfAbsent(enemy, Sprites.EnemyComponent::new);
            }
        }

        /**
         * Replaces <code>player</code> and <code>playerComponent</code> if they don't equal
         * <code>newPlayer</code>.
         *
         * @param newPlayer The (possibly null) player to check if different.
         */
        private void checkPlayer(final Player newPlayer) {
            //If both players are equal, there's no change, so return early.
            if (Objects.equals(player, newPlayer)) {
                return;
            }
            //Remove old component.
            if (playerComponent != null) {
                remove(playerComponent);
            }
            playerComponent = null;

            player = newPlayer;
            if (player != null) {
                playerComponent = new Sprites.PlayerComponent(player);
            }
        }

    }
}
