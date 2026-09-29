package ui;

import java.awt.*;
import javax.swing.*;

public class MenuScreen extends JPanel {

    static final Color INK = new Color(20, 20, 20);
    static final Color SHADOW = new Color(200, 120, 0);

    public MenuScreen(Runnable onPlay, Runnable onLevels, Runnable onSettings, Runnable onExit) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new MenuTitle("PAC-MAN", 84), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        add(new MenuButton("PLAY", 30, 220, 64, onPlay), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        add(new MenuButton("LEVELS", 24, 220, 56, onLevels), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 16, 0);
        add(new MenuButton("SETTINGS", 24, 220, 56, onSettings), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new MenuButton("EXIT", 24, 220, 56, onExit), gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 6, 0);
        add(new Subtitle("ARROW KEYS OR WASD TO MOVE"), gbc);

        gbc.gridy = 6;
        add(new Subtitle("EAT A CHERRY TO SCARE THE GHOSTS"), gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(new Subtitle("ESC OR P TO PAUSE"), gbc);
    }

    static class Subtitle extends JLabel {

        Subtitle(String text) {
            super(spaced(text));
            setForeground(INK);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        private static String spaced(String text) {
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                sb.append(c).append(c == ' ' ? "  " : " ");
            }
            return sb.toString().trim();
        }
    }
}
