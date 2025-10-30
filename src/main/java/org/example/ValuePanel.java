package org.example;

import org.example.Blackboard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Panel that displays number generated
 * @author Aiden Rodriguez - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class ValuePanel extends JPanel implements PropertyChangeListener {

    private final JLabel label = new JLabel("0", SwingConstants.CENTER);
    private final String propertyName;
    public ValuePanel(String option) {
        this.propertyName = option;
        label.setFont(label.getFont().deriveFont(Font.BOLD, 64f));
        add(label, BorderLayout.CENTER);
        //observe blackboard form valuepanel
        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (propertyName.equals(evt.getPropertyName())) {
            int v = (int) evt.getNewValue();
            label.setText(String.valueOf(v));
        }
    }
}

