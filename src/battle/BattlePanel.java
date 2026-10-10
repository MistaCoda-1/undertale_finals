package battle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import ui.*;

import battle.bullets.*;

public class BattlePanel extends JPanel {
    enum BattleState {
        PLAYER_TURN,
        FIGHT_MINIGAME,
        ENEMY_TURN
    }

    // ===== Stage: fixed design size, centered in whatever space the window gives
    private static final int STAGE_W = 960;
    private static final int STAGE_H = 700;

    // ===== Enemy =====
    private static final int ENEMY_W = 700;
    private static final int ENEMY_H = 280;
    private static final int ENEMY_X = (STAGE_W - ENEMY_W) / 2;
    private static final int ENEMY_Y = 8;
    private static final int ENEMY_SPRITE = 250;

    // ===== Text / HP-bar rows (enemy HP, player status) =====
    private static final int ROW_H = 30;
    private static final int BAR_W = 110;
    private static final int BAR_H = 20;
    private static final int ENEMY_HP_Y = ENEMY_Y + ENEMY_H + 4;

    // ===== Dialogue box / battle arena area =====
    private static final int BOX_X = 25;
    private static final int BOX_Y = ENEMY_HP_Y + ROW_H + 6;
    private static final int BOX_W = STAGE_W - 2 * BOX_X;
    private static final int BOX_H = 265;
    private static final int BOX_PAD = 4; // slack around the animating box so its stroke never gets clipped
    private static final int ARENA_W = 300;
    private static final int ARENA_H = 250;

    // ===== Player status row + command buttons =====
    private static final int STATUS_Y = BOX_Y + BOX_H + 6;
    private static final int BTN_W = 212;
    private static final int BTN_H = 50;
    private static final int BTN_GAP = 20;
    private static final int BTN_Y = STATUS_Y + ROW_H + 6;

    private BattleState state = BattleState.PLAYER_TURN;

    private final JPanel stage = new JPanel(null);

    private int selectedBtnIndex = 0;
    private CommandButton[] buttons;
    private Runnable[] commandActions;

    EnemyBG enemyPanel = new EnemyBG(new Color(0, 255, 60), 140);
    JPanel dialoguePanel = new JPanel(new BorderLayout());

    private JLabel dialogueLabel;
    private FightMinigamePanel fightMinigame;
    private Component dialogueSlot;

    private final ImageIcon heartIcon = new ImageIcon(getClass().getResource("/resources/player_soul.png"));
    Font gameFont = UIUtils.undertaleFont;

    // == Roguelike Floor & Player Stats ==
    private String playerName = "DOM"; // Change to take from DB
    private int currentLvl = 1;
    private int currentHp = 20;
    private int maxHp = 20;
    private final int totalGameLvl = 15;

    // == Enemy Stats ==
    private int enemyHp = 50; // placeholder — swap for real per-enemy data later
    private int enemyMaxHp = 50;

    // == Enemy Progression Tracker ==
    private int enemyFightCount = 1;

    // UI Status Components ==
    private JLabel statsTextLabel;
    private JLabel hpMarker;
    private StatusBar healthBar;
    private JLabel hpNumericLabel;
    private StatusBar enemyHealthBar;
    private Image bulletSprite;

    private final Main main;

    public BattlePanel(Main main) {
        this.main = main;

        setLayout(null);
        setBackground(Color.BLACK);

        stage.setBackground(Color.BLACK);
        add(stage);

        loadBullet();
        buildEnemy();
        buildEnemyStatus();
        buildDialogueBox();
        buildPlayerStatus();
        buildCommandButtons();
        buildKeyBindings();

        updateSelection();
    }

    public void loadBullet() {
        try {
            var resource = getClass().getResource("/resources/bullet2.png");
            if (resource != null) {
                this.bulletSprite = javax.imageio.ImageIO.read(resource);
            } else {
                System.err.println("Bullet sprite not found");
            }
        } catch (java.io.IOException e) {
            System.err.println("Error reading bullet asset file: " + e.getMessage());
            this.bulletSprite = null;
        }
    }

    @Override
    public void doLayout() {
        stage.setBounds((getWidth() - STAGE_W) / 2, (getHeight() - STAGE_H) / 2, STAGE_W, STAGE_H);
    }

    // ==========================================
    // BUILD (each method places one region of the stage)
    // ==========================================

    private void buildEnemy() {
        enemyPanel.setLayout(null);
        enemyPanel.setBounds(ENEMY_X, ENEMY_Y, ENEMY_W, ENEMY_H);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/enemy.png"));
        Image img = originalIcon.getImage();

        int rawW = originalIcon.getIconWidth();
        int rawH = originalIcon.getIconHeight();

        int targetH = ENEMY_SPRITE;
        int targetW = (int) Math.round(((double) rawW / rawH) * targetH);

        Image scaledImage = img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
        JLabel enemySprite = new JLabel(new ImageIcon(scaledImage));

        enemySprite.setBounds(
                (ENEMY_W - targetW) / 2,
                (ENEMY_H - targetH) / 2,
                targetW,
                targetH);

        enemyPanel.add(enemySprite);
        stage.add(enemyPanel);
    }

    private void buildEnemyStatus() {
        JLabel marker = makeLabel("HP");
        int markerW = marker.getPreferredSize().width;
        int gap = 8;
        int x = (STAGE_W - (markerW + gap + BAR_W)) / 2; // marker + bar, centered under the enemy

        marker.setBounds(x, ENEMY_HP_Y, markerW, ROW_H);

        enemyHealthBar = new StatusBar(enemyHp, enemyMaxHp);
        enemyHealthBar.setBounds(x + markerW + gap, ENEMY_HP_Y + (ROW_H - BAR_H) / 2, BAR_W, BAR_H);

        stage.add(marker);
        stage.add(enemyHealthBar);
    }

    private void buildDialogueBox() {
        dialoguePanel.setBackground(Color.BLACK);
        dialoguePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                new EmptyBorder(15, 20, 15, 20)));

        dialogueLabel = makeLabel("Ribbit . . .");
        dialogueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialoguePanel.add(dialogueLabel, BorderLayout.CENTER);

        setDialogueSlot(dialoguePanel, BOX_X, BOX_Y, BOX_W, BOX_H);
    }

    private void buildPlayerStatus() {
        statsTextLabel = makeLabel(statsText());
        hpMarker = makeLabel("HP");
        healthBar = new StatusBar(currentHp, maxHp);
        hpNumericLabel = makeLabel(hpText());

        stage.add(statsTextLabel);
        stage.add(hpMarker);
        stage.add(healthBar);
        stage.add(hpNumericLabel);

        layoutStatusRow();
    }

    private void buildCommandButtons() {
        String[] names = { "FIGHT", "ACT", "ITEM", "MERCY" };
        buttons = new CommandButton[names.length];

        int totalW = names.length * BTN_W + (names.length - 1) * BTN_GAP;
        int startX = (STAGE_W - totalW) / 2;

        for (int i = 0; i < names.length; i++) {
            buttons[i] = new CommandButton(names[i], heartIcon);
            buttons[i].setBounds(startX + i * (BTN_W + BTN_GAP), BTN_Y, BTN_W, BTN_H);
            stage.add(buttons[i]);
        }

        // index-matched with buttons[] — Z runs commandActions[selectedBtnIndex]
        commandActions = new Runnable[] {
                this::startFightSequence,
                this::actPlaceholder,
                this::itemPlaceholder,
                this::mercyPlaceholder
        };
    }

    private void buildKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "select");

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (state != BattleState.PLAYER_TURN) {
                    return;
                }
                selectedBtnIndex--;
                if (selectedBtnIndex < 0) {
                    selectedBtnIndex = buttons.length - 1;
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
                if (selectedBtnIndex > buttons.length - 1) {
                    selectedBtnIndex = 0;
                }
                updateSelection();
            }
        });

        actionMap.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (state != BattleState.PLAYER_TURN) {
                    return;
                }
                commandActions[selectedBtnIndex].run();
            }
        });
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(gameFont);
        label.setForeground(Color.WHITE);
        return label;
    }

    // ==========================================
    // DIALOGUE / ARENA AREA
    // ==========================================

    private void setDialogueSlot(Component occupant, int x, int y, int w, int h) {
        if (dialogueSlot != null) {
            stage.remove(dialogueSlot);
        }
        dialogueSlot = occupant;
        occupant.setBounds(x, y, w, h);
        stage.add(occupant);
        stage.revalidate();
        stage.repaint();
    }

    private AnimateBox placeAnimator(int fromW, int fromH, int toW, int toH, Runnable onDone) {
        AnimateBox animator = new AnimateBox(fromW, fromH, toW, toH, onDone);
        setDialogueSlot(animator,
                BOX_X - BOX_PAD,
                BOX_Y - BOX_PAD,
                BOX_W + 2 * BOX_PAD,
                BOX_H + 2 * BOX_PAD);
        return animator;
    }

    // ==========================================
    // BATTLE FLOW
    // ==========================================

    private void actPlaceholder() {
    }

    private void itemPlaceholder() {
    }

    private void mercyPlaceholder() {
    }

    private void startFightSequence() {
        if (state != BattleState.PLAYER_TURN) {
            return;
        }
        state = BattleState.FIGHT_MINIGAME;
        clearSelectionIcons();

        dialoguePanel.removeAll();
        fightMinigame = new FightMinigamePanel(this::onHitEnemy);
        dialoguePanel.add(fightMinigame, BorderLayout.CENTER);

        dialoguePanel.revalidate();
        dialoguePanel.repaint();

        fightMinigame.start();
    }

    private void onHitEnemy(int damage) {
        enemyHp = Math.clamp(enemyHp - damage, 0, enemyMaxHp);
        enemyHealthBar.updateHp(enemyHp, enemyMaxHp);

        // TODO: enemyHp == 0 is where the win -> level up flow hooks in later
        Timer resultPause = new Timer(1200, e -> showBattleBox());
        resultPause.setRepeats(false);
        resultPause.start();
    }

    private void showBattleBox() {
        placeAnimator(BOX_W, BOX_H, ARENA_W, ARENA_H, this::finalizeBattleBox).startAnimation();
    }

    private void finalizeBattleBox() {
        ArenaPanel arena = new ArenaPanel(ARENA_W, ARENA_H, heartIcon,
                this::onPlayerHit,
                this::returnToPlayerTurn);

        // arena sits centered inside the dialogue area
        setDialogueSlot(arena,
                BOX_X + (BOX_W - ARENA_W) / 2,
                BOX_Y + (BOX_H - ARENA_H) / 2,
                ARENA_W,
                ARENA_H);

        state = BattleState.ENEMY_TURN;

        // TODO: pick a pattern (or sequence of patterns) per enemy/floor once there's
        // more than one
        arena.startTurn(new BulletRain(ARENA_W, ARENA_H, bulletSprite));
    }

    private void onPlayerHit(int damage) {
        updatePlayerStats(currentHp - damage, currentLvl);
    }

    private void returnToPlayerTurn() {
        placeAnimator(ARENA_W, ARENA_H, BOX_W, BOX_H, this::restoreDialogueBox).startAnimation();
    }

    private void restoreDialogueBox() {
        dialoguePanel.removeAll();
        dialoguePanel.add(dialogueLabel, BorderLayout.CENTER);
        setDialogueSlot(dialoguePanel, BOX_X, BOX_Y, BOX_W, BOX_H);

        state = BattleState.PLAYER_TURN;
        updateSelection();
    }

    // ==========================================
    // PLAYER STATUS ROW
    // ==========================================

    private String statsText() {
        return playerName + "   LEVEL " + currentLvl + "/" + totalGameLvl;
    }

    private String hpText() {
        return currentHp + " / " + maxHp;
    }

    private void layoutStatusRow() {
        int x = BOX_X;

        x = placeRowLabel(statsTextLabel, x) + 24;
        x = placeRowLabel(hpMarker, x) + 8;

        healthBar.setBounds(x, STATUS_Y + (ROW_H - BAR_H) / 2, BAR_W, BAR_H);
        x += BAR_W + 12;

        placeRowLabel(hpNumericLabel, x);
    }

    private int placeRowLabel(JLabel label, int x) {
        int w = label.getPreferredSize().width;
        label.setBounds(x, STATUS_Y, w, ROW_H);
        return x + w;
    }

    public void updatePlayerStats(int newHp, int level) {
        this.currentHp = Math.clamp(newHp, 0, maxHp);
        this.currentLvl = Math.clamp(level, 1, totalGameLvl);

        if (statsTextLabel != null && hpNumericLabel != null && healthBar != null) {
            statsTextLabel.setText(statsText());
            hpNumericLabel.setText(hpText());
            layoutStatusRow();

            healthBar.updateHp(currentHp, maxHp);
        }
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