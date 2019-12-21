package maps;

import entities.Animal;
import entities.Plant;
import utilities.Vector2d;
import utilities.energyComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class FoldableMap {

    public final int width;
    public final int height;

    public final Vector2d jumpAcrossWidth;
    public final Vector2d jumpAcrossHeight;

    //komparator powinien: ustawiac w kolejności: najsilniejsze (pod względem energii) zwierze, ... , najsłabsze zwierze
    public HashMap<Vector2d, TreeSet<Animal>>   animalsMap  = new HashMap<>();
    public HashMap<Vector2d, Plant>             plantsMap   = new HashMap<>();
    public ArrayList<Animal>                    animalsList = new ArrayList<>();

    public HashMap<Vector2d, Boolean> placesForPlantsJungle;
    public HashMap<Vector2d, Boolean> placesForPlantsOutside;

    private int plantEnergy;


    private Vector2d jungleLowerLeft;
    private Vector2d jungleUpperRight;

    public FoldableMap(int width, int height, int plantEnergy, float jungleRatio){
        this.width = width;
        this.height = height;
        this.plantEnergy = plantEnergy;

        this.jumpAcrossWidth = new Vector2d(width, 0);
        this.jumpAcrossHeight = new Vector2d(0, height);

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

        if ( treesetOfAnimalsAtPosition.isEmpty() ){
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

    public void placePlant(Plant plant){
        plantsMap.put( plant.position, plant );
    }

    public void placePlant(Vector2d position, int energy){
        Plant currentPlant = new Plant(position, energy);
        placePlant(currentPlant);
    }

    public void removePlant(Plant plant){
        plantsMap.remove( plant.position );
    }

    public void simulateNextDay(int energyPerDay){          // TODO rozdzielić usuwanie, ruszanie zwierząt na oddzielne podmetody -> liczy się czytelność, nie super optymalizacja

        for(TreeSet<Animal> currentTreeSet : animalsMap.values()){
            Iterator<Animal> iteratorPoTreeSet = currentTreeSet.iterator();
            while(iteratorPoTreeSet.hasNext()){
                Animal currentAnimal = iteratorPoTreeSet.next();

                if(currentAnimal.energy <= 0){      // Usunięcie martwych zwierząt z mapy
                    System.out.println("An animal has died at " + currentAnimal.position);
                    this.removeAnimalOnMap(currentAnimal);
                }

                currentAnimal.updateEnergy(-energyPerDay);
                currentAnimal.move();               // Zmiana kierunku zaimplementowana w poruszaniu się
            }
        }



        for(Vector2d positionTreeSet : animalsMap.keySet()){

            if(plantsAt(positionTreeSet) != null){  // if plant exist at that position
                int maxEnergy = animalsMap.get(positionTreeSet).first().energy;         // set maxEnergy size

                ArrayList<Animal> animalsThatEat = new ArrayList<>();                   // create a List of maxEnergy Animals (to get amount of them + easier to iterate)
                for(Animal currentAnimal : animalsMap.get(positionTreeSet)){
                    if(currentAnimal.energy != maxEnergy) break;
                    animalsThatEat.add(currentAnimal);
                    this.removeAnimalOnMap(currentAnimal);
                }
                for(Animal currentAnimal : animalsThatEat){
                    currentAnimal.updateEnergy(plantsAt(positionTreeSet).energy / animalsThatEat.size());       // give all animals with most energy their part of food
                    this.placeAnimalOnMap(currentAnimal);
                }
                this.removePlant(plantsAt(positionTreeSet));
            }

            if(animalsMap.get(positionTreeSet).size() >= 2){                // Making babies
                TreeSet<Animal> currentTreeSet = animalsMap.get(positionTreeSet);

                Iterator<Animal> iteratorPoTreeSet = currentTreeSet.iterator();
                Animal firstAnimal = iteratorPoTreeSet.next();
                Animal secondAnimal = iteratorPoTreeSet.next();

                if( firstAnimal.energy > secondAnimal.energy ){
                    firstAnimal.makeBaby(secondAnimal);
                }
                else{
                    int maxEnergy = firstAnimal.energy;

                    ArrayList<Animal> animalsThatMakeBabies = new ArrayList<>();
                    for(Animal currentAnimal : currentTreeSet){
                        if( currentAnimal.energy < maxEnergy ) break;
                        animalsThatMakeBabies.add(currentAnimal);
                    }

                    int randomNumber1 = (int) Math.floor(animalsThatMakeBabies.size() * Math.random());
                    int randomNumber2 = (int) Math.floor(animalsThatMakeBabies.size() * Math.random());
                    while( randomNumber1 == randomNumber2 ) randomNumber2 = (int) Math.floor(animalsThatMakeBabies.size() * Math.random());

                    animalsThatMakeBabies.get(randomNumber1).makeBaby( animalsThatMakeBabies.get(randomNumber2) );
                }
            }
        }

        //// Now create new plants

        if( placesForPlantsJungle.isEmpty() == false ){
            int junglePlantPositionNumber = (int) Math.floor(placesForPlantsJungle.size() * Math.random());

            int i = 0;
            for( Vector2d currentPosition : placesForPlantsJungle.keySet() ){
                if(i == junglePlantPositionNumber ){
                    placePlant(currentPosition, plantEnergy);
                    placesForPlantsJungle.remove(currentPosition);
                    break;
                }
                i++;
            }
        }

        if( placesForPlantsOutside.isEmpty() == false ){
            int outsidePlantPositionNumber = (int) Math.floor(placesForPlantsOutside.size() * Math.random());

            int i = 0;
            for( Vector2d currentPosition : placesForPlantsOutside.keySet() ){
                if(i == outsidePlantPositionNumber ){
                    placePlant(currentPosition, plantEnergy);
                    placesForPlantsOutside.remove(currentPosition);
                    break;
                }
                i++;
            }
        }




    }
}
