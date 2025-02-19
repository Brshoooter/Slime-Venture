package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private BufferedImage[][] robotArr;
    private Playing playing;
    private ArrayList<Robbot> robots = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        robots = level.getRobots();
    }

    public void update(int[][] lvlData, Player player){
        boolean isAnyActive = false;
        for(Robbot c : robots)
            if(c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset){
        drawRobots(g, xLvlOffset);
    }

    private void drawRobots(Graphics g, int xLvlOffset) {
        for(Robbot c : robots)
            if(c.isActive()){
            g.drawImage(robotArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - ROBOT_DEAWOFFSET_X + c.flipX(), (int) c.getHitbox().y - ROBOT_DEAWOFFSET_Y, ROBOT_WIDTH * c.flipW(), ROBOT_HEIGHT, null);

        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Robbot c : robots)
            if(c.isActive())
                if(attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }
    }

    private void loadEnemyImgs() {
        robotArr = new BufferedImage[5][6];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ROBOT_SPRITE);
        for(int j = 0; j < robotArr.length; j++)
            for(int i = 0; i< robotArr[j].length; i++){
                robotArr[j][i] = temp.getSubimage(i * ROBOT_WIDTH_DEFAULT, j * ROBOT_HEIGHT_DEFAULT, ROBOT_WIDTH_DEFAULT, ROBOT_HEIGHT_DEFAULT);
            }
    }

    public void resetAllEnemies(){
        for(Robbot c : robots)
            c.resetEnemy();
    }
}
