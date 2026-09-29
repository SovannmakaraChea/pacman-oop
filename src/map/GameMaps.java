package map;

// Every map is 19 x 21 and keeps the same open tiles for the ghost spawn
// (row 9, columns 9-12), Pac-Man's start (row 15, column 9) and the four
// corner cherries, so the positions set up in MapLoader work on all of them.
public final class GameMaps {

    public static final int LEVEL_COUNT = 10;

    private static final String[][] MAPS = {
        {
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
        },
        {
            "XXXXXXXXXXXXXXXXXXX",
            "X    X       X    X",
            "X XX X XX XX X XX X",
            "X                 X",
            "X XX XXX X XXX XX X",
            "X  X   X   X   X  X",
            "XX X X X X X X X XX",
            "X    X       X    X",
            "X XXXX XX XX XXXX X",
            "O                 O",
            "X XXXX XXXXX XXXX X",
            "X    X       X    X",
            "XX X X XX XX X X XX",
            "X  X           X  X",
            "X XXXX XXXXX XXXX X",
            "X                 X",
            "X XX X XX XX X XX X",
            "X  X X  X X  X X  X",
            "X  X XX X X XX X  X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
        },
        {
            "XXXXXXXXXXXXXXXXXXX",
            "X                 X",
            "X X XXXXX XXXXX X X",
            "X X     X X     X X",
            "X XXX X X X X XXX X",
            "X     X     X     X",
            "XXX X XXX XXX X XXX",
            "X   X         X   X",
            "X X X XXX XXX X X X",
            "O                 O",
            "X XXX XXXXXXX XXX X",
            "X   X         X   X",
            "X X X XXX XXX X X X",
            "X X             X X",
            "X X XXX X X XXX X X",
            "X                 X",
            "XXX X XXX XXX X XXX",
            "X   X   X X   X   X",
            "X XXXXX X X XXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
        }
    };

    private GameMaps() {
    }

    // Only levels that have a map can be played; the rest stay locked.
    public static boolean isUnlocked(int level) {
        return level >= 1 && level <= MAPS.length;
    }

    public static String[] forLevel(int level) {
        return MAPS[level - 1];
    }
}
