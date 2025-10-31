import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Class that holds shared global variables and manages communication
 * between components using PropertyChangeSupport.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.2
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
    private String selectedFileName = "";
    private String currentUrl = "";
    private GridPanel grid;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setGrid(GridPanel g) {
        this.grid = g;
    }

    public GridPanel getGrid() {
        return this.grid;
    }

    public void setStatusBarMessage(String message) {
        String oldMessage = this.statusMessage;
        this.statusMessage = message;
        pcs.firePropertyChange("statusMessage", oldMessage, message);
    }

    public String getStatusBarMessage() {
        return statusMessage;
    }

    public void setSelectedFileName(String fileName) {
        String oldFileName = this.selectedFileName;
        this.selectedFileName = fileName;
        pcs.firePropertyChange("selectedFileName", oldFileName, fileName);
    }

    public String getSelectedFileName() {
        return selectedFileName;
    }

    public void setCurrentUrl(String url) {
        String oldUrl = this.currentUrl;
        this.currentUrl = url;
        pcs.firePropertyChange("currentUrl", oldUrl, url);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void updateGridData(FileAnalyzer.AnalysisResult[] results, long maxLines, String[] names) {
        GridData oldData = new GridData(null, 0, null);
        GridData newData = new GridData(results, maxLines, names);
        pcs.firePropertyChange("gridData", oldData, newData);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public static class GridData {
        public final FileAnalyzer.AnalysisResult[] results;
        public final long maxLines;
        public final String[] names;

        public GridData(FileAnalyzer.AnalysisResult[] results, long maxLines, String[] names) {
            this.results = results;
            this.maxLines = maxLines;
            this.names = names;
        }
    }
}