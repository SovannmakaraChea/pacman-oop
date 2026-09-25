package entities.ghosts;

import ai.GhostMode;

public class Inky extends Ghost {

    public Inky(int x, int y) {
        super(x, y, 2, "Inky");
        setMode(GhostMode.SCATTER);
    }
}