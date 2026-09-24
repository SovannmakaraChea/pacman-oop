import map.MapLoader;
import ui.GamePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CardLayout lets us swap between the menu and the map in the same window
        CardLayout cards = new CardLayout();
        JPanel screens = new JPanel(cards);

        MapLoader map = new MapLoader();
        GamePanel menu = new GamePanel(() -> {
            cards.show(screens, "map");
            map.requestFocusInWindow();
        });

        screens.add(menu, "menu");
        screens.add(map, "map");

        frame.add(screens);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
