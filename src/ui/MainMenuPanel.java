package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainMenuPanel extends JPanel {

    JPanel menuPanel = new JPanel();
    JPanel artPanel = new JPanel();

    JPanel btnPanel = new JPanel();

    private int selectedBtnIndex = 0;
    private MenuButton[] buttons;
    private Runnable[] menuActions;
    private boolean leaderboardOpen = false;

    private final Main main;

    public MainMenuPanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

        // == Uncomment/Comment to show bounds/panel borders == //
        btnPanel.setOpaque(false);
        // menuPanel.setOpaque(false);
        // artPanel.setOpaque(false);

        menuPanel.setBackground(Color.RED);
        artPanel.setBackground(Color.BLUE);

        menuPanel.setLayout(new GridBagLayout());

        MenuButton startBtn = new MenuButton("START");
        MenuButton leaderboardBtn = new MenuButton("LEADERBOARD");
        MenuButton optionsBtn = new MenuButton("OPTIONS");
        MenuButton exitBtn = new MenuButton("EXIT");

        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttons = new MenuButton[] {
                startBtn,
                leaderboardBtn,
                optionsBtn,
                exitBtn
        };

        menuActions = new Runnable[] {
                main::showBattlePanel,
                this::showLeaderboard,
                this::optionsPlaceholder,
                main::showLogin
        };

        btnPanel.add(startBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(leaderboardBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(optionsBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(exitBtn);

        menuPanel.add(btnPanel);

        add(menuPanel, BorderLayout.WEST);
        add(artPanel, BorderLayout.CENTER);

        updateSelection();

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "select");

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (leaderboardOpen) {
                    return;
                }
                selectedBtnIndex--;
                if (selectedBtnIndex < 0) {
                    selectedBtnIndex = buttons.length - 1;
                }
                updateSelection();
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (leaderboardOpen) {
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
                if (leaderboardOpen) {
                    return;
                }
                menuActions[selectedBtnIndex].run();
            }
        });

        setVisible(true);
    }

    /** Opens the leaderboard as a glass-pane popup over the current menu. */
    private void showLeaderboard() {
        if (leaderboardOpen) {
            return;
        }
        leaderboardOpen = true;

        LeaderboardOverlay overlay = new LeaderboardOverlay(this::hideLeaderboard);
        main.getRootPane().setGlassPane(overlay);
        overlay.setVisible(true);
        overlay.requestFocusInWindow();
    }

    /** Called by the overlay when X is pressed. */
    private void hideLeaderboard() {
        leaderboardOpen = false;
        main.getRootPane().getGlassPane().setVisible(false);
        requestFocusInWindow(); // hand keyboard control back to the menu
    }

    private void optionsPlaceholder() { }

    private void updateSelection() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(i == selectedBtnIndex);
        }
    }
}