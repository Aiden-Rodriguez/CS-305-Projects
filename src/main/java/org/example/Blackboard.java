package org.example;
import java.beans.PropertyChangeSupport;
/**
 * Holds all program data
 * @author Aiden Rodriguez - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class Blackboard extends PropertyChangeSupport {

    private static Blackboard instance;
    private int valueAdding;
    private int valueSubtracting;
    private int valueRandom;

    private Blackboard() { super(new Object()); }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public int getValueAdding() { return valueAdding; }

    public void setValueAdding(int v) {
        int old = valueAdding;
        if (old != v) {
            valueAdding = v;
            firePropertyChange("valueAdding", old, v);
        }
    }
    public int getValueSubtracting() { return valueSubtracting; }

    public void setValueSubtracting(int v) {
        int old = valueSubtracting;
        if (old != v) {
            valueSubtracting = v;
            firePropertyChange("valueSubtracting", old, v);
        }
    }
    public int getValueRandom() { return valueRandom; }

    public void setValueRandom(int v) {
        int old = valueRandom;
        if (old != v) {
            valueRandom = v;
            firePropertyChange("valueRandom", old, v);
        }
    }
}
