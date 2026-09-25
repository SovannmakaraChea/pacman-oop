package map;

import entities.Pellet;
import entities.ghosts.Blinky;
import entities.ghosts.Clyde;
import entities.ghosts.Ghost;
import entities.ghosts.Inky;
import entities.ghosts.Pinky;

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

    // Ghost images
    Image blinkyImage;
    Image pinkyImage;
    Image inkyImage;
    Image clydeImage;
    Image scaredGhostImage;

    // Maze data
    HashSet<Block> walls = new HashSet<>();
    HashSet<Pellet> pellets = new HashSet<>();
    HashSet<Rectangle> wallBounds = new HashSet<>();

    // Ghosts
    Blinky blinky;
    Pinky pinky;
    Inky inky;
    Clyde clyde;

    // Ghost update timer
    Timer ghostTimer;

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

        // Load wall image
        URL wallResource =
                MapLoader.class.getResource(
                        "/images/pacman/wall.png"
                );

        wallImage = wallResource == null
                ? null
                : new ImageIcon(wallResource).getImage();

        // Load Ghost images
        blinkyImage = loadImage("/images/ghosts/redGhost.png");
        pinkyImage = loadImage("/images/ghosts/pinkGhost.png");
        inkyImage = loadImage("/images/ghosts/blueGhost.png");
        clydeImage = loadImage("/images/ghosts/orangeGhost.png");
        scaredGhostImage = loadImage("/images/ghosts/scaredGhost.png");

        // Build maze
        loadMap();

        // Create Ghosts
        blinky = new Blinky(288, 288);
        pinky = new Pinky(320, 288);
        inky = new Inky(352, 288);
        clyde = new Clyde(384, 288);

        // Give Ghosts their images
        blinky.setNormalImage(blinkyImage);
        pinky.setNormalImage(pinkyImage);
        inky.setNormalImage(inkyImage);
        clyde.setNormalImage(clydeImage);

        blinky.setFrightenedImage(scaredGhostImage);
        pinky.setFrightenedImage(scaredGhostImage);
        inky.setFrightenedImage(scaredGhostImage);
        clyde.setFrightenedImage(scaredGhostImage);

        // Start with different directions
        blinky.setDirection(utils.Direction.RIGHT);
        pinky.setDirection(utils.Direction.LEFT);
        inky.setDirection(utils.Direction.DOWN);
        clyde.setDirection(utils.Direction.UP);

        // Start Ghost movement
        ghostTimer = new Timer(50, e -> {

            blinky.move(wallBounds);
            pinky.move(wallBounds);
            inky.move(wallBounds);
            clyde.move(wallBounds);

            repaint();
        });

        ghostTimer.start();
    }

    private Image loadImage(String path) {

        URL resource = MapLoader.class.getResource(path);

        if (resource == null) {
            System.out.println("Image not found: " + path);
            return null;
        }

        return new ImageIcon(resource).getImage();
    }

    public void loadMap() {

        walls.clear();
        pellets.clear();
        wallBounds.clear();

        for (int r = 0; r < rowCount; r++) {

            for (int c = 0; c < columnCount; c++) {

                char tileMapChar =
                        tileMap[r].charAt(c);

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

                    // Rectangle used by Ghost collision
                    wallBounds.add(
                            new Rectangle(
                                    x,
                                    y,
                                    tileSize,
                                    tileSize
                            )
                    );

                } else if (tileMapChar == ' ') {

                    pellets.add(
                            new Pellet(
                                    x + 14,
                                    y + 14
                            )
                    );
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Draw walls
        for (Block wall : walls) {

            if (wall.image == null) {

                g.setColor(
                        new Color(30, 90, 200)
                );

                g.fillRect(
                        wall.x,
                        wall.y,
                        wall.width,
                        wall.height
                );

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

        // Draw pellets
        for (Pellet pellet : pellets) {
            pellet.draw(g);
        }

        // Draw Ghosts
        drawGhost(g, blinky);
        drawGhost(g, pinky);
        drawGhost(g, inky);
        drawGhost(g, clyde);
    }

    private void drawGhost(
            Graphics g,
            Ghost ghost
    ) {

        Image image;

        if (ghost.getMode() == ai.GhostMode.FRIGHTENED
                && ghost.getFrightenedImage() != null) {

            image = ghost.getFrightenedImage();

        } else {

            image = ghost.getNormalImage();
        }

        if (image != null) {

            g.drawImage(
                    image,
                    ghost.getX(),
                    ghost.getY(),
                    tileSize,
                    tileSize,
                    null
            );
        }
    }

    public void stopGhosts() {

        if (ghostTimer != null) {
            ghostTimer.stop();
        }
    }

    public void startGhosts() {

        if (ghostTimer != null
                && !ghostTimer.isRunning()) {

            ghostTimer.start();
        }
    }

    public static void main(String[] args) {

        JFrame frame =
                new JFrame("Pac-Man Map");

        MapLoader map =
                new MapLoader();

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