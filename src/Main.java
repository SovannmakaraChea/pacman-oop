import ui.GamePanel;

import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tilesize = 32;
        int boardWidth = columnCount = tilesize;
        int boardHeight = rowCount = tilesize;

        JFrame frame = new JFrame("Pac Man");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel pacmanGame = new GamePanel();
        frame.add(pacmanGame);
        frame.pack();
        frame.setVisible(true);
    }
}