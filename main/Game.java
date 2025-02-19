package main;

import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    public Save save;

    public final static int TILES_DEFAULT_SIZE = 64;
    public final static float SCALE = 1.0f;
    public final static int TILES_IN_WIDTH = 23;
    public final static int TILES_IN_HEIGHT = 12;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public int niv;

    public Game() {
        save = new Save();
        save.bazaDeDate(); // initializeaza baza de date
        getDateDB(); // obtine datele din baza de date
        initClasses(); // initializeaza clasele pentru joc

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        StartGameLoop(); // porneste bucla jocului
    }

    private void initClasses() {
        menu = new Menu(this); // initializeaza meniul
        playing = new Playing(this, niv); // initializeaza modul de joc cu nivelul curent
    }

    private void StartGameLoop() {
        gameThread = new Thread(this);
        gameThread.start(); // porneste firul de executie al jocului
    }

    public void update() {
        // actualizeaza starea jocului in functie de starea curenta
        switch (Gamestate.state) {
            case PLAYING:
                playing.uppdate(); // actualizeaza starea jocului cand se joaca
                break;
            case MENU:
                menu.uppdate(); // actualizeaza starea meniului
                break;
            case OPTIONS:
                save.insertData(0); // salveaza datele in baza de date
                niv = 0;
                playing.levelManager.lvlIndex = -1; // reseteaza indexul nivelului
                Gamestate.state = Gamestate.PLAYING; // schimba starea la joc
                playing.levelManager.loadNextLevel(); // incarca urmatorul nivel
                break;
            default:
                break;
        }
    }

    public void render(Graphics g) {
        // deseneaza pe ecran in functie de starea curenta a jocului
        switch (Gamestate.state) {
            case PLAYING:
                playing.draw(g); // deseneaza jocul cand se joaca
                break;
            case MENU:
                menu.draw(g); // deseneaza meniul
                break;
            case OPTIONS:
                break;
            case QUIT:
            default:
                System.exit(0); // inchide jocul
                break;
        }
    }

    @Override
    public void run() {
        // bucla principala a jocului
        double timePerFrame = 1000000000.0 / FPS_SET; // timpul per cadru
        double timePerUppdate = 1000000000.0 / UPS_SET; // timpul per actualizare

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCHeck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long curentTime = System.nanoTime();

            deltaU += (curentTime - previousTime) / timePerUppdate;
            deltaF += (curentTime - previousTime) / timePerFrame;
            previousTime = curentTime;

            if (deltaU >= 1) {
                update(); // actualizeaza jocul
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint(); // redeseneaza panelul jocului
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCHeck >= 1000) {
                lastCHeck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "  | UPS: " + updates);
                updates = 0;
                frames = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING) {
            playing.getPlayer().resetDirBooleans(); // reseteaza directiile jucatorului daca fereastra pierde focusul
        }
    }

    public Menu getMenu() {
        return menu; // returneaza meniul
    }

    public Playing getPlaying() {
        return playing; // returneaza modul de joc
    }

    public void getDateDB() {
        List<String[]> data = Save.getData(); // obtine datele din baza de date

        if (data.isEmpty()) {
            System.out.println("No data available in the database.");
            return;
        }

        String[] lastEntry = data.get(data.size() - 1);
        System.out.println("Last entry: " + Arrays.toString(lastEntry));

        if (lastEntry.length != 1) {
            System.out.println("Data entry does not have the expected number of columns.");
            return;
        }

        niv = Integer.parseInt(lastEntry[0]); // seteaza nivelul curent

    }

}
