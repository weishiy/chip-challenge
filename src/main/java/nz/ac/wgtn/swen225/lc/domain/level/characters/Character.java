package nz.ac.wgtn.swen225.lc.domain.level.characters;

import nz.ac.wgtn.swen225.lc.domain.Entity;
import nz.ac.wgtn.swen225.lc.domain.Game;
import nz.ac.wgtn.swen225.lc.domain.level.Level;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;

/**
 * Parent class for Enemy and Player
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public abstract class Character extends Entity {

    private Level level;  // The level in which the character exists.
    private Vector2D position;

    /**
     * Constructs a new Character object with the given position.
     * @param position the character position
     */
    public Character(Vector2D position) {
        super();
        this.position = position;
    }

    /**
     * Constructs a new Character object with the given ID and position.
     *
     * @param id The unique identifier for the character.
     * @param position The position of the character.
     */
    public Character(int id, Vector2D position) {
        super(id);
        this.position = position;
    }

    /**
     * Get the level in which the character exists.
     * @return The level containing the character.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Get the game to which the character belongs.
     *
     * @return The Game instance associated with the character's level.
     */

    public Game getGame() {
        return level.getGame();
    }
    /**
     * Get the current position of the character.
     *
     * @return The character's position as a Vector2D object.
     */

    public Vector2D getPosition() {
        return position;
    }
    /**
     * Set the character's position in the game world.
     *
     * @param position The new position for the character.
     */

    public void setPosition(Vector2D position) {
        this.position = position;
    }
    /**
     * Set the level in which the character exists.
     *
     * @param level The level to set for the character.
     */

    public void setLevel(Level level) {
        this.level = level;
    }
}
