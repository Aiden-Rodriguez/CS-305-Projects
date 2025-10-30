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

        JLabel complexityTitle = new JLabel("Complexity", SwingConstants.CENTER);
        JLabel sizeTitle       = new JLabel("Size",       SwingConstants.CENTER);
        JLabel overallTitle    = new JLabel("Overall",    SwingConstants.CENTER);

        add(complexityTitle);
        add(sizeTitle);
        add(overallTitle);

        RectanglePanel sizePanel = new RectanglePanel(Color.BLUE, 30, 0);
        RectanglePanel complexityPanel = new RectanglePanel(Color.RED, 30, 0);

        Blackboard bb = Blackboard.getInstance();
        bb.setSizePanel(sizePanel);
        bb.setComplexityPanel(complexityPanel);

        add(sizePanel);
        add(complexityPanel);

        revalidate();
        repaint();
    }
}