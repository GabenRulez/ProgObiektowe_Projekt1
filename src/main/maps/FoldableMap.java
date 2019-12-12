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
    private HashMap<Vector2d, TreeSet<Animal>> animalsMap = new HashMap<>();
    private HashMap<Vector2d, Plant> plantsMap = new HashMap<>();

    private Vector2d jungleLowerLeft;
    private Vector2d jungleUpperRight;

    public FoldableMap(int width, int height, float jungleRatio){
        this.width = width;
        this.height = height;

        this.jumpAcrossWidth = new Vector2d(width, 0);
        this.jumpAcrossHeight = new Vector2d(0, height);

        double jungleRatio1D = Math.sqrt(jungleRatio);
        int jungleWidth =   (int) Math.floor( this.width    * jungleRatio1D );
        int jungleHeight =  (int) Math.floor( this.height   * jungleRatio1D );

        jungleLowerLeft = new Vector2d((this.width - jungleWidth) / 2, (this.height - jungleHeight)/2 );
        jungleUpperRight = jungleLowerLeft.add( new Vector2d(jungleWidth, jungleHeight) );
    }


    public TreeSet<Animal> animalsAt(Vector2d position){
        return animalsMap.get( position );
    }

    public void placeAnimal(Animal animal){
        if ( animalsMap.get(animal.position) == null ){
            TreeSet<Animal> listOfAnimalsAtPosition = new TreeSet<>(new energyComparator());
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

    public void simulateNextDay(int energyPerDay){

        for(TreeSet<Animal> currentTreeSet : animalsMap.values()){
            Iterator<Animal> iteratorPoTreeSet = currentTreeSet.iterator();
            while(iteratorPoTreeSet.hasNext()){
                Animal currentAnimal = iteratorPoTreeSet.next();

                if(currentAnimal.energy <= 0){      // Usunięcie martwych zwierząt z mapy
                    System.out.println("An animal has died at " + currentAnimal.position);
                    this.removeAnimal(currentAnimal);
                }

                currentAnimal.updateEnergy(-energyPerDay);
                currentAnimal.move();               // Zmiana kierunku zaimplementowana w poruszaniu się
            }
        }



        for(Vector2d positionTreeSet :animalsMap.keySet()){

            if(plantsAt(positionTreeSet) != null){  // if plant exist at that position
                int maxEnergy = animalsMap.get(positionTreeSet).first().energy;         // set maxEnergy size

                ArrayList<Animal> animalsThatEat = new ArrayList<>();                   // create a List of maxEnergy Animals (to get amount of them + easier to iterate)
                for(Animal currentAnimal : animalsMap.get(positionTreeSet)){
                    if(currentAnimal.energy != maxEnergy) break;
                    animalsThatEat.add(currentAnimal);
                }
                for(Animal currentAnimal : animalsThatEat){
                    currentAnimal.updateEnergy(plantsAt(positionTreeSet).energy / animalsThatEat.size());       // give all animals with most energy their part of food
                }
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
        // TODO Lista która jest aktualizowana miejsc na ktorych mozemy postawic roślinkę (tj. miejsc w ktorych nie ma roślinki, ani nie stoi na niej żadne zwierze)





    }
}
