package ui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class StatusBar extends JPanel {
    private int currentHp;
    private int maxHp;

    public StatusBar(int currentHp, int maxHp) {
        this.currentHp = currentHp;
        this.maxHp = maxHp;

        // Keep the styling inside the component itself
        Dimension size = new Dimension(110, 20);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        
        this.setBackground(Color.BLACK);
    }

    // Method to easily update health values from your main game loop/class
    public void updateHp(int currentHp, int maxHp) {
        this.currentHp = currentHp;
        this.maxHp = maxHp;
        this.repaint(); // Triggers paintComponent to redraw the bar
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Prevent division by zero if maxHp isn't set properly
        if (maxHp <= 0)
            return;

        double hpPercentage = (double) currentHp / maxHp;
        int coloredWidth = (int) (getWidth() * hpPercentage);

        // Draw background (Red / Missing health)
        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw foreground (Yellow / Current health)
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, coloredWidth, getHeight());
    }
}