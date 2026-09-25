package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Point;

public class Blinky extends Ghost {

    public Blinky(int x, int y) {

        super(
                x,
                y,
                2,
                "Blinky",
                17,
                1
        );

        setMode(GhostMode.CHASE);
    }

    @Override
    protected Point getTarget(
            int pacmanX,
            int pacmanY,
            Direction pacmanDirection,
            int tileSize
    ) {

        if (mode == GhostMode.CHASE) {
            return new Point(pacmanX, pacmanY);
        }

        return super.getTarget(
                pacmanX,
                pacmanY,
                pacmanDirection,
                tileSize
        );
    }
}