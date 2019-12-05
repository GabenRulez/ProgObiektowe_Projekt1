package maps;

import utilities.Vector2d;

import java.util.HashMap;

public class FoldableMap {

    private int width;
    private int height;

    private HashMap<Vector2d, Object> objectsMap = new HashMap<>();


    public FoldableMap(int width, int height){
        this.width = width;
        this.height = height;

    }
}
