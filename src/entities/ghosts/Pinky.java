package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Point;

public class Pinky extends Ghost {

    public Pinky(int x, int y) {

        super(
                x,
                y,
                4,
                "Pinky",
                1,
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

            int targetX = pacmanX;
            int targetY = pacmanY;

            switch (pacmanDirection) {

                case UP:
                    targetY -= 4 * tileSize;
                    break;

                case DOWN:
                    targetY += 4 * tileSize;
                    break;

                case LEFT:
                    targetX -= 4 * tileSize;
                    break;

                case RIGHT:
                    targetX += 4 * tileSize;
                    break;
            }

            return new Point(targetX, targetY);
        }

        return super.getTarget(
                pacmanX,
                pacmanY,
                pacmanDirection,
                tileSize
        );
    }
}