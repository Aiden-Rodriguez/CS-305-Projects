import javax.swing.*;

/**
 * Class that holds shared global variables
 * accessible throughout the application.
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class Blackboard {

    private static Blackboard instance;

    private Blackboard() { }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    private String statusMessage = "Status Bar";
    private StatusBar statusBar;
    private FileGrid fileGrid;

    private RectanglePanel complexityPanel;
    private RectanglePanel sizePanel;
    public void setComplexityPanel(RectanglePanel p) { this.complexityPanel = p; }
    public void setSizePanel(RectanglePanel p) { this.sizePanel = p; }
    private long maxLinesInFile = 30;
    private long maxControlStatementsInFile = 30;
    private long currentLinesInFile;
    private long currentControlStatementsInFile;

    private Boolean isAuthorInFile;
    private Boolean isVersionInFile;
    private ImagePanel overallImagePanel;

    public void setStatusBarMessage(String message) {
        this.statusMessage = message;
        if (statusBar != null) {
            statusBar.setMessage(message);
        }
    }

    public void setOverallImagePanel(ImagePanel panel) {
        this.overallImagePanel = panel;
    }
    public void updateOverallMoodImage() {
        if (overallImagePanel == null) return;

        String path;
        if (Boolean.TRUE.equals(isAuthorInFile) && Boolean.TRUE.equals(isVersionInFile)) {
            path = "/happy.png";
        } else if (Boolean.TRUE.equals(isAuthorInFile) || Boolean.TRUE.equals(isVersionInFile)) {
            path = "/neutral.png";
        } else {
            path = "/sad.png";
        }

        overallImagePanel.setImage(path);
    }

    public String getStatusBarMessage() {
        return statusMessage;
    }

    public void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public void setFileGrid(FileGrid fileGrid) {
        this.fileGrid = fileGrid;
    }

    public void setMaxLinesInFile(long maxLinesInFile) {
        this.maxLinesInFile = maxLinesInFile;
        pushSizeUpdate();
    }
    public void setMaxControlStatementsInFile(long maxControlStatementsInFile) {
        this.maxControlStatementsInFile = maxControlStatementsInFile;
        pushComplexityUpdate();
    }

    public void setCurrentLinesInFile(long currentLinesInFile) {
        this.currentLinesInFile = currentLinesInFile;
        pushSizeUpdate();
    }
    public void setCurrentControlStatementsInFile(long currentControlStatementsInFile) {
        this.currentControlStatementsInFile = currentControlStatementsInFile;
        pushComplexityUpdate();
    }

    private void pushSizeUpdate() {
        if (sizePanel == null) return;
        int cur = (int)Math.max(0, Math.min(currentLinesInFile, Integer.MAX_VALUE));
        int max = (int)Math.max(1, Math.min(maxLinesInFile, Integer.MAX_VALUE));
        SwingUtilities.invokeLater(() -> sizePanel.updateValues(cur, max));
    }
    private void pushComplexityUpdate() {
        if (complexityPanel == null) return;
        int cur = (int)Math.max(0, Math.min(currentControlStatementsInFile, Integer.MAX_VALUE));
        int max = (int)Math.max(1, Math.min(maxControlStatementsInFile, Integer.MAX_VALUE));
        SwingUtilities.invokeLater(() -> complexityPanel.updateValues(cur, max));
    }

    public void setIsAuthorInFile(Boolean isAuthorInFile) {
        this.isAuthorInFile = isAuthorInFile;
    }

    public void setIsVersionInFile(Boolean isVersionInFile) {
        this.isVersionInFile = isVersionInFile;
    }

    public FileGrid getFileGrid() {
        return fileGrid;
    }
}