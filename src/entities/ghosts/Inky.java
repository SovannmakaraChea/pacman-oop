package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Point;

public class Inky extends Ghost {

    public Inky(int x, int y) {

        super(
                x,
                y,
                2,
                "Inky",
                17,
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

            int aheadX = pacmanX;
            int aheadY = pacmanY;

            switch (pacmanDirection) {

                case UP:
                    aheadY -= 2 * tileSize;
                    break;

                case DOWN:
                    aheadY += 2 * tileSize;
                    break;

                case LEFT:
                    aheadX -= 2 * tileSize;
                    break;

                case RIGHT:
                    aheadX += 2 * tileSize;
                    break;
            }

            // Use Blinky's position as the second point.
            int blinkyX = 288;
            int blinkyY = 288;

            int vectorX = aheadX - blinkyX;
            int vectorY = aheadY - blinkyY;

            return new Point(
                    aheadX + vectorX,
                    aheadY + vectorY
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