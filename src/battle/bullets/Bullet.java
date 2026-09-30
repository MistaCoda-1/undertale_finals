package battle.bullets;

import java.awt.*;

/**
 * One bullet. Deliberately dumb — it only knows how to move itself in a
 * straight line and report its hitbox. Anything pattern-specific (when it
 * spawns, what direction, how many) lives in BulletPattern subclasses, not
 * here, so this class never needs to change as new patterns get added.
 */
public class Bullet {

    public double x;
    public double y;
    public final double vx;
    public final double vy;
    public final int width;
    public final int height;
    public final int damage;
    public final Color color;

    public Bullet(double x, double y, double vx, double vy, int width, int height, int damage, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.color = color;
    }

    public void advance() {
        x += vx;
        y += vy;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /** True once the bullet has drifted past the arena bounds (plus a small buffer) — safe to discard. */
    public boolean isOffscreen(int arenaWidth, int arenaHeight) {
        int buffer = 20;
        return x < -buffer || y < -buffer || x > arenaWidth + buffer || y > arenaHeight + buffer;
    }
}