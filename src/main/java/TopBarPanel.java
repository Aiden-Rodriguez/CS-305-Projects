import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * File creates the top input bar.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class TopBarPanel extends JPanel {

    public interface OnOkListener { void onOk(String urlText); }

    private final JTextField urlField = new JTextField("GitHub Folder URL");
    private final JButton okButton = new JButton("OK");
    private OnOkListener listener;
    private boolean showingPlaceholder = true;

    public TopBarPanel() {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,8);
        c.weightx = 1.0; c.fill = GridBagConstraints.HORIZONTAL;
        add(urlField, c);
        c.weightx = 0; c.insets = new Insets(0,0,0,0);
        add(okButton, c);

        urlField.setForeground(new Color(115,115,115));
        urlField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    urlField.setText("");
                    urlField.setForeground(Color.BLACK);
                    showingPlaceholder = false;
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (urlField.getText().isEmpty()) {
                    showingPlaceholder = true;
                    urlField.setText("GitHub Folder URL");
                    urlField.setForeground(new Color(115,115,115));
                }
            }
        });

        okButton.addActionListener(ae -> {
            if (listener != null) listener.onOk(getUrlText());
        });
    }

    public String getUrlText() {
        return showingPlaceholder ? "" : urlField.getText().trim();
    }
    public void setOnOk(OnOkListener l) { this.listener = l; }
}
