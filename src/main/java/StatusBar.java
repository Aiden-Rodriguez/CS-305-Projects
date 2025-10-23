import javax.swing.*;
import java.awt.*;

/**
 * Status bar that displays what is happening to the user within the program.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class StatusBar extends JPanel{
    private final JLabel statusLabel;
    public StatusBar() {
        statusLabel = new JLabel("Status Bar");
        setBackground(new Color(0, 128, 128));
        add(statusLabel);
    }

    public void setMessage(String message) {
        statusLabel.setText(message);
    }
}
