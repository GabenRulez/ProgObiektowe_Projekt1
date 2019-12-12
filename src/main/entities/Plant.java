package entities;

import utilities.Vector2d;

public class Plant {
    public Vector2d position;
    public final int energy;

    public Plant(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }
}
