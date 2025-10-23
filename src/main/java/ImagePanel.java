import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A simple panel that displays an image loaded from the resources folder.
 * Automatically sizes itself to fit the image.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class ImagePanel extends JPanel {

    private final JLabel imageLabel;

    public ImagePanel(String resourcePath) {
        setLayout(new BorderLayout());
        setOpaque(false);

        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Image not found: " + resourcePath);
        }

        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        add(imageLabel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(100, 100));
    }

    public void setImage(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Image not found: " + resourcePath);
        }

        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
        revalidate();
        repaint();
    }
}
