import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Class that holds shared global variables
 * accessible throughout the application.
 * @author Aiden Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
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
    private GridPanel grid;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void setGrid(GridPanel g) { this.grid = g; }
    public GridPanel getGrid() { return this.grid; }


    /**
     * Sets the status bar message and fires a property change event.
     */
    public void setStatusBarMessage(String message) {
        String oldMessage = this.statusMessage;
        this.statusMessage = message;

        pcs.firePropertyChange("statusMessage", oldMessage, message);
    }

    public String getStatusBarMessage() {
        return statusMessage;
    }

    public void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

}