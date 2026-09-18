import javax.swing.*;

// ==========================================
// You were working on:
//      ShowMainMenu()

/* =================
    ShowMainMenu()
        Finish getting the buttons to work. *
        Remove temporary panels (red and blue panel) *
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

public class Main extends JFrame {

    public Main() {
        setTitle("UNDERTALEEEEE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        showLogin();
        // showMainMenu();
        // showBattlePanel();

        setVisible(true);
    }

    public void showLogin() {
        JPanel login = new LoginPanel(this);
        setContentPane(login);
        revalidate();
        repaint();
        login.requestFocusInWindow();
    }

    public void showMainMenu() {
        JPanel mainMenu = new MainMenuPanel(this);
        setContentPane(mainMenu);
        revalidate();
        repaint();
        mainMenu.requestFocusInWindow();
    }

    public void showBattlePanel() {
        JPanel battle = new BattlePanel(this);
        setContentPane(battle);
        revalidate();
        repaint();
        battle.requestFocusInWindow();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}