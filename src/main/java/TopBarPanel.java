import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Panel that provides URL input and triggers file loading.
 * Coordinates between FileHandler and GridPanel via Blackboard.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
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

    private static class FileAnalysisWorker extends SwingWorker<Void, String> {
        private final List<String> javaFiles;

        FileAnalysisWorker(List<String> javaFiles) {
            this.javaFiles = javaFiles;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Blackboard bb = Blackboard.getInstance();
            publish("Analyzing " + javaFiles.size() + " files...");

            FileAnalyzer.AnalysisResult[] results = new FileAnalyzer.AnalysisResult[javaFiles.size()];
            String[] fileNames = new String[javaFiles.size()];
            long maxLines = 0;

            for (int i = 0; i < javaFiles.size(); i++) {
                publish("Analyzing file " + (i + 1) + " of " + javaFiles.size() + "...");

                String fileUrl = javaFiles.get(i);
                String fileName = URLFormatter.extractFileName(fileUrl);
                String content = FileHandler.getFile(fileUrl);
                FileAnalyzer.AnalysisResult result = FileAnalyzer.analyze(content);

                results[i] = result;
                fileNames[i] = fileName;
                if (result.lineCount > maxLines) maxLines = result.lineCount;
            }

            final long finalMaxLines = maxLines;
            final FileAnalyzer.AnalysisResult[] finalResults = results;
            final String[] finalNames = fileNames;

            SwingUtilities.invokeLater(() -> {
                GridPanel grid = bb.getGrid();
                if (grid != null) {
                    grid.updateFromFiles(finalResults, finalMaxLines, finalNames);
                }
                bb.setStatusBarMessage("Loaded " + javaFiles.size() + " files");
            });

            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            String latest = chunks.get(chunks.size() - 1);
            Blackboard.getInstance().setStatusBarMessage(latest);
        }
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
        if (input == null) return;

        Blackboard bb = Blackboard.getInstance();
        bb.setStatusBarMessage("Preparing...");

        try {
            String dirUrl = URLFormatter.toTreeDirUrl(input);

            FileHandler.getFileListAsync(dirUrl, new FileHandler.FileListCallback() {
                @Override
                public void onSuccess(List<String> javaFiles) {
                    if (javaFiles.isEmpty()) {
                        bb.setStatusBarMessage("No .java files found.");
                        return;
                    }

                    // Now analyze files in background
                    new FileAnalysisWorker(javaFiles).execute();
                }

                @Override
                public void onError(Exception ex) {
                    bb.setStatusBarMessage("Failed to load file list.");
                    ex.printStackTrace();
                }
            });

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

    private static class URLFormatter {

        public static String toTreeDirUrl(String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("URL cannot be empty");
            }
            url = url.replace("/blob/", "/tree/");
            return url.replaceAll("/+$", "");
        }

        public static String extractFileName(String pathOrUrl) {
            if (pathOrUrl == null || pathOrUrl.isEmpty()) {
                throw new IllegalArgumentException("Path cannot be null or empty");
            }

            String trimmed = pathOrUrl.replaceAll("/+$", "");
            int lastSlash = trimmed.lastIndexOf('/');

            if (lastSlash == -1) {
                return trimmed;
            }
            return trimmed.substring(lastSlash + 1);
        }
    }
}