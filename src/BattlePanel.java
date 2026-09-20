import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

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
    JPanel enemyPanel = new JPanel();
    JPanel dialogueContainer = new JPanel(new BorderLayout());
    JPanel dialoguePanel = new JPanel(new BorderLayout());
    private FightMinigamePanel fightMinigame;

    private final ImageIcon heartIcon = new ImageIcon(getClass().getResource("resources/player_soul.png"));

    private final Main main;

    public BattlePanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("resources/enemy.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(
                250,
                250,
                Image.SCALE_SMOOTH);
        JLabel enemySprite = new JLabel(new ImageIcon(scaledImage));
        enemyPanel.setPreferredSize(new Dimension(300, 300));
        enemyPanel.add(enemySprite);

        dialogueContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        dialoguePanel.setBackground(Color.BLACK);
        dialoguePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                new EmptyBorder(15, 20, 15, 20)));

        dialogueContainer.add(dialoguePanel, BorderLayout.CENTER);

        JLabel dialogueLabel = new JLabel("...");
        dialogueLabel.setForeground(Color.WHITE);
        dialogueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialoguePanel.add(dialogueLabel, BorderLayout.CENTER);

        btnPanel.setPreferredSize(new Dimension(300, 80));

        // == PANEL COLORS FOR DEBUGGING == //
        // Set all to black when done.
        enemyPanel.setBackground(Color.red);
        dialogueContainer.setBackground(Color.green);
        btnPanel.setBackground(Color.blue);

        btnPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

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

        // index-matched with buttons[] — Z runs commandActions[selectedBtnIndex]
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

        add(enemyPanel, BorderLayout.NORTH);
        add(dialogueContainer, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

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

        // Z confirms whichever command is currently selected
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

    /**
     * Swaps the dialogue box's contents (not the box itself) for the timing
     * minigame. Guarded by state so mashing FIGHT mid-minigame can't stack
     * multiple minigames on top of each other.
     */
    private void startFightSequence() {
        if (state != BattleState.PLAYER_TURN) {
            return;
        }
        state = BattleState.FIGHT_MINIGAME;
        clearSelectionIcons(); // hide the heart so it's clear the menu isn't active

        dialoguePanel.removeAll();
        fightMinigame = new FightMinigamePanel(this::onFightDamageDealt);
        dialoguePanel.add(fightMinigame, BorderLayout.CENTER);

        dialoguePanel.revalidate();
        dialoguePanel.repaint();

        fightMinigame.start();
    }

    // ACT/ITEM/MERCY aren't implemented yet — these just fill the slots in
    // commandActions so selecting them doesn't blow up.
    private void actPlaceholder() { }

    private void itemPlaceholder() { }

    private void mercyPlaceholder() { }

    /** Called once the player locks in their hit; briefly shows the result, then starts the battle. */
    private void onFightDamageDealt(int damage) {
        Timer resultPause = new Timer(1200, e -> showBattleBox());
        resultPause.setRepeats(false);
        resultPause.start();
    }

    /**
     * Shrinks the dialogue area down into the battle box with the player's
     * soul centered inside. Bullet patterns / enemy attacks come later —
     * this just lays down where that will live.
     */
    private void showBattleBox() {
        dialogueContainer.removeAll();

        JPanel battleBox = new JPanel(null);
        battleBox.setBackground(Color.BLACK);
        battleBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        battleBox.setPreferredSize(new Dimension(300, 250));

        JLabel heartLabel = new JLabel(heartIcon);
        int heartX = (300 - heartIcon.getIconWidth()) / 2;
        int heartY = (250 - heartIcon.getIconHeight()) / 2;
        heartLabel.setBounds(heartX, heartY, heartIcon.getIconWidth(), heartIcon.getIconHeight());
        battleBox.add(heartLabel);

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setOpaque(false);
        centeringWrapper.add(battleBox);

        dialogueContainer.add(centeringWrapper, BorderLayout.CENTER);
        dialogueContainer.revalidate();
        dialogueContainer.repaint();

        state = BattleState.ENEMY_TURN;
    }

    private void updateSelection() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(i == selectedBtnIndex);
        }
    }

    /** Blanks the heart off every command button so the menu reads as inactive. */
    private void clearSelectionIcons() {
        for (CommandButton button : buttons) {
            button.setSelected(false);
        }
    }
}