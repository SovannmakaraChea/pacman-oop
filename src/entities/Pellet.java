package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pellet {


    private int x;
    private int y;

    // Pellet properties
    private final int width = 4;
    private final int height = 4;
    private final int scoreValue = 10;
    private boolean isEaten = false;


    public Pellet(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void draw(Graphics g) {
        if (!isEaten) {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);
        }
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        this.isEaten = eaten;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}