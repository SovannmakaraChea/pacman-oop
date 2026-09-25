package entities.ghosts;

import ai.GhostMode;

public class Blinky extends Ghost {

    public Blinky(int x, int y) {
        super(x, y, 2, "Blinky");
        setMode(GhostMode.CHASE);
    }
}