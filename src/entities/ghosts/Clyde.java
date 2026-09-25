package entities.ghosts;

import ai.GhostMode;

public class Clyde extends Ghost {

    public Clyde(int x, int y) {
        super(x, y, 2, "Clyde");
        setMode(GhostMode.SCATTER);
    }
}