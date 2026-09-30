package battle.bullets;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for one enemy attack phase. One instance = one turn.
 *
 * This class handles everything that's the same for every pattern:
 * advancing bullets, discarding ones that have left the arena, and timing
 * when the pattern is done. A subclass only has to implement spawnTick() —
 * decide what bullets (if any) appear on a given tick. That's the entire
 * recipe for adding a new pattern: extend this class, fill in spawnTick().
 */
public abstract class BulletPattern {

    protected final int arenaWidth;
    protected final int arenaHeight;
    protected final List<Bullet> bullets = new ArrayList<>();

    private final int durationTicks; // how long this pattern spawns bullets for
    private int elapsedTicks = 0;

    protected BulletPattern(int arenaWidth, int arenaHeight, int durationTicks) {
        this.arenaWidth = arenaWidth;
        this.arenaHeight = arenaHeight;
        this.durationTicks = durationTicks;
    }

    /** Called once per game tick by ArenaPanel — advances/culls bullets and asks the subclass whether to spawn more. */
    public final void update() {
        if (elapsedTicks < durationTicks) {
            spawnTick(elapsedTicks);
        }

        for (Bullet bullet : bullets) {
            bullet.advance();
        }
        bullets.removeIf(b -> b.isOffscreen(arenaWidth, arenaHeight));

        elapsedTicks++;
    }

    /**
     * Called every tick while the pattern is within its duration — spawn new
     * bullets here on whatever schedule fits (e.g. every N ticks). Add them
     * to the protected `bullets` list.
     */
    protected abstract void spawnTick(int elapsedTicks);

    /** Pattern is done once its spawn window has passed and every bullet it spawned has left the arena. */
    public boolean isOver() {
        return elapsedTicks >= durationTicks && bullets.isEmpty();
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}