package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LeaderboardPanel extends JPanel {
    Font undertaleFont = UIUtils.loadFont("/resources/8bitoperator-jve/8bitoperator_jve.ttf", 24f);

    public LeaderboardPanel() {
        setPreferredSize(new Dimension(420, 480));
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                new EmptyBorder(20, 30, 15, 30)));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(undertaleFont);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel scoresPanel = new JPanel();
        scoresPanel.setOpaque(false);
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));

        // placeholder data — swap for real rows once there's a database behind this
        String[] placeholderScores = {
                "1.  PLAYER          9999",
                "2.  PLAYER          8500",
                "3.  PLAYER          7200",
                "4.  PLAYER          6100",
                "5.  PLAYER          5000",
                "6.  PLAYER          4400",
                "7.  PLAYER          3900",
                "8.  PLAYER          3200",
                "9.  PLAYER          2600",
                "10. PLAYER          2000"
        };

        for (String entry : placeholderScores) {
            scoresPanel.add(createScoreLabel(entry));
        }
        add(scoresPanel, BorderLayout.CENTER);

        JLabel returnLabel = new JLabel("Press X to return to Main Menu", SwingConstants.CENTER);
        returnLabel.setForeground(Color.LIGHT_GRAY);
        returnLabel.setFont(undertaleFont);
        returnLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        add(returnLabel, BorderLayout.SOUTH);
    }

    private JLabel createScoreLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(undertaleFont);
        label.setBorder(new EmptyBorder(3, 0, 3, 0));
        return label;
    }
}