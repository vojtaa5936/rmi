package shared;

import java.io.Serializable;

public abstract class Polozka implements Serializable{

    private int id;

    public int getId() {
        return id;
    }

    public Polozka setId(int id) {
        this.id = id;
        return this;
    }
}