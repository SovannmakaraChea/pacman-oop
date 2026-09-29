package entities.ghosts;

import ai.GhostMode;
import ai.Pathfinder;
import utils.Direction;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Ghost {

    protected int x;
    protected int y;

    protected final int startX;
    protected final int startY;

    protected int speed;
    protected int frightenedSpeed;

    protected final String name;

    protected Direction direction;
    protected GhostMode mode;

    protected Image normalImage;
    protected Image frightenedImage;

    protected final Point scatterTarget;

    private final Random random = new Random();

    private static final int SIZE = 32;

    private Pathfinder pathfinder;

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
        this.frightenedSpeed = speed;
        this.name = name;

        this.direction = Direction.UP;
        this.mode = GhostMode.SCATTER;

        this.scatterTarget =
                new Point(scatterTileX, scatterTileY);
    }

    public void move() {

        int step = Math.min(currentSpeed(), distanceToNextTile());

        switch (direction) {

            case UP:
                y -= step;
                break;

            case DOWN:
                y += step;
                break;

            case LEFT:
                x -= step;
                break;

            case RIGHT:
                x += step;
                break;
        }
    }

    private int currentSpeed() {
        return mode == GhostMode.FRIGHTENED ? frightenedSpeed : speed;
    }

    // The speed can change mid-tile (e.g. 8 -> 6 when scared on HARD), so the last
    // step before a tile edge is shortened to land exactly on it. Otherwise
    // the ghost drifts off the grid and can never turn again.
    // A ghost is one tile big, so SIZE is also the tile size.
    private int distanceToNextTile() {

        int distance;

        switch (direction) {
            case UP:
                distance = Math.floorMod(y, SIZE);
                break;
            case DOWN:
                distance = SIZE - Math.floorMod(y, SIZE);
                break;
            case LEFT:
                distance = Math.floorMod(x, SIZE);
                break;
            default:
                distance = SIZE - Math.floorMod(x, SIZE);
        }

        return distance == 0 ? SIZE : distance;
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

        int boardWidth = tileMap[0].length() * tileSize;

        boolean onBoard = x >= 0 && x + SIZE <= boardWidth;

        if (onBoard && isCentered(tileSize)) {

            if (mode == GhostMode.FRIGHTENED) {

                chooseRandomDirection(walls);

            } else {

                Point target = getTarget(
                        pacmanX,
                        pacmanY,
                        pacmanDirection,
                        tileSize
                );

                if (pathfinder == null) {
                    pathfinder = new Pathfinder(tileMap, tileSize);
                }

                Direction nextDirection =
                        pathfinder.findDirection(
                                x,
                                y,
                                target.x,
                                target.y,
                                opposite(direction)
                        );

                if (nextDirection != null) {
                    direction = nextDirection;
                } else {
                    chooseRandomDirection(walls);
                }
            }
        }

        move(walls);

        wrapThroughTunnel(boardWidth);
    }

    private void wrapThroughTunnel(int boardWidth) {

        if (x <= -SIZE) {
            x = boardWidth;
        } else if (x >= boardWidth) {
            x = -SIZE;
        }
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

        List<Direction> directions =
                new ArrayList<>(Arrays.asList(Direction.values()));

        Collections.shuffle(directions, random);

        Direction reverse = opposite(direction);

        if (directions.remove(reverse)) {
            directions.add(reverse);
        }

        for (Direction candidate : directions) {

            int testX = x;
            int testY = y;

            switch (candidate) {

                case UP:
                    testY -= currentSpeed();
                    break;

                case DOWN:
                    testY += currentSpeed();
                    break;

                case LEFT:
                    testX -= currentSpeed();
                    break;

                case RIGHT:
                    testX += currentSpeed();
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

    private static Direction opposite(Direction direction) {

        if (direction == null) {
            return null;
        }

        switch (direction) {
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFrightenedSpeed(int frightenedSpeed) {
        this.frightenedSpeed = frightenedSpeed;
    }

    // The pathfinder remembers the map it was built for, so a new level
    // needs a fresh one.
    public void resetPathfinder() {
        pathfinder = null;
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