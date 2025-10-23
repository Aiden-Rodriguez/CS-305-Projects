import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Class that listens to clicks on the FileGrid
 * accessible throughout the application.
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class FileClickListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = extractFileFromEvent(e);
        if (file == null || !file.isFile()) {
            Blackboard.setStatusBarMessage("No file associated with this item.");
            return;
        }

        try {
            FileAnalyzer.AnalysisResult r = FileAnalyzer.analyze(file);

            Blackboard.setStatusBarMessage(r.toStatusMessage());

            Blackboard.setCurrentLinesInFile(r.lineCount);
            Blackboard.setCurrentControlStatementsInFile(r.ifCount + r.switchCount + r.forCount + r.whileCount);
            Blackboard.setIsAuthorInFile(r.hasAuthor);
            Blackboard.setIsVersionInFile(r.hasVersion);

        } catch (Exception ex) {
            Blackboard.setStatusBarMessage("Error reading " + file.getName() + ": " + ex.getMessage());
        }
    }

    private File extractFileFromEvent(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JButton btn) {
            Object prop = btn.getClientProperty("file");
            if (prop instanceof File f) return f;
        }
        return null;
    }
}