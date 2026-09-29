package ui;

import game.Difficulty;
import game.GameSettings;
import game.Skin;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class SettingsScreen extends JPanel {

    private final GameSettings settings;

    private final JLabel difficultyValue = valueLabel();
    private final JLabel skinValue = valueLabel();
    private final SkinPreview preview = new SkinPreview();

    public SettingsScreen(GameSettings settings, Runnable onBack) {
        this.settings = settings;

        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new MenuTitle("SETTINGS", 64), gbc);

        addRow(1, "DIFFICULTY", difficultyValue,
                () -> changeDifficulty(-1), () -> changeDifficulty(1));
        addRow(2, "SKIN", skinValue,
                () -> changeSkin(-1), () -> changeSkin(1));

        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 12, 16, 0);
        add(preview, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(40, 0, 0, 0);
        add(new MenuButton("BACK", 24, 220, 56, onBack), gbc);

        refresh();
    }

    private void addRow(int row, String name, JLabel value, Runnable onPrevious, Runnable onNext) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 16, 12);

        JLabel label = new JLabel(name);
        label.setForeground(MenuScreen.INK);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(label, gbc);

        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 1;
        add(new MenuButton("<", 22, 48, 48, onPrevious), gbc);

        gbc.gridx = 2;
        add(value, gbc);

        gbc.gridx = 3;
        add(new MenuButton(">", 22, 48, 48, onNext), gbc);
    }

    private static JLabel valueLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setForeground(MenuScreen.INK);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setPreferredSize(new Dimension(120, 48));
        return label;
    }

    private void changeDifficulty(int step) {
        Difficulty[] all = Difficulty.values();
        int next = Math.floorMod(settings.getDifficulty().ordinal() + step, all.length);
        settings.setDifficulty(all[next]);
        refresh();
    }

    private void changeSkin(int step) {
        Skin[] all = Skin.values();
        int next = Math.floorMod(settings.getSkin().ordinal() + step, all.length);
        settings.setSkin(all[next]);
        refresh();
    }

    private void refresh() {
        difficultyValue.setText(settings.getDifficulty().getLabel());
        skinValue.setText(settings.getSkin().getLabel());
        preview.setSkin(settings.getSkin());
    }

    private static class SkinPreview extends JComponent {

        private static final int SIZE = 40;

        private Image image;

        SkinPreview() {
            setPreferredSize(new Dimension(SIZE, SIZE));
        }

        void setSkin(Skin skin) {
            URL resource = SkinPreview.class.getResource(skin.imagePath("Right"));
            image = resource == null ? null : new ImageIcon(resource).getImage();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image != null) {
                g.drawImage(image, 0, 0, SIZE, SIZE, null);
            }
        }
    }
}
