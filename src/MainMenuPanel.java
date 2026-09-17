package src;

import javax.swing.*;
import java.awt.*;

import static src.UIUtils.styleButton;

public class MainMenuPanel extends JPanel{
    
    JPanel redPanel = new JPanel();
    JPanel bluePanel = new JPanel();

    JPanel buttonsPanel = new JPanel();

    private final Main main;

    public MainMenuPanel(Main main) {
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        redPanel.setBackground(Color.RED);
        bluePanel.setBackground(Color.BLUE);

        JButton startBtn = new JButton("START");
        JButton leaderboardBtn = new JButton("LEADERBOARD");
        JButton optionstBtn = new JButton("OPTIONS");
        JButton exittBtn = new JButton("EXIT");

        styleButton(startBtn);
        styleButton(leaderboardBtn);
        styleButton(optionstBtn);
        styleButton(exittBtn);

        buttonsPanel.add(Box.createVerticalStrut(350));
        buttonsPanel.add(startBtn);
        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(leaderboardBtn);
        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(optionstBtn);
        buttonsPanel.add(Box.createVerticalStrut(20));

        buttonsPanel.add(exittBtn);

        buttonsPanel.add(Box.createVerticalGlue());

        redPanel.add(buttonsPanel);

        add(redPanel, BorderLayout.WEST);
        add(bluePanel, BorderLayout.CENTER);

        setVisible(true);
    }
}