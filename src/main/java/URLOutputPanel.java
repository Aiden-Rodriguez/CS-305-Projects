import javax.swing.*;
import java.awt.*;
import java.util.List;

public class URLOutputPanel extends JPanel {
    private JTextArea outputArea;

    public URLOutputPanel(){
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayFiles(List<String> files) {
        StringBuilder sb = new StringBuilder();
        sb.append("Files found: ").append(files.size()).append("\n\n");
        for (String file : files) {
            sb.append(file).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    public void displayError(String errorMessage) {
        outputArea.setText(errorMessage);
    }
}
