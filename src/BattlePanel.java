package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class BattlePanel extends JPanel implements KeyListener {

    // Player position
    int x = 100;
    int y = 100;

    // How many pixels the player moves
    int speed = 5;

    // Which keys are currently being held
    boolean up;
    boolean down;
    boolean left;
    boolean right;

    BufferedImage sprite;

    public BattlePanel() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/player_soul.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // Allow this panel to receive keyboard input
        setFocusable(true);
        addKeyListener(this);
    }

    // This is responsible for DRAWING the game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Battle box
        g.setColor(Color.BLACK);
        g.fillRect(100, 100, 250, 250);

        g.setColor(Color.WHITE);
        g.drawRect(100, 100, 250, 250);

        // Player
        g.drawImage(sprite, x, y, null);
    }
    
    // This is responsible for UPDATING the player
    public void update() {

        if (up) {
            y -= speed;
        }

        if (down) {
            y += speed;
        }

        if (left) {
            x -= speed;
        }

        if (right) {
            x += speed;
        }

        // Tell Swing to draw the screen again
        repaint();
    }

    // Key pressed
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    // Key released
    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // We don't need this
    }

    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Game");
        BattlePanel game = new BattlePanel();

        window.add(game);

        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Simple game loop
        while (true) {

            game.update();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}