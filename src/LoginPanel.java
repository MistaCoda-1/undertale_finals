package src;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static src.UIUtils.styleButton;
import static src.UIUtils.loadFont;

public class LoginPanel extends JPanel {
    
    private final JTextField textField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton submitButton = new JButton("DETERMINATION");

    private final ImageIcon logo = new ImageIcon(getClass().getResource("/resources/logo.png"));    // Placeholder, change this to your own art
    private final JLabel label = new JLabel(logo);

    Font undertaleFont = loadFont("/resources/8bitoperator.ttf", 24f);
    JLabel loginStatusLabel = createLabel("", undertaleFont);

    private final Main main;

    public LoginPanel(Main main) {
        this.main = main;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        styleInputField(textField);
        styleInputField(passwordField);
        styleButton(submitButton);

        JLabel usernameLabel = createLabel("USERNAME: ", undertaleFont);
        JLabel passwordLabel = createLabel("PASSWORD: ", undertaleFont);

        JPanel userRow = createRow(usernameLabel, textField);
        JPanel passRow = createRow(passwordLabel, passwordField);

        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        
        add(userRow);
        add(Box.createVerticalStrut(20));
        add(passRow);
        
        add(Box.createVerticalStrut(20));
        add(submitButton);
        
        add(Box.createVerticalStrut(20));
        loginStatusLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(loginStatusLabel);

        add(Box.createVerticalGlue());

        submitButton.addActionListener(e -> login());
        textField.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
    }

    private void login() {  // Hardcoded username & password for now
        String username = textField.getText().trim();
        char[] password = passwordField.getPassword();

        // submitButton.setText("LOGGING IN . . .");

        try {
            if (username.equals("dom") && Arrays.equals("123".toCharArray(), password)) {
                submitButton.setEnabled(false);
                submitButton.setText("Logging In . . .");
                loginStatusLabel.setText("LOGIN SUCCESSFUL!");
                main.showMainMenu();
            } else {
                loginStatusLabel.setText("LOGIN FAILED!");
            }
        } finally {
            Arrays.fill(password, '0');
        }
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
}