package src;

import javax.swing.*;

// ==========================================
// You were working on:
//      ShowMainMenu()

/* =================
    ShowMainMenu()
        Finish getting the buttons to work.
        Remove temporary panels (red and blue panel)
*/

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

public class Main extends JFrame{

    public Main() {
        setTitle("UNDERTALEEEEE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        showLogin();
        // showMainMenu();

        setVisible(true);
    }

    public void showLogin() {
        setContentPane(new LoginPanel(this));
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        setContentPane(new MainMenuPanel(this));
        revalidate();
        repaint();
    }

    // To implement later
    // public void showBattlePanel() {
    //     setContentPane(new BattlePanel(this));
    // }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}