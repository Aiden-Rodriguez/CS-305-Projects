import javax.swing.*;
import java.util.List;

/**
 * SwingWorker responsible for analyzing Java files in the background.
 * Reports progress to Blackboard and updates grid data when complete.
 * Without it status updates wouldn't show while GH info is retrieved.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class FileAnalysisWorker extends SwingWorker<Void, String> {

    private final List<String> javaFiles;

    public FileAnalysisWorker(List<String> javaFiles) {
        this.javaFiles = javaFiles;
    }

    @Override
    protected Void doInBackground() throws Exception {
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
            if (result.lineCount > maxLines) {
                maxLines = result.lineCount;
            }
        }

        final long finalMaxLines = maxLines;
        SwingUtilities.invokeLater(() -> {
            Blackboard bb = Blackboard.getInstance();
            bb.updateGridData(results, finalMaxLines, fileNames);
            bb.setStatusBarMessage("Loaded " + javaFiles.size() + " files");
        });

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        String latest = chunks.get(chunks.size() - 1);
        Blackboard.getInstance().setStatusBarMessage(latest);
    }

    @Override
    protected void done() {
        try {
            get(); // Check for exceptions
        } catch (Exception e) {
            Blackboard.getInstance().setStatusBarMessage("Error analyzing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}