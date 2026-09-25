package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Rectangle;
import java.util.Random;
import java.util.Set;

public class Ghost {

    protected int x;
    protected int y;

    protected int startX;
    protected int startY;

    protected int speed;

    protected String name;

    protected Direction direction;

    protected GhostMode mode;

    private final Random random = new Random();

    private static final int SIZE = 20;

    public Ghost(int x, int y, int speed, String name) {

        this.x = x;
        this.y = y;

        this.startX = x;
        this.startY = y;

        this.speed = speed;
        this.name = name;

        this.direction = Direction.UP;
        this.mode = GhostMode.SCATTER;
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

    public void reset() {

        x = startX;
        y = startY;

        direction = Direction.UP;
        mode = GhostMode.SCATTER;
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

    protected Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void randomDirection() {

        Direction[] directions = Direction.values();

        direction = directions[random.nextInt(directions.length)];
    }

    public GhostMode getMode() {
        return mode;
    }

    public void setMode(GhostMode mode) {
        this.mode = mode;
    }

    public void move(Set<Rectangle> walls) {

        int oldX = x;
        int oldY = y;

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

        for (Rectangle wall : walls) {

            if (getBounds().intersects(wall)) {

                x = oldX;
                y = oldY;

                randomDirection();
                break;
            }
        }
    }
}