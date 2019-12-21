package entities;

import maps.FoldableMap;
import utilities.Vector2d;
import utilities.direction8Way;
import utilities.genes32;

public class Animal {

    public int energy;
    public final int initialEnergy;

    public direction8Way orientation;
    public Vector2d position;
    public genes32 genes;

    private FoldableMap map;

    public Animal(FoldableMap map, int startEnergy){
        this.map = map;
        this.orientation = new direction8Way();
        this.energy = startEnergy;
        this.initialEnergy = startEnergy;

        this.genes = new genes32();

        Vector2d tempPosition = new Vector2d((int)(map.width * Math.random()), (int)(map.height * Math.random()));
        while( map.animalsAt(tempPosition) != null ){
            tempPosition = new Vector2d((int)(map.width * Math.random()), (int)(map.height * Math.random()));
        }
        this.position = tempPosition;

        this.map.placeAnimalOnMap(this);
        this.map.placeAnimalOnList(this);
    }

    public Animal(FoldableMap map, int startEnergy, int currentEnergy, Vector2d position, genes32 genes){
        this(map, startEnergy);
        this.energy = currentEnergy;
        this.position = position;
        this.genes = genes;
    }

    public String toString(){
        return orientation.toString();
    }

    public void move(){
        map.removeAnimalOnMap(this);
        this.turnRandom();
        this.position = this.position.add( this.orientation.toUnitVector() );

            if(this.position.x >= this.map.width) this.position = this.position.subtract(this.map.jumpAcrossWidth);
            else if(this.position.x < 0) this.position = this.position.add(this.map.jumpAcrossWidth);

            if(this.position.y >= this.map.height) this.position = this.position.subtract(this.map.jumpAcrossWidth);
            else if(this.position.y < 0) this.position = this.position.add(this.map.jumpAcrossHeight);

        map.placeAnimalOnMap(this);
    }

    private void turnRandom(){
        this.orientation.turn( this.genes.geneIndex( (int) Math.floor( 32 * Math.random() ) ) );
    }

    public boolean makeBaby(Animal mateFriend){
        if( this.energy < 0.5*this.initialEnergy || mateFriend.energy < 0.5*mateFriend.initialEnergy) return false;

        // look for a place to place animal////////
        direction8Way tempDirection = new direction8Way();
        int iterationsCounter = 0;
        while( map.animalsAt( this.position.add( tempDirection.toUnitVector() ) ) != null ){
            tempDirection.turn(1);
            iterationsCounter++;
            if(iterationsCounter > 8) return false;
        }
        Vector2d kidPosition = this.position.add( tempDirection.toUnitVector() );
        ///////////////////////////////////////////

        // create new genes for a kid//////////////
        int secondPart = 1 + (int) Math.floor( 30*Math.random() );         // should be [1,2,...,29,30] as to make thirdPart at least of length 1       // secondPart i thirdPart są indeksem pierwszego miejsca w nowej części
        int thirdPart = secondPart + 1 + (int) Math.floor( (32-secondPart)*Math.random() );

        genes32 kidGenes = new genes32();
        kidGenes.addGenesParts(this.genes.getGenesBetween( 0, secondPart-1 ), mateFriend.genes.getGenesBetween( secondPart, thirdPart-1 ), this.genes.getGenesBetween( thirdPart, 31 ));
        kidGenes.repairGenes();
        ///////////////////////////////////////////

        Animal dumbKid = new Animal(this.map, this.initialEnergy, (this.energy+mateFriend.energy)/4, kidPosition, kidGenes);
        this.energy = 3 * this.energy/4;
        mateFriend.energy = 3 * mateFriend.energy / 4;
        this.map.placeAnimalOnMap(dumbKid);
        this.map.placeAnimalOnList(dumbKid);

        return true;
    }

    public void updateEnergy(int difference){
        this.energy += difference;
    }



}

