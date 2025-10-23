import java.io.File;

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

    private long maxLinesInFile;
    private long maxControlStatementsInFile;

    private long currentLinesInFile;
    private long currentControlStatementsInFile;

    private Boolean isAuthorInFile;
    private Boolean isVersionInFile;

    // Getters and Setters
    public void setStatusBarMessage(String message) {
        this.statusMessage = message;
        if (statusBar != null) {
            statusBar.setMessage(message);
        }
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
    }

    public void setMaxControlStatementsInFile(long maxControlStatementsInFile) {
        this.maxControlStatementsInFile = maxControlStatementsInFile;
    }

    public void setCurrentLinesInFile(long currentLinesInFile) {
        this.currentLinesInFile = currentLinesInFile;
    }

    public void setCurrentControlStatementsInFile(long currentControlStatementsInFile) {
        this.currentControlStatementsInFile = currentControlStatementsInFile;
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