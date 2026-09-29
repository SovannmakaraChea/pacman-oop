package game;

public class ScoreManager {

    public static final int FRUIT_POINTS = 200;
    private static final int FIRST_GHOST_POINTS = 200;

    private int score = 0;

    private int highScore = 0;

    public void addPoints(int points) {
        score += points;

        if (score > highScore) {
            highScore = score;
        }
    }

    public void addGhostPoints(int ghostsEatenThisFright) {
        addPoints(FIRST_GHOST_POINTS << (ghostsEatenThisFright - 1));
    }

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
