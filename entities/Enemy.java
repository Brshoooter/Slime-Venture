package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {

    // indici pentru animație și starea inamicului
    protected int aniIndex, enemyState, enemyType;

    //viteză pentru animație
    protected int aniTick, aniSpeed = 15;

    // flaguri pentru prima actualizare și dacă inamicul este în aer
    protected boolean firstUpdate = true;
    protected boolean inAir;

    // viteza de cădere și gravitația
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;

    // viteza de mers
    protected float walkSpeed = 0.5f * Game.SCALE;

    // direcția de mers și coordonata pe axa Y a tile-ului
    protected int walkDir = LEFT;
    protected int tileY;

    // distanța de atac
    protected float attackDistance = Game.TILES_SIZE;

    // sănătatea maximă și curentă a inamicului
    protected int maxHealth;
    protected int currentHealth;

    // flaguri pentru activitatea inamicului și dacă atacul a fost verificat
    protected boolean active = true;
    protected boolean attackChecked;

    // constructor care inițializează coordonatele, dimensiunile și tipul inamicului
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    // metodă pentru verificarea primei actualizări
    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    // metodă pentru actualizarea stării inamicului în aer
    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    // metodă pentru deplasarea inamicului
    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    // metodă pentru a întoarce inamicul spre jucător
    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // metodă pentru a verifica dacă inamicul poate vedea jucătorul
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        return false;
    }

    // metodă pentru a verifica dacă jucătorul este în raza de acțiune a inamicului
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    // metodă pentru a verifica dacă jucătorul este suficient de aproape pentru un atac
    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    // metodă pentru a schimba starea inamicului
    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    // metodă pentru a răni inamicul
    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    // metodă pentru a verifica dacă inamicul a lovit jucătorul
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDMG(enemyType));
        attackChecked = true;
    }

    // metodă pentru a actualiza tichetele de animație
    protected void uppdateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
                switch (enemyState) {
                    case ATTACK, HIT -> enemyState = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    // metodă pentru a schimba direcția de mers
    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    // metodă pentru a reseta inamicul
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
    }

    // metode pentru a obține indicele de animație, starea inamicului și activitatea acestuia
    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }

}