package levels;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game; // referința către joc
    private BufferedImage[] levelSprite; // array de imagini pentru sprite-urile nivelului
    private ArrayList<Level> levels; // lista de nivele
    public int lvlIndex; // indexul nivelului curent

    // constructorul clasei LevelManager
    public LevelManager(Game game, int niv){
        this.game = game; // inițializarea jocului
        lvlIndex = niv; // inițializarea indexului nivelului
        importOutsideSprites(); // importarea sprite-urilor din exterior
        levels = new ArrayList<>(); // inițializarea listei de nivele
        buildAllLevels(); // construirea tuturor nivelurilor
    }

    // metoda pentru încărcarea următorului nivel
    public void loadNextLevel(){
        lvlIndex++; // incrementarea indexului nivelului
        if(lvlIndex >= levels.size()){ // verificarea dacă s-au terminat nivelele
            lvlIndex = 0; // resetarea indexului nivelului
            System.out.println("Game completed"); // afișarea mesajului de completare a jocului
            Gamestate.state = Gamestate.MENU; // schimbarea stării jocului la meniul principal
        }

        Level newLevel = levels.get(lvlIndex); // obținerea noului nivel
        game.getPlaying().getEnemyManager().loadEnemies(newLevel); // încărcarea inamicilor pentru noul nivel
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData()); // încărcarea datelor nivelului pentru jucător
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset()); // setarea offset-ului maxim pentru nivel
    }

    // metoda pentru construirea tuturor nivelurilor
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels(); // obținerea tuturor nivelurilor din imagini
        for(BufferedImage img : allLevels) // pentru fiecare imagine de nivel
            levels.add(new Level(img)); // adăugarea nivelului în lista de nivele
    }

    // metoda pentru importarea sprite-urilor din exterior
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS); // obținerea atlasului de sprite-uri

        levelSprite = new BufferedImage[4]; // inițializarea array-ului de sprite-uri
        for(int i = 0; i < 4; i++){
            levelSprite[i] = img.getSubimage(i*64, 0, 64, 64); // extragerea fiecărui sprite din atlas
        }
    }

    // metoda pentru desenarea nivelului
    public void draw(Graphics g, int xlvlOffset){
        for(int j = 0; j < Game.TILES_IN_HEIGHT; j++) // parcurgerea înălțimii nivelului
            for(int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++){ // parcurgerea lățimii nivelului
                int index = levels.get(lvlIndex).getSpriteIndex(i, j); // obținerea indexului sprite-ului
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xlvlOffset, Game.TILES_SIZE * j, null); // desenarea sprite-ului
            }
    }

    // metoda pentru actualizarea nivelului
    public void uppdate(){

    }

    // metoda pentru obținerea nivelului curent
    public Level getCurrentLevel(){
        return levels.get(lvlIndex); // returnarea nivelului curent
    }

    // metoda pentru obținerea numărului total de nivele
    public int getAmountOfLevels(){
        return levels.size(); // returnarea numărului total de nivele
    }

}