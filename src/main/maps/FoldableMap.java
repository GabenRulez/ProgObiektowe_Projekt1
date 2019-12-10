package maps;

import entities.Animal;
import entities.Plant;
import utilities.Vector2d;

import java.util.HashMap;
import java.util.TreeSet;

public class FoldableMap {

    public final int width;
    public final int height;

    public final Vector2d jumpAcrossWidth;
    public final Vector2d jumpAcrossHeight;

        // na dole przechowywanie zarówno zwierząt jak i roślin
    //komparator powinien: ustawiac w kolejności: najpierw roślina, potem najsilniejsze (pod względem energii) zwierze, ... , najsłabsze zwierze
    private HashMap<Vector2d, TreeSet<Animal>> animalsMap = new HashMap<>();
    private HashMap<Vector2d, Plant> plantsMap = new HashMap<>();

    private Vector2d jungleUpperLeft;
    private Vector2d jungleLowerRight;

    public FoldableMap(int width, int height, float jungleRatio){
        this.width = width;
        this.height = height;

        this.jumpAcrossWidth = new Vector2d(width, 0);
        this.jumpAcrossHeight = new Vector2d(0, height);

        double jungleRatio1D = Math.sqrt(jungleRatio);
        int jungleWidth =   (int) Math.floor( this.width    * jungleRatio1D );
        int jungleHeight =  (int) Math.floor( this.height   * jungleRatio1D );

        jungleUpperLeft = new Vector2d((this.width - jungleWidth) / 2, (this.height - jungleHeight)/2 );
        jungleLowerRight = jungleUpperLeft.add( new Vector2d(jungleWidth, jungleHeight) );

    }


    public TreeSet<Animal> animalsAt(Vector2d position){
        return animalsMap.get( position );
    }

    public void placeAnimal(Animal animal){
        if ( animalsMap.get(animal.position) == null ){
            TreeSet<Animal> listOfAnimalsAtPosition = new TreeSet<>();
            listOfAnimalsAtPosition.add(animal);
            animalsMap.put(animal.position, listOfAnimalsAtPosition);
        }
        else{
            TreeSet<Animal> listOfAnimalsAtPosition = animalsMap.get(animal.position);
            listOfAnimalsAtPosition.add(animal);
        }
    }

    public void removeAnimal(Animal animal){
        TreeSet<Animal> listOfAnimalsAtPosition = animalsMap.get(animal.position);
        listOfAnimalsAtPosition.remove(animal);

        if ( listOfAnimalsAtPosition.isEmpty() ){
            animalsMap.remove(animal.position);
        }
    }

    public Plant plantsAt(Vector2d position){
        return plantsMap.get( position );
    }

    public void placePlant(Plant plant){
        plantsMap.put( plant.position, plant );
    }

    public void removePlant(Plant plant){
        plantsMap.remove( plant.position );
    }



}
