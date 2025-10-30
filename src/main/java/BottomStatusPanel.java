import java.awt.*;
import javax.swing.*;

public class BottomStatusPanel extends JPanel {

    private final JTextField fileField = new JTextField("text");

    public BottomStatusPanel() {
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
