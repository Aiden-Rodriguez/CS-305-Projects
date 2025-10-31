import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Class that listens to clicks on the JMenuItems
 * accessible throughout the application.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */
public class ToolBarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        Blackboard bb = Blackboard.getInstance();

        switch (cmd) {
            case "Open from URL...":
                handleOpenFromURL();
                break;

            case "Reload":
                handleReload();
                break;

            case "Clear":
                handleClear();
                break;

            case "Exit":
                bb.setStatusBarMessage("Exiting...");
                System.exit(0);
                break;

            case "About":
                bb.setStatusBarMessage("About");
                JOptionPane.showMessageDialog(null,
                        "Assignment 02\nAuthors: Aiden Rodriguez & Brandon Powell",
                        "About", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                bb.setStatusBarMessage("Unknown: " + cmd);
        }
    }

    /**
     * Opens a dialog to get GitHub URL from user and loads files from it.
     * Same functionality as clicking OK button in TopBarPanel.
     */
    private void handleOpenFromURL() {
        Blackboard bb = Blackboard.getInstance();

        String url = JOptionPane.showInputDialog(
                null,
                "Enter GitHub repository URL:",
                "Open from URL",
                JOptionPane.PLAIN_MESSAGE
        );

        if (url == null || url.trim().isEmpty()) {
            bb.setStatusBarMessage("Open canceled");
            return;
        }

        bb.setStatusBarMessage("Preparing...");

        try {
            String dirUrl = URLFormatter.toTreeDirUrl(url.trim());
            bb.setCurrentUrl(dirUrl);
            loadFilesFromGitHub(dirUrl);
        } catch (Exception ex) {
            bb.setStatusBarMessage("Invalid URL");
            ex.printStackTrace();
        }
    }

    private void handleReload() {
        Blackboard bb = Blackboard.getInstance();
        String currentUrl = bb.getCurrentUrl();

        if (currentUrl == null || currentUrl.isEmpty()) {
            bb.setStatusBarMessage("No URL to reload. Please open a repository first.");
            JOptionPane.showMessageDialog(null,
                    "No repository loaded. Please use 'Open from URL' first.",
                    "Cannot Reload",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        bb.setStatusBarMessage("Reloading...");
        loadFilesFromGitHub(currentUrl);
    }

    private void handleClear() {
        Blackboard bb = Blackboard.getInstance();

        // Clear the grid by sending empty data
        FileAnalyzer.AnalysisResult[] emptyResults = new FileAnalyzer.AnalysisResult[0];
        String[] emptyNames = new String[0];
        bb.updateGridData(emptyResults, 0, emptyNames);

        // Clear selected file name
        bb.setSelectedFileName("");

        // Clear current URL
        bb.setCurrentUrl("");

        bb.setStatusBarMessage("Cleared all data");
    }

    private void loadFilesFromGitHub(String dirUrl) {
        FileHandler.getFileListAsync(dirUrl, new FileHandler.FileListCallback() {
            @Override
            public void onSuccess(List<String> javaFiles) {
                if (javaFiles.isEmpty()) {
                    Blackboard.getInstance().setStatusBarMessage("No .java files found.");
                    return;
                }

                new FileAnalysisWorker(javaFiles).execute();
            }

            @Override
            public void onError(Exception ex) {
                Blackboard.getInstance().setStatusBarMessage("Failed to load file list: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}