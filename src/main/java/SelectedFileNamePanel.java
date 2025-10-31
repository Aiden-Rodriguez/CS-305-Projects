import java.awt.*;
import javax.swing.*;

/**
 * Component that shows the selected file name
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class SelectedFileNamePanel extends JPanel {

    private final JTextField fileField = new JTextField("text");

    public SelectedFileNamePanel() {
        super(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.insets = new Insets(0,0,0,8);
        add(new JLabel("Selected File Name:"), b);
        b.weightx = 1.0; b.fill = GridBagConstraints.HORIZONTAL;
        fileField.setEditable(false);
        add(fileField, b);
    }

    public void setSelectedName(String name) {
        fileField.setText(name);
    }
}
