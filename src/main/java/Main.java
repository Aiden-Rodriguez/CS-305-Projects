import javiergs.tulip.GitHubHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Class that displays files in specified path
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class Main extends JFrame implements ActionListener {

    public static String token = "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";

    private JTextField urlField;
    private JTextArea outputArea;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.pack();
        main.setSize(600, 400);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setTitle("Assignment 01");
        main.setVisible(true);
    }

    public Main(){
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        urlField = new JTextField("https://github.com/CSC3100/Tool-Maven/tree/main");
        JButton openButton = new JButton("Open");

        inputPanel.add(new JLabel("Enter GitHub URL: "), BorderLayout.WEST);
        inputPanel.add(urlField, BorderLayout.CENTER);
        inputPanel.add(openButton, BorderLayout.EAST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        openButton.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText().trim();

        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a GitHub URL",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GitHubHandler gh = new GitHubHandler(token);
            List<String> files = gh.listFilesRecursive(url);

            // Display results
            StringBuilder sb = new StringBuilder();
            sb.append("Files found: ").append(files.size()).append("\n\n");
            for (String file : files) {
                sb.append(file).append("\n");
            }
            outputArea.setText(sb.toString());

        } catch (IOException ex) {
            outputArea.setText("IO Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to fetch files:\n" + ex.getMessage(),
                    "IO Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "An error occurred:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
