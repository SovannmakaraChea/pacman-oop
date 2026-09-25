import ai.Pathfinder;
import utils.Direction;

public class PathfinderTest {

    public static void main(String[] args) {

        String[] tileMap = {
                "XXXXXXXXXXXXXXXXXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X       X    X",
                "XXXX XXXX XXXX XXXX",
                "OOOX X       X XOOO",
                "XXXX X XX XX X XXXX",
                "O                 O",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X  X           X  X",
                "XX X X XXXXX X X XX",
                "X    X   X   X    X",
                "X XXXXXX X XXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        Pathfinder pathfinder = new Pathfinder(tileMap, 32);

        Direction direction = pathfinder.findDirection(
                64, 96,
                544, 96
        );

        System.out.println("Next direction: " + direction);
    }
}