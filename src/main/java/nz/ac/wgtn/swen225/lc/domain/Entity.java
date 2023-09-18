package nz.ac.wgtn.swen225.lc.domain;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity implements Serializable {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final int id;

    public Entity() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

    public int getId() {
        return id;
    }

}
