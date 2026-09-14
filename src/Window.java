package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;

public class Window {

    private final JFrame frame = new JFrame("Anderteyl");
    private final JPanel loginPanel = new JPanel();
    private final JTextField textField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton submitButton = new JButton("DETERMINATION");

    private final ImageIcon logo = new ImageIcon(getClass().getResource("/resources/logo.png"));    // Placeholder, change this to your own art
    private final JLabel label = new JLabel(logo);

    public Window() {
    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().setBackground(Color.BLACK);

        styleInputField(textField);
        styleInputField(passwordField);
        styleButton(submitButton);

        Font undertaleFont = loadFont("/resources/8bitoperator.ttf", 24f);
        JLabel usernameLabel = createLabel("USERNAME: ", undertaleFont);
        JLabel passwordLabel = createLabel("PASSWORD: ", undertaleFont);

        JPanel userRow = createRow(usernameLabel, textField);
        JPanel passRow = createRow(passwordLabel, passwordField);

        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.BLACK);
        
        frame.add(Box.createVerticalStrut(30));
        
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        frame.add(label);
        
        frame.add(Box.createVerticalStrut(10));

        loginPanel.add(userRow);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(passRow);

        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(submitButton);

        loginPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        frame.add(loginPanel);

        frame.add(Box.createVerticalGlue());

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

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

    private void styleInputField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 25));
        field.setMaximumSize(new Dimension(200, 25));
        field.setBackground(Color.BLACK);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        field.setHorizontalAlignment(JTextField.CENTER);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(font);
        return lbl;
    }

    private JPanel createRow(JLabel label, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0));
        row.setOpaque(false);

        row.setPreferredSize(new Dimension(400, 30));
        row.setMaximumSize(new Dimension(400, 30));

        row.add(label);
        row.add(field);
        return row;
    }

    private Font loadFont(String path, float size) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new IOException();
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            System.err.println("Font resource missing. Defaulting to Monospaced.");
            return new Font("Monospaced", Font.PLAIN, (int) size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Window::new);
    }
}