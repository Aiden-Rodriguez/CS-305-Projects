import javax.swing.*;
import java.awt.*;

/**
 * Displays the relevant information for the file such as control statements and length.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class GraphicsPanel extends JPanel {

    public GraphicsPanel() {
        setLayout(new GridLayout(2, 3));

        JLabel complexityTitle = new JLabel("Complexity");
        JLabel sizeTitle = new JLabel("Size");
        JLabel overallTitle = new JLabel("Overall");

        complexityTitle.setHorizontalAlignment(SwingConstants.CENTER);
        sizeTitle.setHorizontalAlignment(SwingConstants.CENTER);
        overallTitle.setHorizontalAlignment(SwingConstants.CENTER);

        add(complexityTitle);
        add(sizeTitle);
        add(overallTitle);

        JPanel complexityGraphic = new RectanglePanel(Color.RED);
        JPanel sizeGraphic = new RectanglePanel(Color.BLUE);
        JPanel overallGraphic = new CirclePanel(Color.YELLOW);

        add(complexityGraphic);
        add(sizeGraphic);
        add(overallGraphic);
    }

    private static class CirclePanel extends JPanel {
        private final Color color;

        public CirclePanel(Color color) {
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            int diameter = Math.min(getWidth(), getHeight()) - 20;
            g.fillOval((getWidth() - diameter) / 2, (getHeight() - diameter) / 2, diameter, diameter);
        }
    }

    private static class RectanglePanel extends JPanel {
        private final Color color;

        public RectanglePanel(Color color) {
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            int width = getWidth() - 20;
            int height = getHeight() - 20;
            g.fillRect(10, 10, width, height);
        }
    }

}
