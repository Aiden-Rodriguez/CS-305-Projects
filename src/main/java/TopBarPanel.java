import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Panel that provides URL input and triggers file loading.
 * Communicates with Blackboard to coordinate file loading workflow.
 * Separated concerns: UI management, URL validation, and workflow coordination.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.2
 */
public class TopBarPanel extends JPanel implements ActionListener {

    private final JTextField urlField = new JTextField("GitHub Folder URL");
    private final JButton okButton = new JButton("OK");
    private boolean showingPlaceholder = true;

    public TopBarPanel() {
        super(new GridBagLayout());
        setupLayout();
        setupPlaceholderBehavior();
        okButton.addActionListener(this);
    }

    private void setupLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 8);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(urlField, c);

        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        add(okButton, c);

        urlField.setForeground(new Color(115, 115, 115));
    }

    private void setupPlaceholderBehavior() {
        urlField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    urlField.setText("");
                    urlField.setForeground(Color.BLACK);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (urlField.getText().isEmpty()) {
                    showingPlaceholder = true;
                    urlField.setText("GitHub Folder URL");
                    urlField.setForeground(new Color(115, 115, 115));
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = getInputUrl();
        if (input == null) {
            return;
        }

        Blackboard bb = Blackboard.getInstance();
        bb.setStatusBarMessage("Preparing...");

        try {
            String dirUrl = URLFormatter.toTreeDirUrl(input);
            bb.setCurrentUrl(dirUrl);

            loadFilesFromGitHub(dirUrl);

        } catch (Exception ex) {
            bb.setStatusBarMessage("Invalid URL");
            ex.printStackTrace();
        }
    }

    private String getInputUrl() {
        String input = urlField.getText().trim();
        if (input.isEmpty() || showingPlaceholder) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a GitHub URL",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return input;
    }

    private void loadFilesFromGitHub(String dirUrl) {
        FileHandler.getFileListAsync(dirUrl, new FileHandler.FileListCallback() {
            @Override
            public void onSuccess(List<String> javaFiles) {
                if (javaFiles.isEmpty()) {
                    Blackboard.getInstance().setStatusBarMessage("No .java files found.");
                    return;
                }

                // Start file analysis in background
                new FileAnalysisWorker(javaFiles).execute();
            }

            @Override
            public void onError(Exception ex) {
                Blackboard.getInstance().setStatusBarMessage("Failed to load file list.");
                ex.printStackTrace();
            }
        });
    }
}