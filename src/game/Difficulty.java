package game;

public enum Difficulty {

    EASY("EASY", 2, 2, 4, 6000, false),
    NORMAL("NORMAL", 4, 4, 4, 6000, false),
    HARD("HARD", 8, 6, 8, 10000, true);

    private final String label;
    private final int ghostSpeed;
    private final int ghostScaredSpeed;
    private final int pacmanSpeed;
    private final int scareDurationMs;
    private final boolean chaseOnly;

    Difficulty(String label, int ghostSpeed, int ghostScaredSpeed, int pacmanSpeed, int scareDurationMs, boolean chaseOnly) {
        this.label = label;
        this.ghostSpeed = ghostSpeed;
        this.ghostScaredSpeed = ghostScaredSpeed;
        this.pacmanSpeed = pacmanSpeed;
        this.scareDurationMs = scareDurationMs;
        this.chaseOnly = chaseOnly;
    }

    public String getLabel() {
        return label;
    }

    public int getGhostSpeed() {
        return ghostSpeed;
    }

    public int getGhostScaredSpeed() {
        return ghostScaredSpeed;
    }

    public int getPacmanSpeed() {
        return pacmanSpeed;
    }

    public int getScareDurationMs() {
        return scareDurationMs;
    }

    public boolean isChaseOnly() {
        return chaseOnly;
    }
}
