package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    // referinta la starea de joc Playing
    private Playing playing;
    // imaginea de fundal
    private BufferedImage backgroundImg;
    // coordonatele si dimensiunile imaginii de fundal
    private int bgX, bgY, bgWidth, bgHeight; // bg vine de la background
    // butoane de sunet
    private SoundButton musicButton, sfxButton;
    // butoane URM (Urmatorul buton, Reincepe butonul, Dezactiveaza pauza)
    private UrmButton menuB, replayB, unpauseB;
    // butonul de volum
    private VolumeButton volumeButton;

    // constructor pentru a initializa overlay-ul de pauza
    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground(); // incarca imaginea de fundal
        createSoundButtons(); // creeaza butoanele de sunet
        createUrmButtons(); // creeaza butoanele URM
        createVolumeButton(); // creeaza butonul de volum
    }

    // metoda pentru crearea butonului de volum
    private void createVolumeButton() {
        int vX = (int)((bgX + 23) * Game.SCALE);
        int vY = (int)((bgY + 250) * Game.SCALE);
        volumeButton = new VolumeButton(vX,vY,SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    // metoda pentru crearea butoanelor URM
    private void createUrmButtons() {
        int menuX = (int)((bgX + 30)*Game.SCALE);
        int replayX = (int)((bgX + 101)*Game.SCALE);
        int unpauseX = (int)((bgX + 170)*Game.SCALE);
        int bY = (int)((bgY + 295) * Game.SCALE);

        menuB = new UrmButton(menuX , bY , URM_SIZE, URM_SIZE,2);
        replayB = new UrmButton(replayX , bY , URM_SIZE, URM_SIZE,1);
        unpauseB = new UrmButton(unpauseX, bY , URM_SIZE, URM_SIZE,0);
    }

    // metoda pentru crearea butoanelor de sunet
    private void createSoundButtons() {
        int soundX = (int)((bgX + 150) * Game.SCALE);
        int musicY = (int)((bgY + 120) *Game.SCALE);
        int sfxY = (int)((bgY + 160) * Game.SCALE);
        musicButton = new SoundButton(soundX,musicY,SOUND_SIZE,SOUND_SIZE);
        sfxButton = new SoundButton(soundX,sfxY,SOUND_SIZE,SOUND_SIZE);
    }

    // metoda pentru incarcarea imaginii de fundal
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int)(100 * Game.SCALE);
    }

    // metoda pentru actualizarea butoanelor
    public void update(){
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    // metoda pentru desenarea elementelor UI
    public void draw(Graphics g){
        // desenarea fundalului
        g.drawImage(backgroundImg,bgX,bgY,bgWidth,bgHeight,null);
        // desenarea butoanelor de sunet
        musicButton.draw(g);
        sfxButton.draw(g);
        // desenarea butoanelor URM
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        // desenarea butonului de volum
        volumeButton.draw(g);
    }

    // metoda pentru gestionarea tragerii mouse-ului
    public void mouseDragged(MouseEvent e){
        if(volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }
    }

    // metoda pentru gestionarea apasarii mouse-ului
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
    }

    // metoda pentru gestionarea eliberarii mouse-ului
    public void mouseReleased(MouseEvent e) {
        if(isIn(e,musicButton)){
            if(musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        } else if(isIn(e,sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        } else if(isIn(e,menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        } else if(isIn(e,replayB)) {
            if (replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if(isIn(e,unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeButton.resetBools();
    }

    // metoda pentru gestionarea miscarii mouse-ului
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isIn(e,musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e,menuB))
            menuB.setMouseOver(true);
        else if(isIn(e,replayB))
            replayB.setMouseOver(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMouseOver(true);
        else if(isIn(e,volumeButton))
            volumeButton.setMouseOver(true);
    }

    // metoda pentru verificarea daca mouse-ul este deasupra unui buton
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(),e.getY());
    }
}
