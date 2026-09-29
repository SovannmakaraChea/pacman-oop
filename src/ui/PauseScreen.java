package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class PauseScreen extends JPanel {

    public PauseScreen(Runnable onResume, Runnable onRestart, Runnable onMainMenu) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new MenuTitle("PAUSED", 72), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        add(new MenuButton("RESUME", 30, 220, 64, onResume), gbc);

        gbc.gridy = 2;
        add(new MenuButton("RESTART", 24, 220, 56, onRestart), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new MenuButton("MAIN MENU", 24, 220, 56, onMainMenu), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(new MenuScreen.Subtitle("PRESS ESC TO RESUME"), gbc);

        getInputMap(WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "resume");
        getActionMap().put("resume", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isShowing()) {
                    onResume.run();
                }
            }
        });
    }
}
