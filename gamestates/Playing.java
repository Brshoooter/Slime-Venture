package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Playing extends State implements Statemethods{
    private Player player; // referința către obiectul jucătorului
    public LevelManager levelManager; // managerul nivelurilor
    private EnemyManager enemyManager; // managerul inamicilor
    private PauseOverlay pauseOverlay; // overlay-ul pentru pauză
    private GameOverOverlay gameOverOverlay; // overlay-ul pentru sfârșitul jocului
    private LevelCompletedOverlay levelCompletedOverlay; // overlay-ul pentru finalizarea nivelului
    private boolean paused = false; // flag pentru a indica dacă jocul este în pauză

    private int xLvlOffset; // offset-ul nivelului pe axa x
    private int leftBorder = (int)(0.4 * Game.GAME_WIDTH); // marginea stângă a ecranului
    private int rightBorder = (int)(0.6 * Game.GAME_WIDTH); // marginea dreaptă a ecranului
    private int maxLvlOffsetX; // valoarea maximă a offset-ului nivelului pe axa x

    private BufferedImage backgroundImg; // imaginea de fundal a ecranului de joc

    private boolean gameOver; // flag pentru a indica dacă jocul s-a încheiat
    private boolean lvlCompleted = false; // flag pentru a indica dacă nivelul a fost completat

    // constructorul clasei playing
    public Playing(Game game, int niv) {
        super(game); // apelarea constructorului clasei de bază
        initClasses(niv); // inițializarea obiectelor și a variabilelor necesare

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG); // încărcarea imaginii de fundal

        calcLvlOffset(); // calcularea offset-ului nivelului
        loadStartLevel(); // încărcarea nivelului de start
    }

    // metoda pentru încărcarea următorului nivel
    public void loadNextLevel(){
        resetAll(); // resetarea jocului
        levelManager.loadNextLevel(); // încărcarea următorului nivel
    }

    // metoda pentru încărcarea nivelului de start
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel()); // încărcarea inamicilor pentru nivelul curent
    }

    // metoda pentru calcularea offset-ului nivelului
    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset(); // obținerea offset-ului maxim al nivelului
    }

    // metoda pentru inițializarea claselor necesare
    private void initClasses(int niv) {
        levelManager = new LevelManager(game, niv); // inițializarea managerului de niveluri
        enemyManager = new EnemyManager(this); // inițializarea managerului de inamici
        player = new Player(200,200, (int)(Game.SCALE*128),(int)(Game.SCALE*128), this); // inițializarea jucătorului
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData()); // încărcarea datelor nivelului pentru jucător
        pauseOverlay = new PauseOverlay(this); // inițializarea overlay-ului pentru pauză
        gameOverOverlay = new GameOverOverlay(this); // inițializarea overlay-ului pentru sfârșitul jocului
        levelCompletedOverlay = new LevelCompletedOverlay(this); // inițializarea overlay-ului pentru finalizarea nivelului
    }

    // metoda pentru actualizarea stării jocului
    @Override
    public void uppdate() {
        if (paused) { // verificarea dacă jocul este în pauză
            pauseOverlay.update(); // actualizarea overlay-ului pentru pauză
        } else if (lvlCompleted) { // verificarea dacă nivelul a fost completat
            levelCompletedOverlay.update(); // actualizarea overlay-ului pentru finalizarea nivelului
        } else if (!gameOver) { // verificarea dacă jocul nu s-a încheiat
            levelManager.uppdate(); // actualizarea managerului de niveluri
            player.update(); // actualizarea jucătorului
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player); // actualizarea managerului de inamici
            checkCloseToBorder(); // verificarea apropierii de marginile ecranului
        }
    }

    // metoda pentru verificarea apropierii de marginile ecranului
    private void checkCloseToBorder() {
        int playerX = (int)player.getHitbox().x; // obținerea poziției jucătorului pe axa x
        int diff = playerX - xLvlOffset; // calcularea diferenței dintre poziția jucătorului și offset-ul nivelului

        if(diff > rightBorder) // verificarea dacă jucătorul este aproape de marginea dreaptă
            xLvlOffset += diff - rightBorder; // ajustarea offset-ului nivelului
        else if(diff < leftBorder) // verificarea dacă jucătorul este aproape de marginea stângă
            xLvlOffset += diff - leftBorder; // ajustarea offset-ului nivelului

        if(xLvlOffset > maxLvlOffsetX) // verificarea dacă offset-ul depășește valoarea maximă
            xLvlOffset = maxLvlOffsetX; // setarea offset-ului la valoarea maximă
        else if(xLvlOffset < 0) // verificarea dacă offset-ul este mai mic decât 0
            xLvlOffset = 0; // setarea offset-ului la 0
    }

    // metoda pentru desenarea stării jocului
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg,0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null); // desenarea imaginii de fundal

        levelManager.draw(g,xLvlOffset); // desenarea nivelului
        player.render(g, xLvlOffset); // desenarea jucătorului
        enemyManager.draw(g, xLvlOffset); // desenarea inamicilor

        if(paused) { // verificarea dacă jocul este în pauză
            g.setColor(new Color(0,0,0,100)); // setarea culorii pentru overlay-ul de pauză
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT); // desenarea unui dreptunghi transparent
            pauseOverlay.draw(g); // desenarea overlay-ului pentru pauză
        } else if(gameOver) { // verificarea dacă jocul s-a încheiat
            gameOverOverlay.draw(g); // desenarea overlay-ului pentru sfârșitul jocului
        } else if(lvlCompleted) { // verificarea dacă nivelul a fost completat
            levelCompletedOverlay.draw(g); // desenarea overlay-ului pentru finalizarea nivelului
        }
    }

    // metoda pentru resetarea stării jocului
    public void resetAll(){
        gameOver = false; // resetarea flag-ului pentru sfârșitul jocului
        paused = false; // resetarea flag-ului pentru pauză
        lvlCompleted = false; // resetarea flag-ului pentru finalizarea nivelului
        player.resetAll(); // resetarea jucătorului
        enemyManager.resetAllEnemies(); // resetarea inamicilor
    }

    // metoda pentru setarea stării de sfârșit de joc
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    // metoda pentru verificarea lovirii inamicilor
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox); // verificarea lovirii inamicilor
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // metoda goală pentru evenimentul de clic mouse
    }

    public void mouseDragged(MouseEvent e){
        if(!gameOver) { // verificarea dacă jocul nu s-a încheiat
            if(paused) // verificarea dacă jocul este în pauză
                pauseOverlay.mouseDragged(e); // apelarea metodei mouseDragged din overlay-ul de pauză
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) { // verificarea dacă jocul nu s-a încheiat
            if (paused) // verificarea dacă jocul este în pauză
                pauseOverlay.mousePressed(e); // apelarea metodei mousePressed din overlay-ul de pauză
            else if (lvlCompleted) // verificarea dacă nivelul a fost completat
                levelCompletedOverlay.mousePressed(e); // apelarea metodei mousePressed din overlay-ul de finalizare a nivelului
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) { // verificarea dacă jocul nu s-a încheiat
            if (paused) // verificarea dacă jocul este în pauză
                pauseOverlay.mouseReleased(e); // apelarea metodei mouseReleased din overlay-ul de pauză
            else if (lvlCompleted) // verificarea dacă nivelul a fost completat
                levelCompletedOverlay.mouseReleased(e); // apelarea metodei mouseReleased din overlay-ul de finalizare a nivelului
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) { // verificarea dacă jocul nu s-a încheiat
            if (paused) // verificarea dacă jocul este în pauză
                pauseOverlay.mouseMoved(e); // apelarea metodei mouseMoved din overlay-ul de pauză
            else if (lvlCompleted) // verificarea dacă nivelul a fost completat
                levelCompletedOverlay.mouseMoved(e); // apelarea metodei mouseMoved din overlay-ul de finalizare a nivelului
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) // verificarea dacă jocul s-a încheiat
            gameOverOverlay.keyPressed(e); // apelarea metodei keyPressed din overlay-ul de sfârșit de joc
        else
            switch (e.getKeyCode()){
                case KeyEvent.VK_A: // verificarea dacă a fost apăsată tasta A
                    player.setLeft(true); // setarea direcției jucătorului spre stânga
                    break;
                case KeyEvent.VK_D: // verificarea dacă a fost apăsată tasta D
                    player.setRight(true); // setarea direcției jucătorului spre dreapta
                    break;
                case KeyEvent.VK_W: // verificarea dacă a fost apăsată tasta W
                    player.setJump(true); // setarea săriturii jucătorului
                    break;
                case KeyEvent.VK_K: // verificarea dacă a fost apăsată tasta K
                    player.setAttacking(true); // setarea atacului jucătorului
                    break;
                case KeyEvent.VK_ESCAPE: // verificarea dacă a fost apăsată tasta Escape
                    paused = !paused; // schimbarea stării de pauză
                    break;
                case KeyEvent.VK_O: // verificarea dacă a fost apăsată tasta O
                    game.save.insertData(levelManager.lvlIndex); // salvarea datelor jocului
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver) { // verificarea dacă jocul nu s-a încheiat
            switch (e.getKeyCode()){
                case KeyEvent.VK_A: // verificarea dacă a fost eliberată tasta A
                    player.setLeft(false); // setarea direcției jucătorului
                    break;
                case KeyEvent.VK_D: // verificarea dacă a fost eliberată tasta D
                    player.setRight(false); // setarea direcției jucătorului
                    break;
                case KeyEvent.VK_W: // verificarea dacă a fost eliberată tasta W
                    player.setJump(false); // setarea săriturii jucătorului
                    break;
            }
        }
    }

    // metoda pentru setarea stării de finalizare a nivelului
    public void setLevelCompleted(boolean lvlCompleted){
        this.lvlCompleted = lvlCompleted;
    }

    // metoda pentru setarea offset-ului maxim al nivelului
    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    // metoda pentru deblocarea jocului din pauză
    public void unpauseGame(){
        paused = false;
    }

    // metoda pentru resetarea direcțiilor jucătorului la pierderea focusului ferestrei
    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    // metoda pentru obținerea obiectului jucătorului
    public Player getPlayer(){
        return player;
    }

    // metoda pentru obținerea managerului de inamici
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }
}