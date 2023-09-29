package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Enemy;
import nz.ac.wgtn.swen225.lc.domain.level.characters.Player;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.*;

public class TestingLevels {
    private static Wall w(int x, int y) {
        return new Wall(new Vector2D(x, y));
    }

    private static Vector2D v(int x, int y) {
        return new Vector2D(x, y);
    }

    private static Enemy e(int x, int y) {
        return new Enemy(v(x, y)) {
            @Override
            public Vector2D nextMove() {
                return null;
            }
        };
    }

    @SuppressWarnings("unused")
    public static class LevelA extends Level {
        private static final int LENGTH = 5;

        public LevelA() {
            super(-1, LENGTH, LENGTH, 999);

            addTile(w(1, 1));
            addTile(w(2, 1));
            addTile(w(3, 1));

            addTile(new Exit(new Vector2D(1, 3)));
            addTile(new InfoField(new Vector2D(2, 3), "A tip"));
            addTile(new LockedDoor(new Vector2D(3, 3), Key.Color.BLUE));

            setPlayer(new Player(v(2, 2)));

            addEnemy(e(1, 2));
            addEnemy(e(3, 2));
        }
    }

    @SuppressWarnings("unused")

    public static class LevelB extends Level {

        public static final int HEIGHT = 9;

        public LevelB() {
            super(-1, 17, HEIGHT, 999);

            for (int y = 0; y < HEIGHT; ++y) {
                addTile(w(15, y));
                addTile(new InfoField(v(0, y), "A tip"));
            }

            addTile(new Exit(v(3, 5)));

            addTile(new ChipTile(v(10, 8), null));

            setPlayer(new Player(v(1, 4)));
        }

    }
}
