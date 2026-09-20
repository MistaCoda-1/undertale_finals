import javax.swing.*;
import java.awt.*;

/**
 * A non-interactive, "statically drawn" stand-in for JButton, used for the
 * main menu (START / LEADERBOARD / OPTIONS / EXIT). No mouse listeners or
 * focus handling — MainMenuPanel is the only thing that changes its state,
 * via setSelected(), driven by keyboard navigation, so these can never be
 * clicked.
 *
 * Kept independent of UIUtils on purpose so it doesn't drag along the font
 * dependency if UIUtils ends up scoped down to LoginPanel only.
 */
public class MenuButton extends JPanel {

    private final JLabel textLabel;
    private boolean selected = false;

    public MenuButton(String text) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(220, 40));
        setMaximumSize(new Dimension(220, 40));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        textLabel = UIUtils.createLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(textLabel, BorderLayout.CENTER);
    }

    /** Toggles selected look: yellow border/text when this entry is the current one. */
    public void setSelected(boolean selected) {
        this.selected = selected;

        Color highlight = selected ? Color.YELLOW : Color.WHITE;
        textLabel.setForeground(highlight);
        setBorder(BorderFactory.createLineBorder(highlight, 1));
    }
}