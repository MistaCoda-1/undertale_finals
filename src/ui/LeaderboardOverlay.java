package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

public class LeaderboardOverlay extends JPanel {

    public LeaderboardOverlay(Runnable onClose) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setFocusable(true);

        add(new LeaderboardPanel());

        addMouseListener(new MouseAdapter() { });

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "close");
        actionMap.put("close", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onClose.run();
            }
        });
    }
}