package map;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashSet;

public class MapLoader extends JPanel {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    int rowCount = 21;
    int columnCount = 19;
    int tileSize = 32;

    int boardWidth = columnCount * tileSize;
    int boardHeight = rowCount * tileSize;

    Image wallImage;

    HashSet<Block> walls = new HashSet<>();

    String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XX XX X XXXX",
            "O                 O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X           X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public MapLoader() {

        setPreferredSize(
                new Dimension(boardWidth, boardHeight)
        );

        setBackground(Color.BLACK);

        URL wallResource = MapLoader.class.getResource("/images/pacman/wall.png");
        wallImage = wallResource == null ? null : new ImageIcon(wallResource).getImage();


        loadMap();
    }

    public void loadMap() {

        walls.clear();

        for (int r = 0; r < rowCount ;r++) {

            for (int c = 0; c < columnCount; c++) {

                char tileMapChar = tileMap[r].charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {

                    Block wall = new Block(
                            wallImage,
                            x,
                            y,
                            tileSize,
                            tileSize
                    );

                    walls.add(wall);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (Block wall : walls) {

            if (wall.image == null) {
                g.setColor(new Color(30, 90, 200));
                g.fillRect(wall.x, wall.y, wall.width, wall.height);
            } else {
                g.drawImage(
                        wall.image,
                        wall.x,
                        wall.y,
                        wall.width,
                        wall.height,
                        null
                );
            }
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Pac-Man Map");

        MapLoader map = new MapLoader();

        frame.add(map);

        frame.pack();

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}