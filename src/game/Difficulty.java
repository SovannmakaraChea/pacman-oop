package game;

public enum Difficulty {

    EASY("EASY", 2, 4, 15000, false),
    NORMAL("NORMAL", 4, 4, 15000, false),
    HARD("HARD", 8, 6, 5000, true);

    private final String label;
    private final int ghostSpeed;
    private final int pacmanSpeed;
    private final int scareDurationMs;
    private final boolean chaseOnly;

    Difficulty(String label, int ghostSpeed, int pacmanSpeed, int scareDurationMs, boolean chaseOnly) {
        this.label = label;
        this.ghostSpeed = ghostSpeed;
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
