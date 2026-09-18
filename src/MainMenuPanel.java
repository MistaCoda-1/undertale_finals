import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    
    JPanel menuPanel = new JPanel();
    JPanel artPanel = new JPanel();

    JPanel btnPanel = new JPanel();

    private final Main main;

    public MainMenuPanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        // == Uncomment/Comment to show bounds/panel borders == //
        btnPanel.setOpaque(false);
        menuPanel.setOpaque(false);
        artPanel.setOpaque(false);

        menuPanel.setBackground(Color.RED);
        artPanel.setBackground(Color.BLUE);
        
        menuPanel.setLayout(new GridBagLayout()); 
        
        JButton startBtn = new JButton("START");
        JButton leaderboardBtn = new JButton("LEADERBOARD");
        JButton optionstBtn = new JButton("OPTIONS");
        JButton exittBtn = new JButton("EXIT");

        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionstBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exittBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        UIUtils.styleButton(startBtn);
        UIUtils.styleButton(leaderboardBtn);
        UIUtils.styleButton(optionstBtn);
        UIUtils.styleButton(exittBtn);

        btnPanel.add(startBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(leaderboardBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(optionstBtn);
        btnPanel.add(Box.createVerticalStrut(20));

        btnPanel.add(exittBtn);
        
        menuPanel.add(btnPanel);

        add(menuPanel, BorderLayout.WEST);
        add(artPanel, BorderLayout.CENTER);

        setVisible(true);

        startBtn.addActionListener(e -> main.showBattlePanel());
        // leaderboardBtn.addActionListener(e -> main.leaderboardPanel);
        // optionstBtn.addActionListener(e -> main.optionsPanel);
        exittBtn.addActionListener(e -> main.showLogin());

    }
}