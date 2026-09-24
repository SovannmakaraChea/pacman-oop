package ui;

import java.awt.*;
import javax.swing.*;

public class MenuScreen extends JPanel {

    public MenuScreen(Runnable onPlay) {
        // Transparent so the GamePanel background shows through
        setOpaque(false);
        setLayout(new GridBagLayout());

        JLabel title = new JLabel("PAC-MAN");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 64));

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 28));
        playButton.setForeground(Color.BLACK);
        playButton.setBackground(Color.YELLOW);
        playButton.setFocusPainted(false);
        playButton.addActionListener(e -> onPlay.run());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(playButton, gbc);
    }
}
