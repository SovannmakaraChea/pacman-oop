package map;

import entities.Fruit;
import entities.PacMan;
import entities.Pellet;
import entities.ghosts.Blinky;
import entities.ghosts.Clyde;
import entities.ghosts.Ghost;
import entities.ghosts.Inky;
import entities.ghosts.Pinky;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import ai.GhostMode;
import audio.SoundManager;
import audio.SoundManager.Sound;
import game.ScoreManager;
import input.KeyHandler;
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

    Image blinkyImage;
    Image pinkyImage;
    Image inkyImage;
    Image clydeImage;
    Image scaredGhostImage;

    HashSet<Block> walls = new HashSet<>();
    HashSet<Pellet> pellets = new HashSet<>();
    HashSet<Rectangle> wallBounds = new HashSet<>();
    List<Fruit> fruits;

    Blinky blinky;
    Pinky pinky;
    Inky inky;
    Clyde clyde;

    static final int TICK_MS = 16;
    Timer gameTimer;

    static final int SCATTER_TICKS = 7000 / TICK_MS;
    static final int CHASE_TICKS = 10000 / TICK_MS;
    GhostMode waveMode = GhostMode.SCATTER;
    int waveTicks = 0;

    PacMan pacman;

    ScoreManager scoreManager = new ScoreManager();
    SoundManager sounds = new SoundManager();
    boolean gameOver = false;
    boolean won = false;

    boolean waitingToStart = true;

    static final int READY_TICKS = 2000 / TICK_MS;
    int readyTicks = READY_TICKS;

    int frightenedTicks = 0;
    int ghostsEatenThisFright = 0;

    static final int HUD_HEIGHT = 32;

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
                new Dimension(boardWidth, boardHeight + HUD_HEIGHT)
        );

        setBackground(Color.BLACK);

        setFocusable(true);

        URL wallResource =
                MapLoader.class.getResource(
                        "/images/pacman/wall.png"
                );

        wallImage = wallResource == null
                ? null
                : new ImageIcon(wallResource).getImage();

        blinkyImage = loadImage("/images/ghosts/redGhost.png");
        pinkyImage = loadImage("/images/ghosts/pinkGhost.png");
        inkyImage = loadImage("/images/ghosts/blueGhost.png");
        clydeImage = loadImage("/images/ghosts/orangeGhost.png");
        scaredGhostImage = loadImage("/images/ghosts/scaredGhost.png");

        loadMap();

        blinky = new Blinky(288, 288);
        pinky = new Pinky(320, 288);
        inky = new Inky(352, 288);
        clyde = new Clyde(384, 288);

        inky.setBlinky(blinky);

        blinky.setNormalImage(blinkyImage);
        pinky.setNormalImage(pinkyImage);
        inky.setNormalImage(inkyImage);
        clyde.setNormalImage(clydeImage);

        blinky.setFrightenedImage(scaredGhostImage);
        pinky.setFrightenedImage(scaredGhostImage);
        inky.setFrightenedImage(scaredGhostImage);
        clyde.setFrightenedImage(scaredGhostImage);

        blinky.setDirection(Direction.RIGHT);
        pinky.setDirection(Direction.LEFT);
        inky.setDirection(Direction.DOWN);
        clyde.setDirection(Direction.UP);

        for (Ghost ghost : ghosts()) {
            ghost.setMode(waveMode);
        }

        pacman = new PacMan(9 * tileSize, 15 * tileSize);
        addKeyListener(new KeyHandler(pacman, this::startPlaying, this::restart));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPlaying();
            }
        });

        gameTimer = new Timer(TICK_MS, e -> {
            updateGame();
            repaint();
        });
    }

    void updateGame() {

        if (gameOver || won || waitingToStart) {
            return;
        }

        if (readyTicks > 0) {
            readyTicks--;
            return;
        }

        pacman.update(tileMap, tileSize);
        eatPellets();
        eatFruit();

        if (frightenedTicks > 0) {
            updateFrightened();
        } else {
            updateWave();
        }

        for (Ghost ghost : ghosts()) {
            ghost.updateAI(
                    tileMap,
                    tileSize,
                    pacman.getX(),
                    pacman.getY(),
                    pacman.getDirection(),
                    wallBounds
            );
        }

        checkGhostCollisions();

        if (pellets.isEmpty() && allFruitEaten()) {
            won = true;
            sounds.stopAll();
            sounds.play(Sound.WIN);
        }
    }

    void eatPellets() {

        Rectangle pacmanBounds = pacman.getBounds();
        Iterator<Pellet> it = pellets.iterator();

        while (it.hasNext()) {
            Pellet pellet = it.next();
            if (pacmanBounds.intersects(pellet.getBounds())) {
                scoreManager.addPoints(pellet.getScoreValue());
                it.remove();
                sounds.playIfIdle(Sound.CHOMP);
            }
        }
    }

    void eatFruit() {

        for (Fruit fruit : fruits) {
            if (fruit.checkCollision(pacman.getBounds())) {
                scoreManager.addPoints(ScoreManager.FRUIT_POINTS);
                frightenGhosts(fruit.getScareDurationMs());
                sounds.play(Sound.FRUIT);
            }
        }
    }

    boolean allFruitEaten() {
        for (Fruit fruit : fruits) {
            if (!fruit.isEaten()) {
                return false;
            }
        }
        return true;
    }

    void frightenGhosts(int durationMs) {

        frightenedTicks = durationMs / TICK_MS;
        ghostsEatenThisFright = 0;

        for (Ghost ghost : ghosts()) {
            if (ghost.getMode() != GhostMode.DEAD) {
                ghost.setMode(GhostMode.FRIGHTENED);
            }
        }
    }

    void updateFrightened() {

        frightenedTicks--;

        if (frightenedTicks > 0) {
            return;
        }

        for (Ghost ghost : ghosts()) {
            if (ghost.getMode() == GhostMode.FRIGHTENED) {
                ghost.setMode(waveMode);
            }
        }
    }

    void checkGhostCollisions() {

        int half = tileSize / 2;

        for (Ghost ghost : ghosts()) {

            boolean touching =
                    Math.abs(ghost.getX() - pacman.getX()) < half
                    && Math.abs(ghost.getY() - pacman.getY()) < half;

            if (!touching) {
                continue;
            }

            if (ghost.getMode() == GhostMode.FRIGHTENED) {

                ghostsEatenThisFright++;
                scoreManager.addGhostPoints(ghostsEatenThisFright);
                sounds.play(Sound.EAT_GHOST);

                ghost.reset();
                ghost.setMode(waveMode);

            } else if (ghost.getMode() != GhostMode.DEAD) {

                pacman.loseLife();
                sounds.stopAll();
                sounds.play(Sound.DEATH);

                if (pacman.getLives() <= 0) {
                    gameOver = true;
                } else {
                    resetPositions();
                }
                return;
            }
        }
    }

    void resetPositions() {

        pacman.reset();

        for (Ghost ghost : ghosts()) {
            ghost.reset();
            ghost.setMode(waveMode);
        }

        frightenedTicks = 0;
        readyTicks = READY_TICKS;
    }

    void restart() {

        if (!gameOver && !won) {
            return;
        }

        scoreManager.reset();
        gameOver = false;
        won = false;
        waveMode = GhostMode.SCATTER;
        waveTicks = 0;

        sounds.stopAll();
        loadMap();
        pacman.resetAll();
        resetPositions();
        waitingToStart = true;
    }

    void startPlaying() {

        requestFocusInWindow();

        waitingToStart = false;
        readyTicks = 0;
    }

    Ghost[] ghosts() {
        return new Ghost[]{blinky, pinky, inky, clyde};
    }

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

        for (Pellet pellet : pellets) {
            pellet.draw(g);
        }

        for (Fruit fruit : fruits) {
            fruit.draw(g);
        }

        drawGhost(g, blinky);
        drawGhost(g, pinky);
        drawGhost(g, inky);
        drawGhost(g, clyde);

        pacman.draw(g);

        drawHud(g);
    }

    private void drawHud(Graphics g) {

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + scoreManager.getScore(), 10, boardHeight + 22);
        drawCentered(g, "HIGH: " + scoreManager.getHighScore(), boardHeight + 22);

        g.setColor(Color.YELLOW);
        for (int i = 0; i < pacman.getLives(); i++) {
            g.fillArc(boardWidth - 30 - i * 26, boardHeight + 6, 20, 20, 225, 270);
        }

        String message = null;
        Color color = Color.YELLOW;

        if (gameOver) {
            message = "GAME OVER";
            color = Color.RED;
        } else if (won) {
            message = "YOU WIN!";
        } else if (readyTicks > 0) {
            message = "READY!";
        }

        if (message == null) {
            return;
        }

        int textY = 11 * tileSize + 24;

        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(color);
        drawCentered(g, message, textY);

        if (gameOver || won) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            drawCentered(g, "Press ENTER to play again", 13 * tileSize + 22);
        } else if (waitingToStart) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            drawCentered(g, "Press any key or click to start", 13 * tileSize + 22);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            drawCentered(g, "Press any key or click to go now", 13 * tileSize + 22);
        }
    }

    private void drawCentered(Graphics g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (boardWidth - width) / 2, y);
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

        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void playMenuMusic() {
        sounds.play(Sound.START);
    }

    public void startGhosts() {

        if (gameTimer != null
                && !gameTimer.isRunning()) {

            gameTimer.start();
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

        map.startGhosts();
        map.requestFocusInWindow();
    }
}