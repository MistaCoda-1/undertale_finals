package battle;

import javax.swing.*;
import java.awt.*;

public class EnemyBG extends JPanel {

    private final Color gridColor;
    private final int cellSize;

    public EnemyBG(Color gridColor, int cellSize) {
        this.gridColor = gridColor;
        this.cellSize = cellSize;
        setOpaque(true); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(gridColor);
        g2d.setStroke(new BasicStroke(2));

        for (int x = cellSize; x < getWidth(); x += cellSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        
        for (int y = cellSize; y < getHeight(); y += cellSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}