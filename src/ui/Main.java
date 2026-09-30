package ui;
import javax.swing.*;

import battle.BattlePanel;

// ==========================================
// You were working on: #1
//      Gameplay mechs
//      Options
//      Completely Revamp Login Page to add registration

/* =================
    More things to do: #2
        Create background art for blue panel area
        Create art for logo
        Create art for app icon
        Create enemy character sprite
*/

/* ================
    Small Fixes: #3
        BattlePanel.java l.35
        BattlePanel.java l.287
*/

/* ================
    Random shit to add: #4
        Miku miku beam button
        Miku button (turns enemy character sprite into miku)
*/

/* ================
    Complete/Done files:
        AnimatBox.java
        EnemyBG.java
        CommandButton.java
        LeaderboardEntry.java
        LeaderboardOverlay.java
        LeaderboardPanel.java
        MenuButton.java
        UIUtils.java
*/

public class Main extends JFrame {

    public Main() {
        setTitle("HeartBreak!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        // showLogin();
        showMainMenu();
        // showBattlePanel();

        setVisible(true);
    }

    public void showLogin() {
        JPanel login = new LoginPanel(this);
        setContentPane(login);
        validate();
        repaint();
        login.requestFocusInWindow();
    }

    public void showMainMenu() {
        JPanel mainMenu = new MainMenuPanel(this);
        setContentPane(mainMenu);
        validate();
        repaint();
        mainMenu.requestFocusInWindow();
    }

    public void showBattlePanel() {
        JPanel battle = new BattlePanel(this);
        setContentPane(battle);
        validate();
        repaint();
        battle.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}