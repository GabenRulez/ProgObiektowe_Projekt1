package simulations;

import entities.Animal;
import maps.FoldableMap;
import utilities.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class SimpleSimulation {

    public FoldableMap map;
    private int moveEnergy;
    private int plantEnergy;

    public int currentDay;

    public SimpleSimulation(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio){
        this.map = new FoldableMap(width, height, plantEnergy, jungleRatio);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;

        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);

        System.out.println("Simulation environment created.");
    }

    public void newDay(){
        this.currentDay++;
        System.out.println();
        System.out.println("Alive : " + this.map.animalsList.size() + " , occupied spaces: " + this.map.animalsMap.size());
        System.out.println("Day " + this.currentDay + " started.");
        this.eraseTheDead();

        this.moveAllAnimals();
        this.feedAllAnimals();
        this.makeBabies();

        this.newPlantJungle();
        this.newPlantOutside();
        System.out.println("Day " + this.currentDay + " came to an end.");
        System.out.println("Alive : " + this.map.animalsList.size() + " , occupied spaces: " + this.map.animalsMap.size());
        System.out.println();
    }

    private void eraseTheDead(){
        ArrayList<Animal> toDelete = new ArrayList<>();

        for(Animal currentAnimal : this.map.animalsList ){
            if( currentAnimal.energy <= 0 ){
                toDelete.add(currentAnimal);
            }
        }
        for( Animal currentAnimal : toDelete ){
            System.out.println("An animal has died at " + currentAnimal.position + ".");

            this.map.removeAnimalOnMap(currentAnimal);
            this.map.removeAnimalOnList(currentAnimal);
        }
    }

    private void moveAllAnimals(){
        for(Animal currentAnimal : this.map.animalsList){
            currentAnimal.updateEnergy(-moveEnergy);        // Tutaj nie trzeba aktualizować struktury drzewa, bo jest zachowana (jeśli z każdego węzła usuwamy stałą liczbę to stosunki jednego do drugiego się nie zmieniają)
            currentAnimal.move();               // Zmiana kierunku zaimplementowana w poruszaniu się
        }
    }

    private void feedAllAnimals() {
        ArrayList<Vector2d> positionsOfTreeSets = new ArrayList<>(this.map.animalsMap.keySet());

        for (Vector2d positionTreeSet : positionsOfTreeSets) {         // TODO spróbuj skopiować pozycje do oddzielnej listy, może tym sposobem pozbędziesz się concurrentModificationError
            if (this.map.plantsAt(positionTreeSet) != null) {  // if plant exist at that position
                int maxEnergy = this.map.animalsMap.get(positionTreeSet).first().energy;         // set maxEnergy size

                ArrayList<Animal> animalsThatEat = new ArrayList<>();                   // create a List of maxEnergy Animals (to get amount of them + easier to iterate)
                for (Animal currentAnimal : this.map.animalsMap.get(positionTreeSet)) {
                    if (currentAnimal.energy != maxEnergy) break;
                    animalsThatEat.add(currentAnimal);
                }
                for (Animal currentAnimal : animalsThatEat) {
                    currentAnimal.updateEnergy(this.map.plantsAt(positionTreeSet).energy / animalsThatEat.size());       // give all animals with most energy their part of food

                    this.map.removeAnimalOnMap(currentAnimal);
                    this.map.placeAnimalOnMap(currentAnimal);

                    System.out.println("An animal ate some plant. It gave him " + this.map.plantsAt(positionTreeSet).energy / animalsThatEat.size() + " energy. Now he has: " + currentAnimal.energy + ".");
                }
                this.map.removePlant(this.map.plantsAt(positionTreeSet));
            }
        }
    }

    private void makeBabies(){
        ArrayList<Vector2d> positions = new ArrayList<>(this.map.animalsMap.keySet());

        for( Vector2d positionTreeSet : positions ){

            if( this.map.animalsMap.get(positionTreeSet).size() >= 2 ){
                TreeSet<Animal> currentTreeSet = this.map.animalsMap.get(positionTreeSet);

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
    }

    private void newPlantJungle(){
        if( this.map.placesForPlantsJungle.isEmpty() == false ){
            int junglePlantPositionNumber = (int) Math.floor(this.map.placesForPlantsJungle.size() * Math.random());

            int i = 0;
            for( Vector2d currentPosition : this.map.placesForPlantsJungle.keySet() ){
                if(i == junglePlantPositionNumber ){
                    this.map.placePlant(currentPosition, plantEnergy);
                    this.map.placesForPlantsJungle.remove(currentPosition);
                    break;
                }
                i++;
            }
        }
    }

    private void newPlantOutside(){
        if( this.map.placesForPlantsOutside.isEmpty() == false ){
            int outsidePlantPositionNumber = (int) Math.floor(this.map.placesForPlantsOutside.size() * Math.random());

            int i = 0;
            for( Vector2d currentPosition : this.map.placesForPlantsOutside.keySet() ){
                if(i == outsidePlantPositionNumber ){
                    this.map.placePlant(currentPosition, plantEnergy);
                    this.map.placesForPlantsOutside.remove(currentPosition);
                    break;
                }
                i++;
            }
        }
    }



}
