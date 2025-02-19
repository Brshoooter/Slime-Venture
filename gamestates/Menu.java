package gamestates;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3]; // butoanele din meniu
    private BufferedImage backgroundImage, backgroundBigImg; // imaginile de fundal
    private int menuX, menuY, menuWidth, menuHeight; // poziția și dimensiunea meniului

    public Menu(Game game) {
        super(game);
        loadButtons(); // încarcă butoanele
        loadBackground(); // încarcă imaginea de fundal
        backgroundBigImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU_IMG); // încarcă imaginea mare de fundal
    }

    // încarcă imaginea de fundal și setează dimensiunile meniului
    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int)(backgroundImage.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundImage.getHeight() * Game.SCALE);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    // încarcă butoanele și setează pozițiile acestora
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);
    }

    // actualizează starea butoanelor
    @Override
    public void uppdate() {
        for (MenuButton mb : buttons) {
            mb.uppdate();
        }
    }

    // desenează meniul și butoanele
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundBigImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    // eveniment de click pe mouse
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    // eveniment de apăsare pe mouse
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    // eveniment de eliberare a mouse-ului
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGamestate(); // aplică starea jocului
                break;
            }
        }
        resetButtons(); // resetează starea butoanelor
    }

    // resetează starea butoanelor
    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    // eveniment de mișcare a mouse-ului
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    // eveniment de apăsare a unei taste
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING; // schimbă starea jocului în PLAYING când este apăsat Enter
    }

    // eveniment de eliberare a unei taste
    @Override
    public void keyReleased(KeyEvent e) {

    }
}