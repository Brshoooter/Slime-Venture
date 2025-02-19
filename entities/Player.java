package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {

    // Animațiile jucătorului
    private BufferedImage[][] animations;

    // Contor pentru animație și viteză de animație
    private int aniTick, aniIndex, aniSpeed = 15;

    // Acțiunea curentă a jucătorului
    private int playerAction = IDLE;

    // Indicator dacă jucătorul se mișcă sau atacă
    private boolean moving = false, attacking = false;

    // Indicatori pentru direcțiile de mișcare și săritură
    private boolean up, left, right, down, jump;

    // Viteza jucătorului
    private float playerSpeed = 2.0f * Game.SCALE;

    // Datele nivelului pentru verificarea coliziunilor
    private int[][] lvlData;

    // Offset-ul pentru desenarea jucătorului pe ecran
    private float xDrawOffset = 39 * Game.SCALE;
    private float yDrawOffset = 97 * Game.SCALE;

    // Variabile pentru săritură / gravitate
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -3.7f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // Imaginea barei de stare
    private BufferedImage statusBarImg;

    // Dimensiuni și poziție pentru bara de stare
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    // Dimensiuni și poziție pentru bara de sănătate
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    // Sănătatea maximă și curentă a jucătorului
    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // Zona de atac a jucătorului
    private Rectangle2D.Float attackBox;

    // Variabile pentru redimensionarea imaginii
    private int flipX = 0;
    private int flipW = 1;

    // Indicator pentru verificarea atacului
    private boolean attackChecked = false;

    // Referință către starea de joc
    private Playing playing;

    // Constructor pentru inițializarea jucătorului
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x, y, (int) (44 * Game.SCALE), (int) (30 * Game.SCALE));
        initAttackBox();
    }

    // Inițializarea zonei de atac
    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    // Actualizarea jucătorului
    public void update() {
        updateHealthBar();

        // Verificare dacă jucătorul a murit
        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        // Actualizare zona de atac
        updateAttackBox();

        // Actualizare poziție și animație
        uppdatePos();
        if (attacking)
            checkAttack();
        uppdateAnimationTick();
        setAnimation();
    }

    // Verificare dacă jucătorul a lovit un inamic
    private void checkAttack() {
        if (attackChecked || aniIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    // Actualizare coordonate și dimensiuni pentru zona de atac
    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (int) (Game.SCALE * 10);
    }

    // Actualizare dimensiuni pentru bara de sănătate
    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    // Desenare jucător și interfață grafică
    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
        drawUI(g);
    }

    // Desenare a zonei de atac
    private void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    // Desenare a interfeței grafice
    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    // Actualizare contor pentru animație
    private void uppdateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    // Setare animație corespunzătoare stării jucătorului
    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = WALK;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            playerAction = JUMP;
        }

        if (attacking) {
            playerAction = ATTACK1;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    // Resetare contor animație
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    // Actualizare poziție jucător și verificare coliziuni
    private void uppdatePos() {
        moving = false;
        if (jump)
            jump();
        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);

        moving = true;
    }

    // Săritură
    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    // Resetare status săritură
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    // Actualizare poziție pe axa X
    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    // Modificare sănătate jucător
    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    // Încărcare animații
    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[8][13];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 128, i * 128, 128, 128);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STTATUS_BAR);
    }

    // Încărcare date nivel
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    // Resetare direcții de mișcare
    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    // Setare starea de atac
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    // Getteri și setteri pentru direcții de mișcare
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    // Setter pentru săritură
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    // Resetare stare jucător
    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
}