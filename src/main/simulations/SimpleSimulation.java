package simulations;

import entities.Animal;
import maps.FoldableMap;
import utilities.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class SimpleSimulation {

    private FoldableMap map;
    private int moveEnergy;
    private int plantEnergy;

    public SimpleSimulation(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio){
        this.map = new FoldableMap(width, height, plantEnergy, jungleRatio);
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;

        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
        new Animal(this.map, startEnergy);
    }

    public void newDay(){
        this.eraseTheDead();

        this.moveAllAnimals();
        this.feedAllAnimals();
        this.makeBabies();

        this.newPlantJungle();
        this.newPlantOutside();
    }

    private void eraseTheDead(){
        for(TreeSet<Animal> currentTreeSet : this.map.animalsMap.values()){
            Iterator<Animal> iteratorPoTreeSet = currentTreeSet.iterator();
            while(iteratorPoTreeSet.hasNext()){
                Animal currentAnimal = iteratorPoTreeSet.next();

                if(currentAnimal.energy <= 0){      // Usunięcie martwych zwierząt z mapy
                    System.out.println("An animal has died at " + currentAnimal.position);
                    this.map.removeAnimal(currentAnimal);
                }
            }
        }
    }


    private void moveAllAnimals(){
        for(TreeSet<Animal> currentTreeSet : this.map.animalsMap.values()){
            Iterator<Animal> iteratorPoTreeSet = currentTreeSet.iterator();
            while(iteratorPoTreeSet.hasNext()){
                Animal currentAnimal = iteratorPoTreeSet.next();

                currentAnimal.updateEnergy(-moveEnergy);        // Tutaj nie trzeba aktualizować struktury drzewa, bo jest zachowana (jeśli z każdego węzła usuwamy stałą liczbę to stosunki jednego do drugiego się nie zmieniają)
                currentAnimal.move();               // Zmiana kierunku zaimplementowana w poruszaniu się
            }
        }
    }

    private void feedAllAnimals() {
        for (Vector2d positionTreeSet : this.map.animalsMap.keySet()) {
            if (this.map.plantsAt(positionTreeSet) != null) {  // if plant exist at that position
                int maxEnergy = this.map.animalsMap.get(positionTreeSet).first().energy;         // set maxEnergy size

                ArrayList<Animal> animalsThatEat = new ArrayList<>();                   // create a List of maxEnergy Animals (to get amount of them + easier to iterate)
                for (Animal currentAnimal : this.map.animalsMap.get(positionTreeSet)) {
                    if (currentAnimal.energy != maxEnergy) break;
                    animalsThatEat.add(currentAnimal);

                    this.map.removeAnimal(currentAnimal);
                }
                for (Animal currentAnimal : animalsThatEat) {
                    currentAnimal.updateEnergy(this.map.plantsAt(positionTreeSet).energy / animalsThatEat.size());       // give all animals with most energy their part of food

                    this.map.placeAnimal(currentAnimal);
                }
                this.map.removePlant(this.map.plantsAt(positionTreeSet));
            }
        }
    }

    private void makeBabies(){
        for(Vector2d positionTreeSet : this.map.animalsMap.keySet()){

            if( this.map.animalsMap.get(positionTreeSet).size() >= 2 ){                // Making babies
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
