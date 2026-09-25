package entities.ghosts;

import ai.GhostMode;

public class Pinky extends Ghost {

    public Pinky(int x, int y) {
        super(x, y, 2, "Pinky");
        setMode(GhostMode.CHASE);
    }
}