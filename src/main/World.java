import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import simulations.SimpleSimulation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class World {

    public static void main(String[] args){

        System.out.println("Starting the program...");
        System.out.println("...");

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("parameters.json"));

            int width =         (int)(long)     jsonObject.get("width");
            int height =        (int)(long)     jsonObject.get("height");
            int startEnergy =   (int)(long)     jsonObject.get("startEnergy");
            int moveEnergy =    (int)(long)     jsonObject.get("moveEnergy");
            int plantEnergy =   (int)(long)     jsonObject.get("plantEnergy");
            float jungleRatio = (float)(double) jsonObject.get("jungleRatio");

            SimpleSimulation symulacja = new SimpleSimulation( width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio);

            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();
            symulacja.newDay();




        }
        catch(IOException | ParseException exception){
            System.err.println("Plik json nie istnieje!");
        }
    }
}
