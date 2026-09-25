package entities.ghosts;

import ai.GhostMode;
import ai.Pathfinder;
import utils.Direction;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Set;

public abstract class Ghost {

    protected int x;
    protected int y;

    protected final int startX;
    protected final int startY;

    protected int speed;

    protected final String name;

    protected Direction direction;
    protected GhostMode mode;

    protected Image normalImage;
    protected Image frightenedImage;

    protected final Point scatterTarget;

    private final Random random = new Random();

    private static final int SIZE = 20;

    protected Ghost(
            int x,
            int y,
            int speed,
            String name,
            int scatterTileX,
            int scatterTileY
    ) {

        this.x = x;
        this.y = y;

        this.startX = x;
        this.startY = y;

        this.speed = speed;
        this.name = name;

        this.direction = Direction.UP;
        this.mode = GhostMode.SCATTER;

        this.scatterTarget =
                new Point(scatterTileX, scatterTileY);
    }

    public void move() {

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

    public void move(Set<Rectangle> walls) {

        int oldX = x;
        int oldY = y;

        move();

        for (Rectangle wall : walls) {

            if (getBounds().intersects(wall)) {

                x = oldX;
                y = oldY;

                chooseRandomDirection(walls);
                break;
            }
        }
    }

    public void updateAI(
            String[] tileMap,
            int tileSize,
            int pacmanX,
            int pacmanY,
            Direction pacmanDirection,
            Set<Rectangle> walls
    ) {

        if (isCentered(tileSize)) {

            if (mode == GhostMode.FRIGHTENED) {

                chooseRandomDirection(walls);

            } else {

                Point target = getTarget(
                        pacmanX,
                        pacmanY,
                        pacmanDirection,
                        tileSize
                );

                Pathfinder pathfinder =
                        new Pathfinder(tileMap, tileSize);

                Direction nextDirection =
                        pathfinder.findDirection(
                                x,
                                y,
                                target.x,
                                target.y
                        );

                if (nextDirection != null) {
                    direction = nextDirection;
                }
            }
        }

        move(walls);
    }

    protected Point getTarget(
            int pacmanX,
            int pacmanY,
            Direction pacmanDirection,
            int tileSize
    ) {

        switch (mode) {

            case SCATTER:
                return new Point(
                        scatterTarget.x * tileSize,
                        scatterTarget.y * tileSize
                );

            case DEAD:
                return new Point(startX, startY);

            case CHASE:
                return new Point(pacmanX, pacmanY);

            case FRIGHTENED:
            default:
                return new Point(x, y);
        }
    }

    private boolean isCentered(int tileSize) {

        return x % tileSize == 0
                && y % tileSize == 0;
    }

    private void chooseRandomDirection(
            Set<Rectangle> walls
    ) {

        Direction[] directions = Direction.values();

        for (int attempt = 0; attempt < 10; attempt++) {

            Direction candidate =
                    directions[random.nextInt(directions.length)];

            int testX = x;
            int testY = y;

            switch (candidate) {

                case UP:
                    testY -= speed;
                    break;

                case DOWN:
                    testY += speed;
                    break;

                case LEFT:
                    testX -= speed;
                    break;

                case RIGHT:
                    testX += speed;
                    break;
            }

            Rectangle testBounds =
                    new Rectangle(
                            testX,
                            testY,
                            SIZE,
                            SIZE
                    );

            boolean blocked = false;

            for (Rectangle wall : walls) {

                if (testBounds.intersects(wall)) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {

                direction = candidate;
                return;
            }
        }
    }

    public void reset() {

        x = startX;
        y = startY;
        direction = Direction.UP;
        mode = GhostMode.SCATTER;
    }

    public Rectangle getBounds() {

        return new Rectangle(
                x,
                y,
                SIZE,
                SIZE
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public GhostMode getMode() {
        return mode;
    }

    public void setMode(GhostMode mode) {
        this.mode = mode;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Image getNormalImage() {
        return normalImage;
    }

    public Image getFrightenedImage() {
        return frightenedImage;
    }

    public void setNormalImage(Image normalImage) {
        this.normalImage = normalImage;
    }

    public void setFrightenedImage(Image frightenedImage) {
        this.frightenedImage = frightenedImage;
    }
}