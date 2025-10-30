package org.example;
/**
 * Generates data / changes to data
 * @author Aiden Rodriguez - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class DataSource implements Runnable {

    private String option;
    public DataSource(String option) {
        this.option = option;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (this.option == "add") {
                    org.example.Blackboard.getInstance().setValueAdding(i++);
                    Thread.sleep(100);
                } else if (this.option == "subtract"){
                    org.example.Blackboard.getInstance().setValueSubtracting(i--);
                    Thread.sleep(100);
                } else {
                    int min = 0;
                    int max = 1000;
                    int randomInRange = min + (int)(Math.random() * ((max - min) + 1));
                    org.example.Blackboard.getInstance().setValueRandom(randomInRange);
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ie) {
        }
    }
}
