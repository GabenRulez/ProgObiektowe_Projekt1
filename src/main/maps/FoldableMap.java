package maps;

import utilities.Vector2d;

import java.util.HashMap;
import java.util.TreeSet;

public class FoldableMap {

    private int width;
    private int height;

        // na dole przechowywanie zarówno zwierząt jak i roślin
    //komparator powinien: ustawiac w kolejności: najpierw roślina, potem najsilniejsze (pod względem energii) zwierze, ... , najsłabsze zwierze
    private HashMap<Vector2d, TreeSet<Object>> objectsMap = new HashMap<>();


    public FoldableMap(int width, int height){
        this.width = width;
        this.height = height;

    }
}
