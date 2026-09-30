package battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
    
    private final Runnable onTurnEnd;

    public ArenaPanel(int width, int height, ImageIcon heartIcon, Runnable onTurnEnd) {
        // Extract the raw image and its dimensions cleanly
        this.heartImage = heartIcon.getImage();
        this.heartWidth = heartIcon.getIconWidth();
        this.heartHeight = heartIcon.getIconHeight();
        this.onTurnEnd = onTurnEnd;

        // No layout or child components needed anymore!
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        setPreferredSize(new Dimension(width, height));

        // Initial positions remain identical
        soulX = (width - heartWidth) / 2;
        soulY = (height - heartHeight) / 2;

        setupInputMappings();
    }

    // 2. Add the custom paint method to draw the soul manually
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the screen and draws the background/border
        
        // Draw the heart at its active pixel position
        g.drawImage(heartImage, soulX, soulY, heartWidth, heartHeight, this);
    }

    public void startTurn() {
        if (gameLoopTimer != null && gameLoopTimer.isRunning()) {
            return;
        }

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

            // Bounding collision checks updated with standard integers
            int maxW = getWidth() - heartWidth - 3;
            int maxH = getHeight() - heartHeight - 3;

            soulX = Math.clamp(soulX, 3, maxW);
            soulY = Math.clamp(soulY, 3, maxH);

            // 3. Swap .setBounds() for repaint() to request an optimized frame update
            repaint(); 
        });

        gameLoopTimer.start();
        requestFocusInWindow();
    }

    public void endTurn() {
        if (gameLoopTimer != null) {
            gameLoopTimer.stop();
        }
        moveUp = moveDown = moveLeft = moveRight = focusActive = false; 
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
