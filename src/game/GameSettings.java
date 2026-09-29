package game;

public class GameSettings {

    private Difficulty difficulty = Difficulty.NORMAL;
    private Skin skin = Skin.YELLOW;

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
