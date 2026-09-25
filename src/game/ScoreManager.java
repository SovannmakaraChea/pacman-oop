package game;

public class ScoreManager {

    public static final int FRUIT_POINTS = 50;
    private static final int FIRST_GHOST_POINTS = 200;

    private int score = 0;

    // Best score since the game was opened (not saved to a file).
    private int highScore = 0;

    public void addPoints(int points) {
        score += points;

        if (score > highScore) {
            highScore = score;
        }
    }

    // 200, 400, 800, 1600 for the 1st, 2nd, 3rd, 4th ghost eaten in one fright.
    public void addGhostPoints(int ghostsEatenThisFright) {
        addPoints(FIRST_GHOST_POINTS << (ghostsEatenThisFright - 1));
    }

    // New game: score goes back to 0, the high score stays.
    public void reset() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }
}
