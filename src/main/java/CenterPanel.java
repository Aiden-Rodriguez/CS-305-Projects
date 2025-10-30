import javax.swing.*;
import java.util.List;

public class CenterPanel extends JPanel {
    private JTextArea testField;

    public CenterPanel(){
        testField = new JTextArea("Test Area");

        add(testField);
    }

    public void displayFiles(List<String> files) {
        StringBuilder sb = new StringBuilder();
        sb.append("Files found: ").append(files.size()).append("\n\n");
        for (String file : files) {
            sb.append(file).append("\n");
        }
        testField.setText(sb.toString());
    }

    public void displayError(String errorMessage) {
        testField.setText(errorMessage);
    }
}
