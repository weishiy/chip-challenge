package nz.ac.wgtn.swen225.lc.renderer.maze;

import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.ExitLock;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.LockedDoor;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Tile;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Wall;
import nz.ac.wgtn.swen225.lc.renderer.assets.DoorComponent;
import nz.ac.wgtn.swen225.lc.renderer.assets.TileMaker;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Renders the tiles and characters on a level.
 */
public class ResizeableMaze extends JLayeredPane {

    /**
     * Layer which contains tiles, not entities.
     */
    private final MazeBoard board = new MazeBoard();

    /**
     * Layer which contains objects (non-opaque tiles).
     */
    private final MazeObjects objects = new MazeObjects();

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
    public ResizeableMaze() {
        int layerIndex = 1;

        setLayer(board, layerIndex);
        add(board);

        setLayer(objects, ++layerIndex);
        add(objects);

        setLayer(entities, ++layerIndex);
        add(entities);
    }

    /**
     * Sets the level to render.
     *
     * @param newLevel The new level, or <code>null</code>.
     */
    public void setLevel(final Level newLevel) {
        this.level = newLevel; //FIXME: externally mutable
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
        objects.render();
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
        Objects.requireNonNull(level);
        int ratioX = getWidth() / level.getWidth();
        int ratioY = getHeight() / level.getHeight();
        //To fit inside size, choose lower of fitting ratios.
        return Math.min(ratioX, ratioY);
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
     * Creates bounds for a tile with its top-left at <code>position</code> and length of
     * <code>getTileLength()</code>.
     *
     * @param position The position of the <code>Tile</code>.
     * @return A <code>Rectangle</code> representing the bounds of the given object.
     */
    private Rectangle makeBounds(final Vector2D position) {
        int left = position.x() * getTileLength();
        int top = position.y() * getTileLength();

        return new Rectangle(left, top, getTileLength(), getTileLength());
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

                JComponent[][] tiles = TileMaker.makeBoard(level);
                assert tiles.length == columns;
                assert tiles[0].length == rows;

                Dimension size = new Dimension(getTileLength(), getTileLength());

                //GridLayout fills rows then columns
                for (int y = 0; y < rows; ++y) {
                    for (int x = 0; x < columns; ++x) {
                        JComponent tile = tiles[x][y];
                        tile.setSize(size);
                        add(tile);
                    }
                }

            }
            revalidate(); //Inserted or removed tiles, changed layout.
        }
    }

    /**
     * Renders non-opaque objects, like keys and chips.
     */
    private class MazeObjects extends JPanel {

        MazeObjects() {
            setLayout(null);
            setOpaque(false);
        }

        /**
         * Update objects in level.
         */
        public void render() {
            try {
                removeAll();

                if (level == null) {
                    return;
                }

                setSize(getCroppedSize());

                addObjects();

            } finally {
                revalidate();
            }
        }

        private void addObjects() {
            final Set<Vector2D> wallPositions = getWallPositions();

            level.getTiles().stream().filter(tile -> TileMaker.OBJECTS.contains(tile.getClass()))
                    .forEach(tile -> {
                        //Doors rendered specially
                        if (tile.getClass().equals(LockedDoor.class) || tile.getClass().equals(
                                ExitLock.class)) {
                            addDoor(wallPositions, tile);
                            return;
                        }

                        JComponent objectComponent = TileMaker.makeTile(tile);
                        objectComponent.setBounds(makeBounds(tile.getPosition()));
                        add(objectComponent);
                    });
        }

        private Set<Vector2D> getWallPositions() {
            return level.getTiles().stream().filter(tile -> Wall.class.equals(tile.getClass())).map(
                    Tile::getPosition).collect(Collectors.toSet());
        }

        private void addDoor(final Set<Vector2D> wallPositions, final Tile door) {
            final Key.Color colour;
            if (door instanceof LockedDoor) {
                colour = ((LockedDoor) door).getColor();
            } else {
                colour = null;
            }

            final Vector2D position = door.getPosition();
            final Function<Vector2D, Boolean> isWallPresentFromOffset =
                    offset -> wallPositions.contains(position.add(offset));

            boolean wallAbove = isWallPresentFromOffset.apply(Vector2D.UP);
            boolean wallBelow = isWallPresentFromOffset.apply(Vector2D.DOWN);

            boolean wallLeft = isWallPresentFromOffset.apply(Vector2D.LEFT);
            boolean wallRight = isWallPresentFromOffset.apply(Vector2D.RIGHT);

            //Depending on presence of walls around door, we choose different orientation
            if ((wallAbove && wallBelow) && !(wallLeft || wallRight)) {
                DoorComponent doorComponent = TileMaker.makeLeftRightDoor(position, colour);
                doorComponent.setDoorBounds(makeBounds(position));
                add(doorComponent);
            } else if (!(wallAbove || wallBelow) && (wallLeft && wallRight)) {
                DoorComponent doorComponent = TileMaker.makeUpDownDoor(position, colour);
                doorComponent.setDoorBounds(makeBounds(position));
                add(doorComponent);
            } else {
                //Default case, we make up-down door.
                //Leave redundant if statement above in case we want to make different default door.
                DoorComponent doorComponent = TileMaker.makeUpDownDoor(position, colour);
                doorComponent.setBounds(makeBounds(position));
                add(doorComponent);
            }
        }
    }


    /**
     * Renders entities, such as players and enemies.
     */
    private class MazeEntities extends JPanel {
        MazeEntities() {
            setLayout(null);
            setOpaque(false);
        }


        /**
         * Update entities to account to changes in level.
         */
        public void render() {
            try {
                removeAll();

                if (level == null) {
                    return;
                }

                setSize(getCroppedSize());

                final Player newPlayer = level.getPlayer();
                final Set<Enemy> newEnemies = Set.copyOf(level.getEnemies());

                addPlayer(newPlayer);
                addEnemies(newEnemies);
            } finally {
                revalidate(); //Inserted/removed elements.
            }
        }

        /**
         * Adds enemies to the board.
         *
         * @param enemies The set of enemies that should exist.
         */
        private void addEnemies(final Set<Enemy> enemies) {
            for (Enemy enemy : enemies) {
                JComponent enemyComponent = TileMaker.makeEnemy(new TileMaker.EnemyInfo(enemy));
                enemyComponent.setBounds(makeBounds(enemy.getPosition()));
                add(enemyComponent);
            }
        }

        /**
         * Adds the player to the board.
         *
         * <p>If the player is null, does nothing.
         *
         * @param player The (possibly null) player to check if different.
         */
        private void addPlayer(final Player player) {
            if (player == null) {
                return;
            }

            JComponent playerComponent = TileMaker.makePlayer(new TileMaker.PlayerInfo(player));

            playerComponent.setBounds(makeBounds(player.getPosition()));
            add(playerComponent);
        }

    }
}
