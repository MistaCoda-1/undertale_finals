package battle;

import javax.swing.*;
import java.awt.*;

public class AnimateBox extends JPanel {

    private final int startWidth;
    private final int startHeight;
    private final int targetWidth;
    private final int targetHeight;
    private final Runnable onComplete;

    private int boxWidth;
    private int boxHeight;
    private Timer timer;

    public AnimateBox(int startWidth, int startHeight, int targetWidth, int targetHeight, Runnable onComplete) {
        this.startWidth = startWidth;
        this.startHeight = startHeight;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.onComplete = onComplete;

        this.boxWidth = startWidth;
        this.boxHeight = startHeight;

        setBackground(Color.BLACK);
    }

    public void startAnimation() {
        if (timer != null && timer.isRunning()) {
            return;
        }

        timer = new Timer(15, e -> {
            boolean widthDone = false;
            boolean heightDone = false;

            // Undertale snapping step speeds
            int stepX = 25;
            int stepY = 15;

            // Animate width
            if (boxWidth > targetWidth) {
                boxWidth = Math.max(targetWidth, boxWidth - stepX);
            } else {
                widthDone = true;
            }

            // Animate height
            if (boxHeight > targetHeight) {
                boxHeight = Math.max(targetHeight, boxHeight - stepY);
            } else if (boxHeight < targetHeight) {
                boxHeight = Math.min(targetHeight, boxHeight + stepY);
            } else {
                heightDone = true;
            }

            repaint();

            // When target dimensions are reached, halt and trigger the callback
            if (widthDone && heightDone) {
                timer.stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = (getWidth() - boxWidth) / 2;
        int centerY = (getHeight() - boxHeight) / 2;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(centerX, centerY, boxWidth, boxHeight);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(centerX, centerY, boxWidth - 1, boxHeight - 1);
    }
}