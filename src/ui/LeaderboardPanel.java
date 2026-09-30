package ui;

import db.LeaderboardDAO;
import db.LeaderboardEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeaderboardPanel extends JPanel {
    Font gameFont = UIUtils.undertaleFont;
    private static final int TOP_LIMIT = 10;

    private final JPanel tablePanel = new JPanel(new GridBagLayout());

    public LeaderboardPanel() {
        setPreferredSize(new Dimension(520, 480));
        setBackground(Color.BLACK);
        setOpaque(true);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                new EmptyBorder(20, 30, 15, 30)));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(gameFont);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        tablePanel.setOpaque(false);
        add(tablePanel, BorderLayout.CENTER);

        JLabel returnLabel = new JLabel("Press X to return to Main Menu", SwingConstants.CENTER);
        returnLabel.setForeground(Color.LIGHT_GRAY);
        returnLabel.setFont(gameFont);
        returnLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        add(returnLabel, BorderLayout.SOUTH);

        showMessage("Loading...");
        loadScores();
    }

    private void loadScores() {
        new SwingWorker<List<LeaderboardEntry>, Void>() {
            @Override
            protected List<LeaderboardEntry> doInBackground() throws Exception {
                return new LeaderboardDAO().getTopPlayers(TOP_LIMIT);
            }

            @Override
            protected void done() {
                try {
                    showEntries(get());
                } catch (InterruptedException | ExecutionException e) {
                    // e.printStackTrace(); // uncomment for debugging
                    System.out.println("Open XAMPP first.");
                    showMessage("Could not load leaderboard.");
                }
            }
        }.execute();
    }

    private void showEntries(List<LeaderboardEntry> entries) {
        tablePanel.removeAll();

        addRow(0, "RANK", "USERNAME", "HIGHEST LEVEL", Color.YELLOW);

        int row = 1;
        for (LeaderboardEntry entry : entries) {
            addRow(row++,
                    String.valueOf(entry.getRank()),
                    entry.getUsername(),
                    String.valueOf(entry.getHighestLevel()),
                    Color.WHITE);
        }

        if (entries.isEmpty()) {
            addSpanningMessage(row++, "No scores yet.");
            addFiller(row);
        } else {
            addFiller(row);
        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void showMessage(String message) {
        tablePanel.removeAll();
        addRow(0, "RANK", "USERNAME", "HIGHEST LEVEL", Color.YELLOW);
        addSpanningMessage(1, message);
        addFiller(2);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void addRow(int row, String rank, String username, String level, Color color) {
        addCell(rank, 0, row, 0.2, SwingConstants.LEFT, color);
        addCell(username, 1, row, 0.5, SwingConstants.LEFT, color);
        addCell(level, 2, row, 0.3, SwingConstants.RIGHT, color);
    }

    private void addCell(String text, int col, int row, double weightX, int align, Color color) {
        JLabel label = new JLabel(text, align);
        label.setForeground(color);
        label.setFont(gameFont);
        label.setBorder(new EmptyBorder(4, 0, 4, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = weightX;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tablePanel.add(label, gbc);
    }

    private void addSpanningMessage(int row, String message) {
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(gameFont);
        label.setBorder(new EmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tablePanel.add(label, gbc);
    }

    private void addFiller(int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        tablePanel.add(Box.createGlue(), gbc);
    }
}