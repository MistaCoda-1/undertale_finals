import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class UIUtils {
    static Font undertaleFont = UIUtils.loadFont("8bitoperator-jve/8bitoperator_jve.ttf", 24f);

    private UIUtils() { }

    public static Font loadFont(String path, float size) {
        try (InputStream is = UIUtils.class.getResourceAsStream(path)) {
            if (is == null) throw new IOException("File not found at: " + path);
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            System.err.println("Font error: " + e.getMessage() + ". Defaulting to Monospaced.");
            return new Font("Monospaced", Font.BOLD, (int) size);
        }
    }

    public static void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFont(undertaleFont);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setForeground(Color.YELLOW);
                    button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setForeground(Color.WHITE);
                    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                }
            }
        );
    }

    public static void styleInputField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setMaximumSize(new Dimension(200, 35));
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(undertaleFont);
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(undertaleFont);
        return lbl;
    }
}