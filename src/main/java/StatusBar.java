import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Status bar that displays what is happening to the user within the program.
 * Now listens to Blackboard status message changes via PropertyChangeListener.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */
public class StatusBar extends JPanel {

    private final JLabel statusLabel;

    public StatusBar() {
        statusLabel = new JLabel("Status Bar");
        setBackground(new Color(0, 128, 128));
        setLayout(new BorderLayout()); // Better layout
        add(statusLabel, BorderLayout.WEST);
        statusLabel.setForeground(Color.WHITE);
    }

    public void registerWithBlackboard() {
        Blackboard bb = Blackboard.getInstance();
        bb.addPropertyChangeListener("statusMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String newMessage = (String) evt.getNewValue();
                SwingUtilities.invokeLater(() -> statusLabel.setText(newMessage));
            }
        });

        statusLabel.setText(bb.getStatusBarMessage());
    }
}