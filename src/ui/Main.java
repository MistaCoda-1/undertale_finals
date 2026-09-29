package ui;
import javax.swing.*;

import battle.BattlePanel;

// ==========================================
// You were working on:
//      Options

/* =================
    More things to do:
        Create background art for blue panel area
        Create art for logo
        Create art for app icon
        Create enemy character sprite
*/

/* ================
    Random shit to add:
        Miku miku beam button
        Miku button (turns enemy character sprite into miku)
*/

/* ================
    Shit to fix:
        Some functions in UIUtils need to be moved to
        where they're used. Stylize button is only used inside
        LoginPanel.
*/

public class Main extends JFrame {

    public Main() {
        setTitle("Undertale Mock Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        showLogin();

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