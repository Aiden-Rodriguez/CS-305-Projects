import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Main extends JFrame {

    public static String token = "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";

    public static void main(String[] args) {
        Main main = new Main();
        main.pack();
        main.setSize(600, 400);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setTitle("Assignment 01");
        main.setVisible(true);
    }

    public Main(){
        URLOutputPanel outputPanel = new URLOutputPanel();
        URLInputPanel inputPanel = new URLInputPanel(outputPanel, token);

        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
    }
}