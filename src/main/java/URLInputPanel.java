import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * File that controls the input panel;
 * Responsible for user input and interaction
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class URLInputPanel extends JPanel implements ActionListener {
    private JTextField urlField;
    private JButton openButton;
    private URLOutputPanel outputPanel;
    private String token;

    public URLInputPanel(URLOutputPanel outputPanel, String token){
        this.outputPanel = outputPanel;
        this.token = token;

        setLayout(new BorderLayout());
        urlField = new JTextField("https://github.com/CSC3100/Tool-Maven/tree/main");
        openButton = new JButton("Open");

        add(new JLabel("Enter GitHub URL: "), BorderLayout.WEST);
        add(urlField, BorderLayout.CENTER);
        add(openButton, BorderLayout.EAST);

        openButton.addActionListener(this);
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
        }

        try {
            GitHubHandler gh = new GitHubHandler(token);
            List<String> files = gh.listFilesRecursive(url);
            outputPanel.displayFiles(files);

        } catch (IOException ex) {
            outputPanel.displayError("IO Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to fetch files:\n" + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            outputPanel.displayError("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "An error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}