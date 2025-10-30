import javax.swing.*;
import java.awt.*;

/**
 * A panel that displays a vertical bar (rectangle) whose height
 * is proportional to the current value relative to a maximum.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 *
 */
public class RectanglePanel extends JPanel {
    private final Color color;
    private int max;
    private int current;

    public RectanglePanel(Color color, int max, int current) {
        this.color = color;
        this.max = Math.max(max, 1);
        this.current = Math.max(0, Math.min(current, max));
    }

    public void updateValues(int current, int max) {
        this.max = Math.max(max, 1);
        this.current = Math.max(0, Math.min(current, max));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);

        int width = getWidth() - 20;
        int height = getHeight() - 20;

        double ratio = (double) current / max;
        int fillHeight = (int) (height * ratio);

        int y = getHeight() - 10 - fillHeight;
        g.fillRect(10, y, width, fillHeight);
    }
}
