package battle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import ui.UIUtils;

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

        textLabel = UIUtils.createLabel(text);
        textLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

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

        Color highlight = selected ? Color.YELLOW : new Color(224, 132, 66);
        textLabel.setForeground(highlight);
        setBorder(BorderFactory.createLineBorder(highlight, 3));

        iconLabel.setIcon(selected ? heartIcon : commandIcon);
    }

    private static ImageIcon blankIcon(ImageIcon sizedLike) {
        return new ImageIcon(new BufferedImage(
                sizedLike.getIconWidth(),
                sizedLike.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB));
    }
}