package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Main file that stitches all together.
 * @author Aiden Rodriguez - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class Main extends JFrame {

    public Main() {
        DataSource additionProducer = new DataSource("add");
        Thread t0 = new Thread(additionProducer);

        DataSource subtractionProducer = new DataSource("subtract");
        Thread t1 = new Thread(subtractionProducer);

        DataSource randomProducer = new DataSource("random");
        Thread t2 = new Thread(randomProducer);

        t0.setDaemon(true);
        t0.start();
        t1.setDaemon(true);
        t1.start();
        t2.setDaemon(true);
        t2.start();

        setLayout(new GridLayout(3,1));
        ValuePanel panelAdding = new ValuePanel("valueAdding");
        add(panelAdding);
        ValuePanel panelSubtracting = new ValuePanel("valueSubtracting");
        add(panelSubtracting);
        ValuePanel panelRandom = new ValuePanel("valueRandom");
        add(panelRandom);

    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Lab");
        frame.setSize(400, 250);
        frame.setVisible(true);
    }
}
