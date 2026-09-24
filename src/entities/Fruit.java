package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Fruit extends Entity {
    private int tileX;
    private int tileY;
    private int tileSize;
    private boolean isEaten;
    private static final int SCARE_DURATION_MS = 15000; // 15 seconds

    public Fruit(int tileX, int tileY, int tileSize) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.isEaten = false;

        this.x = tileX * tileSize;
        this.y = tileY * tileSize;
    }

    /**
     * Creates cherries at the 4 corner locations marked in your maze.
     */
    public static List<Fruit> createCornerCherries(int tileSize) {
        List<Fruit> cherries = new ArrayList<>();

        cherries.add(new Fruit(1, 3, tileSize));   // Top-Left
        cherries.add(new Fruit(26, 3, tileSize));  // Top-Right
        cherries.add(new Fruit(1, 23, tileSize));  // Bottom-Left
        cherries.add(new Fruit(26, 23, tileSize)); // Bottom-Right

        return cherries;
    }

    public void draw(Graphics2D g) {
        if (isEaten) return;

        int centerX = x + tileSize / 2;
        int centerY = y + tileSize / 2;
        int radius = tileSize / 4;

        // Draw stems
        g.setColor(new Color(34, 139, 34));
        g.drawLine(centerX - 3, centerY, centerX + 2, centerY - 6);
        g.drawLine(centerX + 3, centerY, centerX + 2, centerY - 6);

        // Draw cherries
        g.setColor(Color.RED);
        g.fillOval(centerX - radius - 1, centerY - 2, radius * 2, radius * 2);
        g.fillOval(centerX + 1, centerY - 2, radius * 2, radius * 2);

        // Draw highlight
        g.setColor(Color.WHITE);
        g.fillOval(centerX - radius + 1, centerY, 3, 3);
        g.fillOval(centerX + 3, centerY, 3, 3);
    }

    public boolean checkCollision(Rectangle pacmanBounds) {
        if (isEaten) return false;

        Rectangle fruitBounds = new Rectangle(x, y, tileSize, tileSize);
        if (pacmanBounds.intersects(fruitBounds)) {
            isEaten = true;
            return true;
        }
        return false;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public int getScareDurationMs() {
        return SCARE_DURATION_MS;
    }
}