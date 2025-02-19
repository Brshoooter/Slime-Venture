package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;

public class Robbot extends Enemy {

    // cutia de atac
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    // constructorul robotului, inițializează poziția și dimensiunile
    public Robbot(float x, float y) {
        super(x, y, ROBOT_WIDTH, ROBOT_HEIGHT, ROBOT);
        initHitbox(x, y, (int)(21 * Game.SCALE), (int)(49 * Game.SCALE));
        initAttackBox();
    }

    // inițializare cutie de atac
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(40 * Game.SCALE), (int)(60 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 10);
    }

    // actualizare starea robotului
    public void update(int[][] lvlData, Player player) {
        uppdateBehaviour(lvlData, player);
        uppdateAnimationTick();
        uppdateAttackBox();
    }

    // actualizare poziția cutiei de atac
    private void uppdateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    // actualizare comportamentul robotului în funcție de starea sa și poziția jucătorului
    private void uppdateBehaviour(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 5 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    // desenare cutia de atac pentru debugging
    public void drawAttackbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    // întoarce poziția pe axa X pentru a desena robotul în funcție de direcția de mers
    public int flipX() {
        if (walkDir == LEFT)
            return width;
        else
            return 0;
    }

    // întoarce lățimea pentru a desena robotul în funcție de direcția de mers
    public int flipW() {
        if (walkDir == LEFT)
            return -1;
        else
            return 1;
    }
}