package ui;

import game.GameSettings;
import map.GameMaps;

import java.awt.*;
import javax.swing.*;

public class LevelScreen extends JPanel {

    private static final int PER_ROW = 5;

    public LevelScreen(GameSettings settings, Runnable onPlay, Runnable onBack) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 32, 0);
        add(new MenuTitle("LEVELS", 64), gbc);

        JPanel grid = new JPanel(new GridLayout(0, PER_ROW, 12, 12));
        grid.setOpaque(false);

        for (int level = 1; level <= GameMaps.LEVEL_COUNT; level++) {
            int picked = level;
            MenuButton button = new MenuButton(String.valueOf(level), 26, 72, 72, () -> {
                settings.setLevel(picked);
                onPlay.run();
            });

            if (!GameMaps.isUnlocked(level)) {
                button.setEnabled(false);
                button.setForeground(Color.GRAY);
                button.setCursor(Cursor.getDefaultCursor());
            }

            grid.add(button);
        }

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 24, 0);
        add(grid, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 32, 0);
        add(new MenuScreen.Subtitle("GREY LEVELS ARE LOCKED"), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(new MenuButton("BACK", 24, 220, 56, onBack), gbc);
    }
}
