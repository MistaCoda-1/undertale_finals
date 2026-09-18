import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginPanel extends JPanel {
    
    private final JTextField textField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton submitButton = new JButton("DETERMINATION");

    private final ImageIcon logo = new ImageIcon(getClass().getResource("resources/logo.png"));    // Placeholder, change this to your own art
    private final JLabel label = new JLabel(logo);

    ImageIcon sprite = new ImageIcon("resources/player_soul.png");
    JLabel loginStatusLabel = UIUtils.createLabel("");

    private final Main main;

    public LoginPanel(Main main) {
        this.main = main;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        UIUtils.styleInputField(textField);
        UIUtils.styleInputField(passwordField);
        UIUtils.styleButton(submitButton);

        JLabel usernameLabel = UIUtils.createLabel("USERNAME: ");
        JLabel passwordLabel = UIUtils.createLabel("PASSWORD: ");

        JPanel userRow = createRow(usernameLabel, textField);
        JPanel passRow = createRow(passwordLabel, passwordField);

        label.setAlignmentX(CENTER_ALIGNMENT);
        userRow.setAlignmentX(CENTER_ALIGNMENT);
        passRow.setAlignmentX(CENTER_ALIGNMENT);
        submitButton.setAlignmentX(CENTER_ALIGNMENT);

        add(label);
        
        add(userRow);
        add(Box.createVerticalStrut(5));
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
                // submitButton.setText("Logging In . . .");
                // loginStatusLabel.setText("LOGIN SUCCESSFUL!");
                main.showMainMenu();
            } else {
                loginStatusLabel.setText("LOGIN FAILED!");
            }
        } finally {
            Arrays.fill(password, '0');
        }
    }

    private JPanel createRow(JLabel label, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0));
        row.setOpaque(false);

        row.setPreferredSize(new Dimension(400, 45));
        row.setMaximumSize(new Dimension(400, 45));

        row.add(label);
        row.add(field);
        return row;
    }
}