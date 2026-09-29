package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

class MenuButton extends JButton {

    private boolean hover = false;

    MenuButton(String text, int fontSize, int width, int height, Runnable onClick) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, fontSize));
        setForeground(Color.YELLOW);
        setPreferredSize(new Dimension(width, height));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });

        addActionListener(e -> onClick.run());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int push = getModel().isPressed() ? 3 : 0;
        int w = getWidth() - 6;
        int h = getHeight() - 6;

        g2.setColor(MenuScreen.SHADOW);
        g2.fillRoundRect(5, 5, w, h, 28, 28);

        g2.setColor(hover ? new Color(55, 55, 55) : MenuScreen.INK);
        g2.fillRoundRect(push, push, w, h, 28, 28);

        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        String text = getText();
        int textX = push + (w - fm.stringWidth(text)) / 2;
        int textY = push + (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, textX, textY);

        g2.dispose();
    }
}
