package maps;

import entities.Animal;
import entities.Plant;
import utilities.Vector2d;
import utilities.energyComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class FoldableMap {

    public final int width;
    public final int height;

    //komparator ustawia w kolejności: najsilniejsze (pod względem energii) zwierz, ... , najsłabszy zwierz
    public HashMap<Vector2d, TreeSet<Animal>>   animalsMap  = new HashMap<>();
    private HashMap<Vector2d, Plant>             plantsMap   = new HashMap<>();
    public ArrayList<Animal>                    animalsList = new ArrayList<>();

    public HashMap<Vector2d, Boolean> placesForPlantsJungle;
    public HashMap<Vector2d, Boolean> placesForPlantsOutside;

    private Vector2d jungleLowerLeft;
    private Vector2d jungleUpperRight;

    public FoldableMap(int width, int height, float jungleRatio){
        this.width = width;
        this.height = height;

        double jungleRatio1D = Math.sqrt(jungleRatio);
        int jungleWidth =   (int) Math.floor( this.width    * jungleRatio1D );
        int jungleHeight =  (int) Math.floor( this.height   * jungleRatio1D );

        jungleLowerLeft = new Vector2d((this.width - jungleWidth) / 2, (this.height - jungleHeight)/2 );
        jungleUpperRight = jungleLowerLeft.add( new Vector2d(jungleWidth, jungleHeight) );

        placesForPlantsJungle = new HashMap<>();
        placesForPlantsOutside = new HashMap<>();

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                Vector2d tempVector = new Vector2d(i,j);
                if( tempVector.precedes(jungleUpperRight) && tempVector.follows(jungleLowerLeft) ){
                    placesForPlantsJungle.put( tempVector, Boolean.TRUE );
                }
                else{
                    placesForPlantsOutside.put( tempVector, Boolean.TRUE );
                }
            }
        }
    }


    public TreeSet<Animal> animalsAt(Vector2d position){
        return animalsMap.get( position );
    }

    public void placeAnimalOnMap(Animal animal){
        if ( animalsMap.get(animal.position) == null ){
            TreeSet<Animal> listOfAnimalsAtPosition = new TreeSet<>(new energyComparator());
            listOfAnimalsAtPosition.add(animal);
            animalsMap.put(animal.position, listOfAnimalsAtPosition);

            placesForPlantsJungle.remove(animal.position);
            placesForPlantsOutside.remove(animal.position);
        }
        else{
            TreeSet<Animal> listOfAnimalsAtPosition = animalsMap.get(animal.position);
            listOfAnimalsAtPosition.add(animal);
        }
    }

    public void removeAnimalOnMap(Animal animal){
        TreeSet<Animal> treesetOfAnimalsAtPosition = animalsMap.get(animal.position);
        treesetOfAnimalsAtPosition.remove(animal);

        if (treesetOfAnimalsAtPosition.isEmpty()){
            animalsMap.remove(animal.position);

            if( animal.position.precedes(jungleUpperRight) && animal.position.follows(jungleLowerLeft) ){
                placesForPlantsJungle.put(animal.position, Boolean.TRUE);
            }
            else{
                placesForPlantsOutside.put(animal.position, Boolean.TRUE);
            }
        }
    }

    public void placeAnimalOnList(Animal animal){
        this.animalsList.add(animal);
    }

    public void removeAnimalOnList(Animal animal){
        this.animalsList.remove(animal);
    }

    public Plant plantsAt(Vector2d position){
        return plantsMap.get( position );
    }

    private void placePlant(Plant plant){
        plantsMap.put( plant.position, plant );
    }

    public void placePlant(Vector2d position, int energy){
        Plant currentPlant = new Plant(position, energy);
        placePlant(currentPlant);
    }

    public void removePlant(Plant plant){
        plantsMap.remove( plant.position );
    }
}
