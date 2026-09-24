package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel {
    private int rowCount = 21;
    private int columnCount = 19;
    private int tilesize = 32;
    private int boardWidth = columnCount = tilesize;
    private int boardHeight = rowCount = tilesize;

    public GamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.YELLOW);
    }
}