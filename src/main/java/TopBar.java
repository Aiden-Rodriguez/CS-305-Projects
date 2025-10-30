import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class TopBar extends JPanel implements ActionListener {

        private JTextField folderUrl;
        private JButton okURL;
        private String token;

        private CenterPanel centerPanel;

    public TopBar(CenterPanel centerPanel){
        this.centerPanel = centerPanel;

        // Test with: https://github.com/CSC3100/Tool-Maven/tree/main
        folderUrl = new JTextField("GitHub Folder URL");
        okURL = new JButton("OK");

        okURL.addActionListener(this);

        setLayout(new BorderLayout());
        add(folderUrl, BorderLayout.WEST);
        add(okURL, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = folderUrl.getText().trim();

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
            centerPanel.displayFiles(files);

        } catch (IOException ex) {
            centerPanel.displayError("IO Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to fetch files:\n" + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            centerPanel.displayError("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "An error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
