package ui;

import java.awt.*;
import javax.swing.*;

class MenuTitle extends JComponent {

    private final String text;
    private final Font font;

    MenuTitle(String text, int fontSize) {
        this.text = text;
        this.font = new Font("Arial", Font.BOLD, fontSize);
        setPreferredSize(new Dimension(480, fontSize + 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

        g2.setColor(MenuScreen.SHADOW);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(MenuScreen.INK);
        g2.drawString(text, x, y);
        g2.dispose();
    }
}
