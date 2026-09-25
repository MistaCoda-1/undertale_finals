package ui;
import javax.swing.*;
import java.awt.*;

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

    public void setSelected(boolean selected) {
        this.selected = selected;

        Color highlight = selected ? Color.YELLOW : Color.WHITE;
        textLabel.setForeground(highlight);
        setBorder(BorderFactory.createLineBorder(highlight, 1));
    }
}