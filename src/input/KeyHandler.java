package input;

import entities.PacMan;
import utils.Direction;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    private final PacMan pacman;
    private final Runnable onStart;
    private final Runnable onRestart;
    private final Runnable onPause;

    public KeyHandler(PacMan pacman, Runnable onStart, Runnable onRestart, Runnable onPause) {
        this.pacman = pacman;
        this.onStart = onStart;
        this.onRestart = onRestart;
        this.onPause = onPause;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
            onPause.run();
            // stop this same ESC press from also triggering the pause screen's resume
            e.consume();
            return;
        }

        onStart.run();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                pacman.setNextDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                pacman.setNextDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                pacman.setNextDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                pacman.setNextDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_ENTER:
                onRestart.run();
                break;
        }
    }
}
