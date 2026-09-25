package map;

import entities.Fruit;
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
import java.util.List;
import ai.GhostMode;
import utils.Direction;

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
    List<Fruit> fruits;

    // Ghosts
    Blinky blinky;
    Pinky pinky;
    Inky inky;
    Clyde clyde;

    // Ghost update timer. 16ms is about 60 frames a second; with a ghost
    // speed of 2px that is 125px (about 4 tiles) a second.
    static final int TICK_MS = 16;
    Timer ghostTimer;

    // Ghosts take turns: scatter to their own corners (two at the top,
    // two at the bottom), then chase Pac-Man, then repeat.
    static final int SCATTER_TICKS = 7000 / TICK_MS;
    static final int CHASE_TICKS = 10000 / TICK_MS;
    GhostMode waveMode = GhostMode.SCATTER;
    int waveTicks = 0;

    // Where the ghosts chase. There is no Pac-Man yet, so this starts
    // on his spawn tile; Pac-Man's code should call setPacman() each move.
    int pacmanX = 9 * tileSize;
    int pacmanY = 15 * tileSize;
    Direction pacmanDirection = Direction.LEFT;

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

        inky.setBlinky(blinky);

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
        blinky.setDirection(Direction.RIGHT);
        pinky.setDirection(Direction.LEFT);
        inky.setDirection(Direction.DOWN);
        clyde.setDirection(Direction.UP);

        for (Ghost ghost : ghosts()) {
            ghost.setMode(waveMode);
        }

        // Start Ghost movement
        ghostTimer = new Timer(TICK_MS, e -> {

            updateWave();

            for (Ghost ghost : ghosts()) {
                ghost.updateAI(
                        tileMap,
                        tileSize,
                        pacmanX,
                        pacmanY,
                        pacmanDirection,
                        wallBounds
                );
            }

            repaint();
        });

        ghostTimer.start();
    }

    Ghost[] ghosts() {
        return new Ghost[]{blinky, pinky, inky, clyde};
    }

    // Switch every ghost between SCATTER and CHASE when the wave runs out.
    // Frightened or dead ghosts are left alone.
    void updateWave() {

        waveTicks++;

        int waveLength = waveMode == GhostMode.SCATTER
                ? SCATTER_TICKS
                : CHASE_TICKS;

        if (waveTicks < waveLength) {
            return;
        }

        waveTicks = 0;
        waveMode = waveMode == GhostMode.SCATTER
                ? GhostMode.CHASE
                : GhostMode.SCATTER;

        for (Ghost ghost : ghosts()) {
            if (ghost.getMode() == GhostMode.SCATTER
                    || ghost.getMode() == GhostMode.CHASE) {
                ghost.setMode(waveMode);
            }
        }
    }

    public void setPacman(int x, int y, Direction direction) {
        pacmanX = x;
        pacmanY = y;
        pacmanDirection = direction;
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
        fruits = Fruit.createCornerCherries(tileSize);

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

                } else if (tileMapChar == ' ' && !hasFruitAt(r, c)) {

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

    private boolean hasFruitAt(int row, int col) {
        for (Fruit fruit : fruits) {
            if (fruit.getTileX() == col && fruit.getTileY() == row) {
                return true;
            }
        }
        return false;
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

        // Draw fruit
        for (Fruit fruit : fruits) {
            fruit.draw(g);
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