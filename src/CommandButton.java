import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A non-interactive, "statically drawn" stand-in for JButton, used for the
 * FIGHT / ACT / ITEM / MERCY commands. It has no mouse listeners or focus
 * handling at all — BattlePanel is the only thing that changes its state,
 * via setSelected(), driven by keyboard navigation. That means these can
 * never be clicked, by construction, rather than by disabling JButton's
 * built-in behavior after the fact.
 *
 * Layout: command text on the left, an icon slot on the right. The icon
 * slot defaults to a blank placeholder the same size as the heart icon —
 * swap in a real per-command icon later with setCommandIcon(). Whichever
 * command is currently selected shows the heart in that slot instead.
 */
public class CommandButton extends JPanel {

    private final JLabel textLabel;
    private final JLabel iconLabel;
    private final ImageIcon heartIcon;

    private ImageIcon commandIcon;
    private boolean selected = false;

    public CommandButton(String text, ImageIcon heartIcon) {
        this.heartIcon = heartIcon;
        this.commandIcon = blankIcon(heartIcon);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 40));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));

        textLabel = UIUtils.createLabel(text);
        textLabel.setBorder(new EmptyBorder(0, 0, 0, 12));

        iconLabel = new JLabel(commandIcon);
        iconLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        add(textLabel, BorderLayout.EAST);
        add(iconLabel, BorderLayout.WEST);
    }

    /** Sets the icon shown in the right-hand slot whenever this command isn't selected. */
    public void setCommandIcon(ImageIcon icon) {
        this.commandIcon = icon;
        if (!selected) {
            iconLabel.setIcon(commandIcon);
        }
    }

    /** Toggles selected look: yellow highlight, and the heart takes over the icon slot. */
    public void setSelected(boolean selected) {
        this.selected = selected;

        Color highlight = selected ? Color.YELLOW : Color.WHITE;
        textLabel.setForeground(highlight);
        setBorder(BorderFactory.createLineBorder(highlight, 1));

        iconLabel.setIcon(selected ? heartIcon : commandIcon);
    }

    private static ImageIcon blankIcon(ImageIcon sizedLike) {
        return new ImageIcon(new BufferedImage(
                sizedLike.getIconWidth(),
                sizedLike.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB));
    }
}