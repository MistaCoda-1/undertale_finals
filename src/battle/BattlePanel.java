package battle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import ui.Main;
import ui.UIUtils; // Import your utility package

public class BattlePanel extends JPanel {
    enum BattleState {
        PLAYER_TURN,
        FIGHT_MINIGAME,
        ENEMY_TURN
    }

    private BattleState state = BattleState.PLAYER_TURN;

    private int selectedBtnIndex = 0;
    private CommandButton[] buttons;
    private Runnable[] commandActions;

    JPanel btnPanel = new JPanel(new GridLayout(1, 4, 20, 0));
    EnemyBG enemyPanel = new EnemyBG(new Color(0, 255, 60), 70);
    JPanel dialogueContainer = new JPanel(new BorderLayout());
    JPanel dialoguePanel = new JPanel(new BorderLayout());
    private FightMinigamePanel fightMinigame;

    private final ImageIcon heartIcon = new ImageIcon(getClass().getResource("/resources/player_soul.png"));

    // == Roguelike Floor & Player Stats ==
    private String playerName = "DOM";
    private int currentLvl = 1;
    private int currentHp = 20;
    private int maxHp = 20;
    private final int totalGameLvl = 15;

    // UI Status Components ==
    private JLabel statsTextLabel;
    private JPanel hpBarGraphic;
    private JLabel hpNumericLabel;

    private final Main main;

    public BattlePanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/enemy.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                250,
                250,
                Image.SCALE_SMOOTH);
        JLabel enemySprite = new JLabel(new ImageIcon(scaledImage));

        enemyPanel.setPreferredSize(new Dimension(420, 280));
        enemyPanel.setLayout(new GridBagLayout());
        enemyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel enemyCenteringWrapper = new JPanel(new GridBagLayout());
        enemyCenteringWrapper.setBackground(Color.BLACK);
        enemyCenteringWrapper.setBorder(new EmptyBorder(25, 0, 10, 0));
        enemyCenteringWrapper.add(enemyPanel);

        enemyPanel.add(enemySprite);

        dialogueContainer.setBorder(new EmptyBorder(10, 25, 10, 25));

        dialoguePanel.setBackground(Color.BLACK);
        dialoguePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                new EmptyBorder(15, 20, 15, 20)));

        dialogueContainer.add(dialoguePanel, BorderLayout.CENTER);

        // FIX: Styled dialogue label text using your helper font setup
        JLabel dialogueLabel = new JLabel("...");
        dialogueLabel.setForeground(Color.WHITE);
        dialogueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Dynamically loads the loaded ttf file configuration directly into the text
        // element
        Font gameFont = UIUtils.loadFont("/resources/8bitoperator-jve/8bitoperator_jve.ttf", 24f);
        dialogueLabel.setFont(gameFont);
        dialoguePanel.add(dialogueLabel, BorderLayout.CENTER);

        btnPanel.setPreferredSize(new Dimension(300, 75));

        enemyPanel.setBackground(Color.black);
        dialogueContainer.setBackground(Color.black);
        btnPanel.setBackground(Color.black);

        btnPanel.setBorder(new EmptyBorder(5, 25, 20, 25));

        CommandButton fightBtn = new CommandButton("FIGHT", heartIcon);
        CommandButton actBtn = new CommandButton("ACT", heartIcon);
        CommandButton itemBtn = new CommandButton("ITEM", heartIcon);
        CommandButton mercyBtn = new CommandButton("MERCY", heartIcon);

        buttons = new CommandButton[] {
                fightBtn,
                actBtn,
                itemBtn,
                mercyBtn
        };

        commandActions = new Runnable[] {
                this::startFightSequence,
                this::actPlaceholder,
                this::itemPlaceholder,
                this::mercyPlaceholder
        };

        btnPanel.add(fightBtn);
        btnPanel.add(actBtn);
        btnPanel.add(itemBtn);
        btnPanel.add(mercyBtn);

        JPanel lowerWrapper = new JPanel(new BorderLayout());
        lowerWrapper.setBackground(Color.BLACK);

        lowerWrapper.add(createStatusBar(), BorderLayout.NORTH);
        lowerWrapper.add(btnPanel, BorderLayout.SOUTH);

        add(enemyCenteringWrapper, BorderLayout.NORTH);
        add(dialogueContainer, BorderLayout.CENTER);
        add(lowerWrapper, BorderLayout.SOUTH);

        updateSelection();

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
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

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "select");
        actionMap.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (state != BattleState.PLAYER_TURN) {
                    return;
                }
                commandActions[selectedBtnIndex].run();
            }
        });

        setVisible(true);
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusBar.setBackground(Color.BLACK);
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 25, 5, 25));

        // Load baseline custom font engine rule
        Font statusFont = UIUtils.loadFont("/resources/8bitoperator-jve/8bitoperator_jve.ttf", 24f);

        // 1. Setup Main Stats Text
        statsTextLabel = new JLabel(playerName + "   LEVEL " + currentLvl + "/" + totalGameLvl + "    ");
        statsTextLabel.setFont(statusFont);
        statsTextLabel.setForeground(Color.WHITE);

        // 2. Setup "HP" marker text (Slightly smaller size scale for aesthetic
        // accuracy)
        JLabel hpMarker = new JLabel("HP  ");
        hpMarker.setFont(statusFont.deriveFont(Font.BOLD, 14f));
        hpMarker.setForeground(Color.WHITE);

        // 3. Render Status Canvas Blocks
        hpBarGraphic = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                double hpPercentage = (double) currentHp / maxHp;
                int coloredWidth = (int) (getWidth() * hpPercentage);

                g.setColor(Color.RED);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.YELLOW);
                g.fillRect(0, 0, coloredWidth, getHeight());
            }
        };
        hpBarGraphic.setPreferredSize(new Dimension(110, 20));
        hpBarGraphic.setBackground(Color.BLACK);

        // 4. Setup Fraction Label Text
        hpNumericLabel = new JLabel("   " + currentHp + " / " + maxHp);
        hpNumericLabel.setFont(statusFont);
        hpNumericLabel.setForeground(Color.WHITE);

        statusBar.add(statsTextLabel);
        statusBar.add(hpMarker);
        statusBar.add(hpBarGraphic);
        statusBar.add(hpNumericLabel);

        return statusBar;
    }

    public void updatePlayerStats(int newHp, int level) {
        this.currentHp = Math.clamp(newHp, 0, maxHp);
        this.currentLvl = Math.clamp(level, 1, totalGameLvl);

        if (statsTextLabel != null && hpNumericLabel != null && hpBarGraphic != null) {
            Font currentFont = statsTextLabel.getFont();
            statsTextLabel.setText(playerName + "   LEVEL " + currentLvl + "/" + totalGameLvl + "    ");
            hpNumericLabel.setText("   " + currentHp + " / " + maxHp);
            hpBarGraphic.repaint();
        }
    }

    private void startFightSequence() {
        if (state != BattleState.PLAYER_TURN) {
            return;
        }
        state = BattleState.FIGHT_MINIGAME;
        clearSelectionIcons();

        dialoguePanel.removeAll();
        fightMinigame = new FightMinigamePanel(this::onFightDamageDealt);
        dialoguePanel.add(fightMinigame, BorderLayout.CENTER);

        dialoguePanel.revalidate();
        dialoguePanel.repaint();

        fightMinigame.start();
    }

    private void actPlaceholder() {
    }

    private void itemPlaceholder() {
    }

    private void mercyPlaceholder() {
    }

    private void onFightDamageDealt(int damage) {
        Timer resultPause = new Timer(1200, e -> showBattleBox());
        resultPause.setRepeats(false);
        resultPause.start();
    }

    private void showBattleBox() {
        int currentWidth = dialogueContainer.getWidth();
        int currentHeight = dialogueContainer.getHeight();

        dialogueContainer.removeAll();

        AnimateBox animator = new AnimateBox(currentWidth, currentHeight, 300, 250, () -> {
            finalizeBattleBox(300, 250);
        });

        dialogueContainer.add(animator, BorderLayout.CENTER);
        dialogueContainer.revalidate();
        dialogueContainer.repaint();
        animator.startAnimation();
    }

    private void finalizeBattleBox(int width, int height) {
        dialogueContainer.removeAll();

        JPanel battleBox = new JPanel(null);
        battleBox.setBackground(Color.BLACK);
        battleBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        battleBox.setPreferredSize(new Dimension(width, height));

        JLabel heartLabel = new JLabel(heartIcon);
        int heartX = (width - heartIcon.getIconWidth()) / 2;
        int heartY = (height - heartIcon.getIconHeight()) / 2;
        heartLabel.setBounds(heartX, heartY, heartIcon.getIconWidth(), heartIcon.getIconHeight());
        battleBox.add(heartLabel);

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setOpaque(false);
        centeringWrapper.add(battleBox);

        dialogueContainer.add(centeringWrapper, BorderLayout.CENTER);
        dialogueContainer.revalidate();
        dialogueContainer.repaint();

        requestFocusInWindow();

        state = BattleState.ENEMY_TURN;
    }

    private void updateSelection() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(i == selectedBtnIndex);
        }
    }

    private void clearSelectionIcons() {
        for (CommandButton button : buttons) {
            button.setSelected(false);
        }
    }
}