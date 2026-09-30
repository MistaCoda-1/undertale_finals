package battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.IntConsumer;

import battle.bullets.*;

public class ArenaPanel extends JPanel {
    private final Image heartImage; 
    private final int heartWidth;
    private final int heartHeight;
    
    private int soulX;
    private int soulY;
    private final int normalSpeed = 6;
    private final int focusSpeed = 3;
    private Timer gameLoopTimer;
    
    private boolean moveUp, moveDown, moveLeft, moveRight;
    private boolean focusActive;
    
    private final IntConsumer onPlayerHit;
    private final Runnable onTurnEnd;

    private BulletPattern pattern;
    private static final int INVINCIBILITY_TICKS = 30; // ~0.7s at a 16ms tick
    private int invincibleTicks = 0;

    public ArenaPanel(int width, int height, ImageIcon heartIcon, IntConsumer onPlayerHit, Runnable onTurnEnd) {
        this.heartImage = heartIcon.getImage();
        this.heartWidth = heartIcon.getIconWidth();
        this.heartHeight = heartIcon.getIconHeight();
        this.onPlayerHit = onPlayerHit;
        this.onTurnEnd = onTurnEnd;

        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        setPreferredSize(new Dimension(width, height));

        // Center player
        soulX = (width - heartWidth) / 2;
        soulY = (height - heartHeight) / 2;

        setupInputMappings();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear screen and draws the background/border

        if (pattern != null) {
            for (Bullet bullet : pattern.getBullets()) {
                g.setColor(bullet.color);
                g.fillRect((int) bullet.x, (int) bullet.y, bullet.width, bullet.height);
            }
        }

        // simple invincibility flicker: skip drawing the heart every few ticks while it's active
        boolean drawHeart = invincibleTicks <= 0 || (invincibleTicks / 4) % 2 == 0;
        if (drawHeart) {
            g.drawImage(heartImage, soulX, soulY, heartWidth, heartHeight, this);
        }
    }

    /** Starts the dodge phase, running the given bullet pattern until it's over. */
    public void startTurn(BulletPattern pattern) {
        if (gameLoopTimer != null && gameLoopTimer.isRunning()) {
            return;
        }

        this.pattern = pattern;
        invincibleTicks = 0;

        gameLoopTimer = new Timer(16, e -> {
            double dx = 0;
            double dy = 0;

            if (moveLeft)  dx -= 1;
            if (moveRight) dx += 1;
            if (moveUp)    dy -= 1;
            if (moveDown)  dy += 1;

            if (dx != 0 && dy != 0) {
                dx *= 0.7071; 
                dy *= 0.7071;
            }

            int activeSpeed = focusActive ? focusSpeed : normalSpeed;

            soulX += (int) Math.round(dx * activeSpeed);
            soulY += (int) Math.round(dy * activeSpeed);

            int maxW = getWidth() - heartWidth - 3;
            int maxH = getHeight() - heartHeight - 3;

            soulX = Math.clamp(soulX, 3, maxW);
            soulY = Math.clamp(soulY, 3, maxH);

            this.pattern.update();
            checkCollisions();

            if (this.pattern.isOver()) {
                endTurn();
                if (onTurnEnd != null) {
                    onTurnEnd.run();
                }
                return;
            }

            repaint();
        });

        gameLoopTimer.start();
        requestFocusInWindow();
    }

    /** Heart-vs-bullet collision, with a short invincibility window after each hit. */
    private void checkCollisions() {
        if (invincibleTicks > 0) {
            invincibleTicks--;
            return;
        }

        // hitbox is a little smaller than the sprite, same idea as Undertale's forgiving hit detection
        Rectangle heartBounds = new Rectangle(soulX + 4, soulY + 4, heartWidth - 8, heartHeight - 8);

        for (Bullet bullet : pattern.getBullets()) {
            if (heartBounds.intersects(bullet.getBounds())) {
                if (onPlayerHit != null) {
                    onPlayerHit.accept(bullet.damage);
                }
                invincibleTicks = INVINCIBILITY_TICKS;
                break; // only one hit per tick, even if multiple bullets overlap
            }
        }
    }

    public void endTurn() {
        if (gameLoopTimer != null) {
            gameLoopTimer.stop();
        }
        moveUp = moveDown = moveLeft = moveRight = focusActive = false;
        pattern = null;
    }

    private void setupInputMappings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Directional Pressed Mappings
        im.put(KeyStroke.getKeyStroke("UP"), "pressUp");
        am.put("pressUp", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveUp = true; } });
        im.put(KeyStroke.getKeyStroke("DOWN"), "pressDown");
        am.put("pressDown", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveDown = true; } });
        im.put(KeyStroke.getKeyStroke("LEFT"), "pressLeft");
        am.put("pressLeft", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveLeft = true; } });
        im.put(KeyStroke.getKeyStroke("RIGHT"), "pressRight");
        am.put("pressRight", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveRight = true; } });
        im.put(KeyStroke.getKeyStroke("X"), "pressX");
        am.put("pressX", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { focusActive = true; } });

        // Directional Released Mappings
        im.put(KeyStroke.getKeyStroke("released UP"), "releaseUp");
        am.put("releaseUp", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveUp = false; } });
        im.put(KeyStroke.getKeyStroke("released DOWN"), "releaseDown");
        am.put("releaseDown", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveDown = false; } });
        im.put(KeyStroke.getKeyStroke("released LEFT"), "releaseLeft");
        am.put("releaseLeft", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveLeft = false; } });
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "releaseRight");
        am.put("releaseRight", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { moveRight = false; } });
        im.put(KeyStroke.getKeyStroke("released X"), "releaseX");
        am.put("releaseX", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { focusActive = false; } });
    }
}