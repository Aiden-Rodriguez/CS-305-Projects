import javiergs.tulip.GitHubHandler;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public static String getFileName(String pathOrUrl) {
        if (pathOrUrl == null || pathOrUrl.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Remove any trailing slashes
        String trimmed = pathOrUrl.replaceAll("/+$", "");

        // Split by forward slash and return the last piece
        int lastSlash = trimmed.lastIndexOf('/');
        if (lastSlash == -1) return trimmed; // no slash present
        return trimmed.substring(lastSlash + 1);
    }

    public static String toTreeDirUrl(String url) {
        if (url == null || url.isEmpty()) throw new IllegalArgumentException("URL empty");
        // Ensure directory semantics
        url = url.replace("/blob/", "/tree/");
        // strip trailing slashes (optional)
        return url.replaceAll("/+$", "");
    }

    public static String toBlobFileUrl(String url) {
        if (url == null || url.isEmpty()) throw new IllegalArgumentException("URL empty");
        // Ensure file semantics
        return url.replace("/tree/", "/blob/");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = urlField.getText().trim();
        Blackboard bb = Blackboard.getInstance();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a GitHub URL", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            bb.setStatusBarMessage("Fetching file list from GitHub...");
            String dirUrl = toTreeDirUrl(input);
            List<String> javaFiles = FileHandler.getFileList(dirUrl);

            if (javaFiles == null || javaFiles.isEmpty()) {
                bb.setStatusBarMessage("No .java files found in directory.");
                return;
            }

            bb.setStatusBarMessage("Found " + javaFiles.size() + " Java files. Analyzing...");

            // Analyze all Java files and find max line count
            FileAnalyzer.AnalysisResult[] results = new FileAnalyzer.AnalysisResult[javaFiles.size()];
            String[] fileNames = new String[javaFiles.size()];
            long maxLines = 0;

            for (int i = 0; i < javaFiles.size(); i++) {
                try {
                    bb.setStatusBarMessage("Analyzing file " + (i + 1) + " of " + javaFiles.size() + "...");
                    String fileUrl = javaFiles.get(i);

                    // Extract just the filename from the URL
                    fileNames[i] = getFileName(fileUrl);

                    String content = FileHandler.getFile(fileUrl);
                    results[i] = FileAnalyzer.analyze(content);

                    if (results[i].lineCount > maxLines) {
                        maxLines = results[i].lineCount;
                    }
                } catch (Exception ex) {
                    // If a specific file fails, create an empty result
                    results[i] = new FileAnalyzer.AnalysisResult(0, 0, 0, 0, 0, false, false);
                    fileNames[i] = "Error loading file";
                    System.err.println("Warning: Could not load file " + javaFiles.get(i));
                }
            }

            // Update grid with file analysis results
            GridPanel grid = bb.getGrid();
            if (grid != null) {
                grid.updateFromFiles(results, maxLines, fileNames);
            }

            bb.setStatusBarMessage("Successfully loaded " + javaFiles.size() + " Java files");

        } catch (Exception ex) {
            bb.setStatusBarMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}