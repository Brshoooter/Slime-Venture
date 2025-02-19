package levels;

import entities.Robbot;
import main.Game;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetRobots;

public class Level {

    private BufferedImage img; // imaginea nivelului
    private int[][] lvlData; // datele nivelului (matricea de tiles)
    private ArrayList<Robbot> robots; // lista de roboți inamici
    private int lvlTilesWide; // lățimea nivelului în tiles
    private int maxTilesOffset; // offset-ul maxim în tiles
    private int maxLvlOffsetX; // offset-ul maxim pe axa x

    // constructorul clasei level
    public Level(BufferedImage img) {
        this.img = img; // inițializarea imaginii nivelului
        createLvlData(); // crearea datelor nivelului
        createEnemies(); // crearea inamicilor
        calcLvlOffsets(); // calcularea offset-urilor nivelului
    }

    // metoda pentru calcularea offset-urilor nivelului
    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth(); // obținerea lățimii nivelului în tiles
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH; // calcularea offset-ului maxim în tiles
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset; // calcularea offset-ului maxim pe axa x
    }

    // metoda pentru crearea inamicilor
    private void createEnemies() {
        robots = GetRobots(img); // obținerea listei de roboți din imagine
    }

    // metoda pentru crearea datelor nivelului
    private void createLvlData() {
        lvlData = GetLevelData(img); // obținerea datelor nivelului din imagine
    }

    // metoda pentru obținerea indexului sprite-ului la o anumită poziție
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x]; // returnarea indexului sprite-ului din matricea de tiles
    }

    // metoda pentru obținerea datelor nivelului
    public int[][] getLvlData(){
        return lvlData; // returnarea matricei de tiles
    }

    // metoda pentru obținerea offset-ului maxim pe axa x
    public int getLvlOffset(){
        return maxLvlOffsetX; // returnarea offset-ului maxim pe axa x
    }

    // metoda pentru obținerea listei de roboți
    public ArrayList<Robbot> getRobots(){
        return robots; // returnarea listei de roboți
    }
}
