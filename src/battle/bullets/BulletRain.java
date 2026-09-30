package battle.bullets;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simplest possible pattern, and a template for writing more: bullets spawn
 * at a random x along the top edge and fall straight down. Copy this file,
 * rename the class, and change spawnTick()'s spawn condition / Bullet
 * construction to get a different pattern (e.g. spawn from the sides for a
 * sweep, or spawn in a row for a wall).
 */
public class BulletRain extends BulletPattern {

    private static final int SPAWN_EVERY_TICKS = 1; // roughly 5/sec at a 16ms tick
    private static final int BULLET_SIZE = 10;
    private static final double FALL_SPEED = 4.5;
    private static final int DAMAGE = 2;

    public BulletRain(int arenaWidth, int arenaHeight) {
        super(arenaWidth, arenaHeight, 300); // ~5 seconds of spawning at a 16ms tick
    }

    @Override
    protected void spawnTick(int elapsedTicks) {
        if (elapsedTicks % SPAWN_EVERY_TICKS != 0) {
            return;
        }
        double x = ThreadLocalRandom.current().nextDouble(0, Math.max(1, arenaWidth - BULLET_SIZE));
        bullets.add(new Bullet(x, -BULLET_SIZE, 0, FALL_SPEED, BULLET_SIZE, BULLET_SIZE, DAMAGE, Color.WHITE));
    }
}