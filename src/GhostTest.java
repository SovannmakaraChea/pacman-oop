import entities.ghosts.Blinky;
import entities.ghosts.Clyde;
import entities.ghosts.Ghost;
import entities.ghosts.Inky;
import entities.ghosts.Pinky;

public class GhostTest {

    public static void main(String[] args) {

        Ghost[] ghosts = {
                new Blinky(100, 100),
                new Pinky(120, 100),
                new Inky(140, 100),
                new Clyde(160, 100)
        };

        for (Ghost ghost : ghosts) {

            System.out.println(
                    ghost.getClass().getSimpleName()
                    + " | Mode = "
                    + ghost.getMode()
            );
        }
    }
}