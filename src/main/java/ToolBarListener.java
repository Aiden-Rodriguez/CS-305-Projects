import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Class that listens to clicks on the Jmenuitems
 * accessible throughout the application.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class ToolBarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "Open": {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose a folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File dir = chooser.getSelectedFile();
                } else {
                    Blackboard.getInstance().setStatusBarMessage("Open canceled");
                }
                break;
            }

            case "Exit":
                Blackboard.getInstance().setStatusBarMessage("Exiting…");
                System.exit(0);
                break;

            case "Action":
                Blackboard.getInstance().setStatusBarMessage("Action clicked");
                break;

            case "About":
                Blackboard.getInstance().setStatusBarMessage("About");
                JOptionPane.showMessageDialog(null,
                        "Assignment 01\nAuthors: Aiden Rodriguez & Brandon Powell",
                        "About", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                Blackboard.getInstance().setStatusBarMessage("Unknown: " + cmd);
        }
    }
}
