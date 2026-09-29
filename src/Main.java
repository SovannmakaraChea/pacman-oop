import game.GameSettings;
import map.MapLoader;
import ui.GamePanel;
import ui.MenuScreen;
import ui.PauseScreen;
import ui.SettingsScreen;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cards = new CardLayout();
        JPanel screens = new JPanel(cards);

        GameSettings settings = new GameSettings();
        MapLoader map = new MapLoader();

        Runnable showGame = () -> {
            cards.show(screens, "map");
            map.startGhosts();
            map.requestFocusInWindow();
        };

        GamePanel menu = new GamePanel(new MenuScreen(
                () -> {
                    map.applySettings(settings);
                    showGame.run();
                },
                () -> cards.show(screens, "settings")
        ));

        GamePanel settingsScreen = new GamePanel(new SettingsScreen(
                settings,
                () -> cards.show(screens, "menu")
        ));

        GamePanel pauseScreen = new GamePanel(new PauseScreen(
                showGame,
                () -> {
                    map.newGame();
                    showGame.run();
                },
                () -> {
                    map.newGame();
                    cards.show(screens, "menu");
                    map.playMenuMusic();
                }
        ));

        map.setOnPause(() -> cards.show(screens, "pause"));

        screens.add(menu, "menu");
        screens.add(settingsScreen, "settings");
        screens.add(pauseScreen, "pause");
        screens.add(map, "map");

        frame.add(screens);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        map.playMenuMusic();
    }
}
