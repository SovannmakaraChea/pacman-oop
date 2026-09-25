package entities;

import utils.Direction;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;

public class PacMan {

    private int x;
    private int y;

    private final int startX;
    private final int startY;

    // 2px a tick, same as the ghosts. It divides the 32px tile evenly,
    // so Pac-Man always lands exactly on a tile centre where he can turn.
    private final int speed = 4;

    private static final int SIZE = 32;
    private static final int START_LIVES = 3;

    // direction = where he is going now; nextDirection = the key the
    // player pressed last. He turns as soon as that way is open.
    private Direction direction = Direction.LEFT;
    private Direction nextDirection = Direction.LEFT;
    private boolean moving = true;

    private int lives = START_LIVES;

    private final Image upImage = loadImage("/images/pacman/pacmanUp.png");
    private final Image downImage = loadImage("/images/pacman/pacmanDown.png");
    private final Image leftImage = loadImage("/images/pacman/pacmanLeft.png");
    private final Image rightImage = loadImage("/images/pacman/pacmanRight.png");

    public PacMan(int x, int y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
    }

    public void update(String[] tileMap, int tileSize) {

        int boardWidth = tileMap[0].length() * tileSize;
        boolean onBoard = x >= 0 && x + SIZE <= boardWidth;

        // Turning back is allowed at any time, like the arcade game.
        if (nextDirection == opposite(direction)) {
            direction = nextDirection;
            moving = true;
        }

        if (onBoard && x % tileSize == 0 && y % tileSize == 0) {

            if (canMove(nextDirection, tileMap, tileSize)) {
                direction = nextDirection;
            }

            moving = canMove(direction, tileMap, tileSize);
        }

        if (moving) {
            switch (direction) {
                case UP:
                    y -= speed;
                    break;
                case DOWN:
                    y += speed;
                    break;
                case LEFT:
                    x -= speed;
                    break;
                case RIGHT:
                    x += speed;
                    break;
            }
        }

        // Leaving through one side tunnel brings him back in the other side.
        if (x <= -SIZE) {
            x = boardWidth;
        } else if (x >= boardWidth) {
            x = -SIZE;
        }
    }

    // Is the next tile in this direction open? Only called on a tile centre.
    private boolean canMove(Direction dir, String[] tileMap, int tileSize) {

        int col = x / tileSize;
        int row = y / tileSize;

        switch (dir) {
            case UP:
                row--;
                break;
            case DOWN:
                row++;
                break;
            case LEFT:
                col--;
                break;
            case RIGHT:
                col++;
                break;
        }

        if (row < 0 || row >= tileMap.length) {
            return false;
        }

        // Off the side of the map is the tunnel, so it counts as open.
        if (col < 0 || col >= tileMap[row].length()) {
            return true;
        }

        return tileMap[row].charAt(col) != 'X';
    }

    private static Direction opposite(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            default:
                return Direction.LEFT;
        }
    }

    public void draw(Graphics g) {

        Image image;

        switch (direction) {
            case UP:
                image = upImage;
                break;
            case DOWN:
                image = downImage;
                break;
            case RIGHT:
                image = rightImage;
                break;
            default:
                image = leftImage;
        }

        if (image != null) {
            g.drawImage(image, x, y, SIZE, SIZE, null);
            return;
        }

        // Fallback if the image can't be found: a yellow circle with a mouth
        int startAngle;
        switch (direction) {
            case UP:
                startAngle = 135;
                break;
            case DOWN:
                startAngle = 315;
                break;
            case RIGHT:
                startAngle = 45;
                break;
            default:
                startAngle = 225;
        }
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, SIZE, SIZE, startAngle, 270);
    }

    private static Image loadImage(String path) {
        URL resource = PacMan.class.getResource(path);
        return resource == null ? null : new ImageIcon(resource).getImage();
    }

    // Back to the start tile after losing a life.
    public void reset() {
        x = startX;
        y = startY;
        direction = Direction.LEFT;
        nextDirection = Direction.LEFT;
        moving = true;
    }

    // New game: start tile and full lives.
    public void resetAll() {
        reset();
        lives = START_LIVES;
    }

    public void loseLife() {
        lives--;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLives() {
        return lives;
    }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }
}
