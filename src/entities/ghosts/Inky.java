package entities.ghosts;

import ai.GhostMode;
import utils.Direction;

import java.awt.Point;

public class Inky extends Ghost {

    private Ghost blinky;

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

    // Inky's chase target depends on where Blinky is.
    public void setBlinky(Ghost blinky) {
        this.blinky = blinky;
    }

    @Override
    protected Point getTarget(
            int pacmanX,
            int pacmanY,
            Direction pacmanDirection,
            int tileSize
    ) {

        if (mode == GhostMode.CHASE && blinky != null) {

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

            int blinkyX = blinky.getX();
            int blinkyY = blinky.getY();

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