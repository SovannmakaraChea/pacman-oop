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

        add(new MenuScreen(onPlay));
    }
}
