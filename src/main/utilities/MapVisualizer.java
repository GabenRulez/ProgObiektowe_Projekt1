package utilities;      // Created by Wojciech K

import entities.Animal;
import maps.FoldableMap;
import simulations.SimpleSimulation;

public class MapVisualizer {
    SimpleSimulation simulation;
    FoldableMap map;
    int lineWidth;
    private String lineOfHash = "";

    public MapVisualizer(SimpleSimulation simulation){
        this.simulation = simulation;
        this.map = simulation.map;
        this.lineWidth = this.map.width;

        for(int i=0; i<this.lineWidth; i++) lineOfHash += "#";
    }

    public void drawMap(){
        this.lineOfHash();
        this.writeCenter("Dzień " + this.simulation.currentDay);
        this.lineOfHash();
        this.drawCorrectMap();
        this.lineOfHash();
        this.writeAllAnimalsOut();
        this.lineOfHash();

    }

    public void lineOfHash(){ System.out.println("#" + lineOfHash + "#"); }

    public void writeCenter(String text){
        int spacesInFront = ( this.lineWidth - text.length() )/2;
        String temp = "";
        for(int i=0; i<spacesInFront; i++) temp += " ";

        if(text.length() %2 ==1){
            System.out.println("#" + temp + text + temp + " #");
        }
        else{
            System.out.println("#" + temp + text + temp + "#");
        }
    }

    public void drawCorrectMap(){
        for(int i = this.map.height-1; i>=0; i--){
            this.drawMapRow(i);
        }
    }

    public void drawMapRow(int rowNumber){
        String tempString = "";

        for(int i=0; i<this.lineWidth; i++){
            if (this.map.animalsAt(new Vector2d(i, rowNumber)) != null){
                tempString += this.map.animalsAt(new Vector2d(i, rowNumber)).first().orientation.toArrow();
            }
            else if(this.map.plantsAt(new Vector2d(i, rowNumber)) != null){
                tempString += "@";
            }
            else tempString += " ";
        }

        System.out.println("#" + tempString + "#");
    }

    public void writeAllAnimalsOut(){
        boolean flag = false;
        for(Animal animal : this.map.animalsList){
            this.writeCenter("Animal at " + animal.position.toString() + ", rot: " + animal.orientation + ", energy: " + animal.energy);
            flag = true;
        }
        if (flag == false) this.writeCenter("No animals are alive.");
    }



}
