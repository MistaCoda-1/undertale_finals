package battle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FightMinigamePanel extends JPanel {

    public interface DamageListener {
        void onDamageDealt(int damage);
    }

    private static final int BAR_WIDTH = 750;
    private static final int BAR_HEIGHT = 20;
    private static final int TARGET_WIDTH = 30;
    private static final int MAX_DAMAGE = 20;
    private static final int BAR_Y = 100;

    private final Timer tickTimer;
    private final DamageListener listener;

    private double linePos = 0;
    private double lineSpeed = 7;
    private boolean locked = false;
    private int lastDamage = 0;

    public FightMinigamePanel(DamageListener listener) {
        this.listener = listener;

        setPreferredSize(new Dimension(BAR_WIDTH + 40, 90));
        setOpaque(false);
        setFocusable(true);

        tickTimer = new Timer(15, e -> {
            linePos += lineSpeed;
            if (linePos >= BAR_WIDTH) {
                linePos = BAR_WIDTH; // reached the end without a press: miss
                lockIn();
                return;
            }
            repaint();
        });

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "lockIn");
        actionMap.put("lockIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lockIn();
            }
        });
    }

    public void start() {
        locked = false;
        lastDamage = 0;
        linePos = 0;
        tickTimer.start();
        requestFocusInWindow();
    }

    private void lockIn() {
        if (locked) {
            return;
        }
        locked = true;
        tickTimer.stop();

        double center = BAR_WIDTH / 2.0;
        double distanceFromCenter = Math.abs(linePos - center);
        double accuracy = Math.max(0, 1 - (distanceFromCenter / center));
        lastDamage = (int) Math.round(accuracy * MAX_DAMAGE);

        repaint();

        if (listener != null) {
            listener.onDamageDealt(lastDamage);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int barX = (getWidth() - BAR_WIDTH) / 2; // keep the bar centered in whatever space it's given

        // bar background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        // center target zone
        g2.setColor(Color.WHITE);
        g2.fillRect(barX + BAR_WIDTH / 2 - TARGET_WIDTH / 2, BAR_Y, TARGET_WIDTH, BAR_HEIGHT);

        // moving line
        g2.setColor(Color.RED);
        int lineX = barX + (int) linePos;
        g2.fillRect(lineX - 2, 0, 4, 250);

        // bar border
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, BAR_Y, BAR_WIDTH, BAR_HEIGHT);

        // hint / result text
        g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.WHITE);
        String message = "PRESS SPACE!";
        if (locked) {
            message = lastDamage > 0 ? ("DAMAGE: " + lastDamage) : "MISS!";
        }
        int textWidth = g2.getFontMetrics().stringWidth(message);
        g2.drawString(message, barX + BAR_WIDTH / 2 - textWidth / 2, BAR_Y + BAR_HEIGHT + 30);
    }
}