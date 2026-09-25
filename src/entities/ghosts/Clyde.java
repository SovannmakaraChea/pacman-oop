package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Point;

public class Clyde extends Ghost {

    public Clyde(int x, int y) {

        super(
                x,
                y,
                2,
                "Clyde",
                1,
                19
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

            double distance =
                    Math.hypot(
                            pacmanX - x,
                            pacmanY - y
                    );

            if (distance > 8 * tileSize) {
                return new Point(pacmanX, pacmanY);
            }

            // Too close, scatter to corner.
            return new Point(
                    scatterTarget.x * tileSize,
                    scatterTarget.y * tileSize
            );
        }

        return super.getTarget(
                pacmanX,
                pacmanY,
                pacmanDirection,
                tileSize
        );
    }
}