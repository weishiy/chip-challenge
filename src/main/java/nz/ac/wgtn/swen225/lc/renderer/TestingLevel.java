package nz.ac.wgtn.swen225.lc.renderer;

import nz.ac.wgtn.swen225.lc.domain.Vector2D;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.domain.level.items.Key;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Exit;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.InfoField;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.LockedDoor;
import nz.ac.wgtn.swen225.lc.domain.level.tiles.Wall;

public class TestingLevel extends Level {
    private static final int LENGTH = 5;

    public TestingLevel() {
        super(-1, LENGTH, LENGTH, 999);

        addTile(w(1, 1));
        addTile(w(2, 1));
        addTile(w(3, 1));

        addTile(new Exit(new Vector2D(1, 3)));
        addTile(new InfoField(new Vector2D(2, 3)));
        addTile(new LockedDoor(new Vector2D(3, 3), Key.Color.BLUE));
    }

    private static Wall w(int x, int y) {
        return new Wall(new Vector2D(x, y));
    }

}
