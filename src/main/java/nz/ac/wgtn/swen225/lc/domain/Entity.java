package nz.ac.wgtn.swen225.lc.domain;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Root class for all entities in the game. Contains a unique identifier of all objects on the level
 * @author Shiyan Wei
 * Student ID: 300569298
 */
public abstract class Entity implements Serializable {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final int id;
    /**
     * Creates a new entity with an automatically generated unique identifier.
     */
    public Entity() {
        this.id = ID_GENERATOR.getAndIncrement();
    }
    /**
     * Creates a new entity with the specified unique identifier.
     *
     * @param id The unique identifier for the entity.
     */
    public Entity(int id) {
        this.id = id;
    }
    /**
     * Get the unique identifier of the entity.
     *
     * @return The unique identifier of the entity.
     */
    public int getId() {
        return id;
    }

}
