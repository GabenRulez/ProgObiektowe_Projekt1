package utilities;

import entities.Animal;

import java.util.Comparator;

public class energyComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal first, Animal second){
        if(first.energy > second.energy) return -1;
        if ( first.equals(second) ) return 0;
        return 1;
    }
}
