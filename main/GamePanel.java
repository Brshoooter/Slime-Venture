package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

// clasa GamePanel extinde clasa JPanel si reprezinta panoul principal al jocului
public class GamePanel extends JPanel {

    private Game game; // referinta catre obiectul Game asociat panoului

    // constructorul clasei
    public GamePanel(Game game){

        this.game = game; // initializarea referintei catre obiectul Game

        setPanelSize(); // initializarea dimensiunilor panoului
        addKeyListener(new KeyboardInputs(this)); // adaugarea unui ascultator de evenimente de tastatura
        addMouseListener(new MouseInputs(this)); // adaugarea unui ascultator de evenimente de mouse
        addMouseMotionListener(new MouseInputs(this)); // adaugarea unui ascultator de miscare a mouse-ului

    }

    // metoda privata pentru initializarea dimensiunilor panoului
    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT); // crearea unei dimensiuni conform dimensiunilor jocului
        setPreferredSize(size); // setarea dimensiunilor preferate ale panoului
    }

    // metoda pentru actualizarea jocului (nu implementata inca)
    public void updateGame(){

    }

    // suprascrierea metodei paintComponent pentru desenarea elementelor grafice in panou
    public void paintComponent(Graphics g){
        super.paintComponent(g); // apelul metodei paintComponent din clasa de baza
        game.render(g); // renderea jocului in panou

    }

    // metoda pentru obtinerea referintei catre obiectul Game asociat panoului
    public Game getGame(){
        return game; // returnarea referintei catre obiectul Game
    }

}
