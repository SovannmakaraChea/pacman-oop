package ui;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private int rowCount = 21;
    private int columnCount = 19;
    private int tilesize = 32;
    private int boardWidth = columnCount * tilesize;
    private int boardHeight = rowCount * tilesize;

    public GamePanel(Runnable onPlay) {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.YELLOW);
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
