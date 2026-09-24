package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Fruit {
    private int x;
    private int y;
    private int tileX;
    private int tileY;
    private int tileSize;
    private boolean isEaten;
    private static final int SCARE_DURATION_MS = 15000;
    private static final Image CHERRY_IMAGE = loadCherryImage();

    public Fruit(int tileX, int tileY, int tileSize) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tileSize;
        this.isEaten = false;
        this.x = tileX * tileSize;
        this.y = tileY * tileSize;
    }

    public static List<Fruit> createCornerCherries(int tileSize) {
        List<Fruit> cherries = new ArrayList<>();
        // Map is 19 x 21 tiles, so these are the open tiles in each corner
        cherries.add(new Fruit(1, 1, tileSize));   // Top-Left
        cherries.add(new Fruit(17, 1, tileSize));  // Top-Right
        cherries.add(new Fruit(1, 19, tileSize));  // Bottom-Left
        cherries.add(new Fruit(17, 19, tileSize)); // Bottom-Right
        return cherries;
    }

    private static Image loadCherryImage() {
        URL cherryResource = Fruit.class.getResource("/images/items/cherry.png");
        return cherryResource == null ? null : new ImageIcon(cherryResource).getImage();
    }

    public void draw(Graphics g) {
        if (isEaten) return;

        if (CHERRY_IMAGE != null) {
            g.drawImage(CHERRY_IMAGE, x, y, tileSize, tileSize, null);
            return;
        }

        // Fallback if cherry.png can't be found: draw the cherry by hand

        int centerX = x + tileSize / 2;
        int centerY = y + tileSize / 2;
        int radius = tileSize / 4;

        g.setColor(new Color(34, 139, 34));
        g.drawLine(centerX - 3, centerY, centerX + 2, centerY - 6);
        g.drawLine(centerX + 3, centerY, centerX + 2, centerY - 6);

        g.setColor(Color.RED);
        g.fillOval(centerX - radius - 1, centerY - 2, radius * 2, radius * 2);
        g.fillOval(centerX + 1, centerY - 2, radius * 2, radius * 2);

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

    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
    public boolean isEaten() { return isEaten; }
    public int getScareDurationMs() { return SCARE_DURATION_MS; }
}