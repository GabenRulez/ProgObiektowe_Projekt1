import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import simulations.SimpleSimulation;
import utilities.MapVisualizer;

import java.io.FileReader;
import java.io.IOException;

public class World {

    public static void main(String[] args){

        System.out.println("Starting the program...");
        System.out.println("Animals are displayed as arrows, plants are displayed as '@'.");

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("parameters.json"));

            int width =         (int)(long)     jsonObject.get("width");
            int height =        (int)(long)     jsonObject.get("height");
            int startEnergy =   (int)(long)     jsonObject.get("startEnergy");
            int moveEnergy =    (int)(long)     jsonObject.get("moveEnergy");
            int plantEnergy =   (int)(long)     jsonObject.get("plantEnergy");
            float jungleRatio = (float)(double) jsonObject.get("jungleRatio");
            int spawnAnimals =  (int)(long)     jsonObject.get("spawnAnimals");
            int maxIteration =  (int)(long)     jsonObject.get("iterations");
            int timeout =       (int)(long)     jsonObject.get("timeoutBetweenFrames");

            SimpleSimulation symulacja = new SimpleSimulation( width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, spawnAnimals);

            MapVisualizer visualizer = new MapVisualizer(symulacja);

            for(int i=0; i<maxIteration; i++){
                Thread.sleep(timeout);
                symulacja.newDay();
                visualizer.drawMap();
            }

        }
        catch(IOException | ParseException exception){
            System.err.println("Plik json nie istnieje!");
        } catch (InterruptedException e) {
            System.err.println("Funkcja Sleep zgłosiła błąd.");
        }
    }
}
