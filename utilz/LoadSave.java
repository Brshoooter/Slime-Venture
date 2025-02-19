package utilz;

import entities.Robbot;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.ROBOT;

public class LoadSave {
    // constante pentru numele fisierelor
    public static final String PLAYER_ATLAS = "sprites3.png";
    public static final String LEVEL_ATLAS = "outside_tiles.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String BACKGROUND_MENU_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_background.png";
    public static final String ROBOT_SPRITE = "enemy_sprite.png";
    public static final String STTATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";

    // metoda pentru incarcarea unui sprite atlas
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is); // citeste imaginea din fisier

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close(); // inchide fluxul de intrare
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    // metoda pentru obtinerea tuturor nivelurilor din directorul lvls
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI()); // obtine fisierul de la URL
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles(); // obtine lista fisierelor
        File[] filesSorted = new File[files.length];

        // sorteaza fisierele in functie de nume
        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        // citeste imaginile din fisiere
        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs; // returneaza imaginile
    }
}
