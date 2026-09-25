package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MenuScreen extends JPanel {

    private static final Color INK = new Color(20, 20, 20);
    private static final Color SHADOW = new Color(200, 120, 0);

    public MenuScreen(Runnable onPlay) {
        // Transparent so the GamePanel background shows through
        setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new Title(), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(new PlayButton(onPlay), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        add(new Subtitle("ARROW KEYS OR WASD TO MOVE"), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(new Subtitle("EAT A CHERRY TO SCARE THE GHOSTS"), gbc);
    }

    // "PAC-MAN" in big black letters with an orange drop shadow.
    private static class Title extends JComponent {

        private static final String TEXT = "PAC-MAN";
        private final Font font = new Font("Arial", Font.BOLD, 84);

        Title() {
            setPreferredSize(new Dimension(480, 100));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(font);

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(TEXT)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

            g2.setColor(SHADOW);
            g2.drawString(TEXT, x + 5, y + 5);
            g2.setColor(INK);
            g2.drawString(TEXT, x, y);
            g2.dispose();
        }
    }

    // Small spaced-out capitals, used for the controls hint.
    private static class Subtitle extends JLabel {

        Subtitle(String text) {
            super(spaced(text));
            setForeground(INK);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        private static String spaced(String text) {
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                sb.append(c).append(c == ' ' ? "  " : " ");
            }
            return sb.toString().trim();
        }
    }

    // Rounded black button with yellow text; lighter while hovered,
    // pushed down a little while pressed.
    private static class PlayButton extends JButton {

        private boolean hover = false;

        PlayButton(Runnable onPlay) {
            super("PLAY");
            setFont(new Font("Arial", Font.BOLD, 30));
            setForeground(Color.YELLOW);
            setPreferredSize(new Dimension(220, 64));
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

            addActionListener(e -> onPlay.run());
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

            // Orange shadow under the button
            g2.setColor(SHADOW);
            g2.fillRoundRect(5, 5, w, h, 28, 28);

            g2.setColor(hover ? new Color(55, 55, 55) : INK);
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
}
