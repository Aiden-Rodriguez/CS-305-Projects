import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Component that shows the selected file name.
 * Listens to Blackboard for selection changes via PropertyChangeListener.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */
public class SelectedFileNamePanel extends JPanel {

    private final JTextField fileField = new JTextField("");

    public SelectedFileNamePanel() {
        super(new GridBagLayout());
        setupLayout();
        registerWithBlackboard();
    }

    private void setupLayout() {
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(0, 0, 0, 8);
        add(new JLabel("Selected File Name:"), b);

        b.weightx = 1.0;
        b.fill = GridBagConstraints.HORIZONTAL;
        fileField.setEditable(false);
        add(fileField, b);
    }

    private void registerWithBlackboard() {
        Blackboard bb = Blackboard.getInstance();

        bb.addPropertyChangeListener("selectedFileName", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String newFileName = (String) evt.getNewValue();
                SwingUtilities.invokeLater(() -> fileField.setText(newFileName));
            }
        });

        fileField.setText(bb.getSelectedFileName());
    }
}