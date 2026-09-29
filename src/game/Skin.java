package game;

import java.awt.Color;

public enum Skin {

    YELLOW("YELLOW", "pacman", new Color(255, 255, 0)),
    RED("RED", "pacmanRed", new Color(238, 40, 36)),
    ORANGE("ORANGE", "pacmanOrange", new Color(252, 183, 74));

    private final String label;
    private final String imagePrefix;
    private final Color color;

    Skin(String label, String imagePrefix, Color color) {
        this.label = label;
        this.imagePrefix = imagePrefix;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    // direction is "Up", "Down", "Left" or "Right"
    public String imagePath(String direction) {
        return "/images/pacman/" + imagePrefix + direction + ".png";
    }
}
