import javiergs.tulip.GitHubHandler;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

/**
 * File creates the top input bar.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class TopBarPanel extends JPanel implements ActionListener {

    private final JTextField urlField = new JTextField("GitHub Folder URL");
    private final JButton okButton = new JButton("OK");
    private boolean showingPlaceholder = true;
    private String token;

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

        okButton.addActionListener(this);
    }

    public String getUrlText() {
        return showingPlaceholder ? "" : urlField.getText().trim();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText().trim();

        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a GitHub URL",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            FileHandler.getFileList(url);
            FileHandler.getFile("https://github.com/CSC3100/Tool-Maven/blob/main/src/main/java/javiergs/Main.java");
        }
    }
}

