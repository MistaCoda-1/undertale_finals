import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BattlePanel extends JPanel {

    enum BattleState {
        PLAYER_TURN,
        FIGHT_TIMING,
        ENEMY_TURN
    }

    private BattleState state = BattleState.PLAYER_TURN;

    private int selectedBtnIndex = 0;
    JPanel btnPanel = new JPanel(new GridLayout(1, 4, 20, 0));
    private JButton[] buttons;

    JPanel enemyPanel = new JPanel();
    JPanel dialogueContainer = new JPanel(new BorderLayout());
    JPanel dialoguePanel = new JPanel();

    private final ImageIcon heartIcon =
            new ImageIcon(getClass().getResource("resources/player_soul.png"));

    private final ImageIcon emptyIcon = new ImageIcon(new BufferedImage(
            heartIcon.getIconWidth(),
            heartIcon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB));

    private final Main main;

    // ===== FIGHT TIMING =====
    private JPanel timingPanel;
    private Timer timingTimer;

    private int timingPosition = 0;
    private int timingDirection = 1;

    // ===== BATTLE ARENA =====
    private JPanel battleArena;

    public BattlePanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // =========================
        // ENEMY
        // =========================

        enemyPanel.setBackground(Color.BLACK);

        ImageIcon originalIcon =
                new ImageIcon(getClass().getResource("resources/enemy.png"));

        Image originalImage = originalIcon.getImage();

        Image scaledImage = originalImage.getScaledInstance(
                250,
                250,
                Image.SCALE_SMOOTH);

        JLabel enemySprite = new JLabel(new ImageIcon(scaledImage));

        enemyPanel.add(enemySprite);

        // =========================
        // DIALOGUE
        // =========================

        dialogueContainer.setBackground(Color.BLACK);

        dialogueContainer.setBorder(
                new EmptyBorder(100, 50, 10, 50)
        );

        dialoguePanel.setBackground(Color.BLACK);

        dialoguePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE),
                        new EmptyBorder(15, 20, 15, 20)
                )
        );

        dialogueContainer.add(dialoguePanel, BorderLayout.CENTER);

        JLabel dialogueLabel = new JLabel("...");
        dialogueLabel.setForeground(Color.WHITE);

        dialoguePanel.add(dialogueLabel);

        // =========================
        // BUTTONS
        // =========================

        btnPanel.setPreferredSize(new Dimension(300, 80));

        // == Uncomment/Comment to show bounds/panel borders == //
        btnPanel.setOpaque(false);
        btnPanel.setBackground(Color.RED);

        btnPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JButton fightBtn = new JButton("FIGHT");
        JButton actBtn = new JButton("ACT");
        JButton itemBtn = new JButton("ITEM");
        JButton mercyBtn = new JButton("MERCY");

        buttons = new JButton[] {
                fightBtn,
                actBtn,
                itemBtn,
                mercyBtn
        };

        btnPanel.add(fightBtn);
        btnPanel.add(actBtn);
        btnPanel.add(itemBtn);
        btnPanel.add(mercyBtn);

        UIUtils.styleButton(fightBtn);
        UIUtils.styleButton(actBtn);
        UIUtils.styleButton(itemBtn);
        UIUtils.styleButton(mercyBtn);

        fightBtn.setFocusable(false);
        actBtn.setFocusable(false);
        itemBtn.setFocusable(false);
        mercyBtn.setFocusable(false);

        // =========================
        // FIGHT BUTTON
        // =========================

        fightBtn.addActionListener(e -> startFightTiming());

        // =========================
        // ADD EVERYTHING
        // =========================

        add(enemyPanel, BorderLayout.NORTH);
        add(dialogueContainer, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // =========================
        // ARROW KEY NAVIGATION
        // =========================

        updateSelection();

        InputMap inputMap =
                getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        ActionMap actionMap = getActionMap();

        inputMap.put(
                KeyStroke.getKeyStroke("LEFT"),
                "moveLeft"
        );

        inputMap.put(
                KeyStroke.getKeyStroke("RIGHT"),
                "moveRight"
        );

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                // Don't navigate buttons during the timing minigame
                if (state != BattleState.PLAYER_TURN) {
                    return;
                }

                selectedBtnIndex--;

                if (selectedBtnIndex < 0) {
                    selectedBtnIndex = 3;
                }

                updateSelection();
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                // Don't navigate buttons during the timing minigame
                if (state != BattleState.PLAYER_TURN) {
                    return;
                }

                selectedBtnIndex++;

                if (selectedBtnIndex > 3) {
                    selectedBtnIndex = 0;
                }

                updateSelection();
            }
        });

        setVisible(true);
    }

    // =========================================================
    // BUTTON SELECTION
    // =========================================================

    private void updateSelection() {

        for (int i = 0; i < buttons.length; i++) {

            if (i == selectedBtnIndex) {
                buttons[i].setIcon(heartIcon);
            } else {
                buttons[i].setIcon(emptyIcon);
            }
        }
    }

    // =========================================================
    // FIGHT TIMING MINIGAME
    // =========================================================

    private void startFightTiming() {

        state = BattleState.FIGHT_TIMING;

        // Remove the dialogue area
        remove(dialogueContainer);

        // Create timing panel
        timingPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Timing bar
                int barX = 50;
                int barWidth = getWidth() - 100;
                int barY = getHeight() / 2 - 10;
                int barHeight = 20;

                g2.setColor(Color.WHITE);
                g2.drawRect(
                        barX,
                        barY,
                        barWidth,
                        barHeight
                );

                // Center indicator
                int centerX = barX + barWidth / 2;

                g2.setColor(Color.RED);

                g2.fillRect(
                        centerX - 3,
                        barY - 15,
                        6,
                        barHeight + 30
                );

                // Moving line
                int lineX = barX + timingPosition;

                g2.setColor(Color.WHITE);

                g2.fillRect(
                        lineX - 2,
                        barY - 20,
                        4,
                        barHeight + 40
                );
            }
        };

        timingPanel.setBackground(Color.BLACK);

        add(timingPanel, BorderLayout.CENTER);

        revalidate();
        repaint();

        // Make sure the timing panel can receive the SPACE key
        InputMap inputMap =
                timingPanel.getInputMap(
                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );

        ActionMap actionMap =
                timingPanel.getActionMap();

        inputMap.put(
                KeyStroke.getKeyStroke("SPACE"),
                "finishTiming"
        );

        actionMap.put(
                "finishTiming",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e) {

                        finishFightTiming();
                    }
                }
        );

        // Start from the left
        timingPosition = 0;
        timingDirection = 1;

        // Move the line
        timingTimer = new Timer(10, e -> {

            int barWidth = timingPanel.getWidth() - 100;

            timingPosition += timingDirection * 2;

            if (timingPosition >= barWidth) {
                timingPosition = barWidth;
                timingDirection = -1;
            }

            if (timingPosition <= 0) {
                timingPosition = 0;
                timingDirection = 1;
            }

            timingPanel.repaint();
        });

        timingTimer.start();
    }

    // =========================================================
    // FINISH TIMING
    // =========================================================

    private void finishFightTiming() {

        if (state != BattleState.FIGHT_TIMING) {
            return;
        }

        state = BattleState.ENEMY_TURN;

        if (timingTimer != null) {
            timingTimer.stop();
        }

        int barX = 50;
        int barWidth = timingPanel.getWidth() - 100;

        int centerX = barX + barWidth / 2;

        int lineX = barX + timingPosition;

        int distance = Math.abs(lineX - centerX);

        // Maximum possible damage
        int maxDamage = 100;

        // Distance from center to edge
        int maxDistance = barWidth / 2;

        // 1.0 = perfect
        // 0.0 = edge
        double accuracy =
                1.0 - ((double) distance / maxDistance);

        if (accuracy < 0) {
            accuracy = 0;
        }

        int damage =
                (int) (maxDamage * accuracy);

        if (damage < 1) {
            damage = 1;
        }

        System.out.println("Distance: " + distance);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Damage dealt: " + damage);

        showBattleArena();
    }

    // =========================================================
    // BATTLE ARENA
    // =========================================================

    private void showBattleArena() {

        remove(timingPanel);

        battleArena = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                // Battle box size
                int boxWidth = 500;
                int boxHeight = 300;

                // Center the box
                int boxX =
                        (getWidth() - boxWidth) / 2;

                int boxY =
                        (getHeight() - boxHeight) / 2;

                // Draw battle box
                g2.setColor(Color.WHITE);

                g2.drawRect(
                        boxX,
                        boxY,
                        boxWidth,
                        boxHeight
                );

                // Draw player soul in center
                Image soulImage =
                        heartIcon.getImage();

                int soulSize = 20;

                int soulX =
                        boxX +
                        boxWidth / 2 -
                        soulSize / 2;

                int soulY =
                        boxY +
                        boxHeight / 2 -
                        soulSize / 2;

                g2.drawImage(
                        soulImage,
                        soulX,
                        soulY,
                        soulSize,
                        soulSize,
                        this
                );
            }
        };

        battleArena.setBackground(Color.BLACK);

        add(battleArena, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}