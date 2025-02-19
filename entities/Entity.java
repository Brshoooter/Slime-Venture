package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    // coordonatele pe axele x și y
    protected float x, y;

    // lățimea și înălțimea entității
    protected int width, height;

    // hitbox-ul entității
    protected Rectangle2D.Float hitbox;

    // constructor care inițializează coordonatele și dimensiunile entității
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // metodă pentru inițializarea hitbox-ului cu coordonate și dimensiuni
    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    // metodă pentru desenarea hitbox-ului, utilă pentru debugging
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        // pentru debugging-ul hitbox-ului
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    // metodă pentru obținerea hitbox-ului entității
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
