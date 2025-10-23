import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Grid on left side of the program that shows file names and has buttons for those files.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */
public class FileGrid extends JPanel {
    private final JPanel listPanel = new JPanel();
    private final ActionListener fileClickListener = new FileClickListener();

    public FileGrid() {
        setLayout(new BorderLayout());
        setBackground(Color.YELLOW);

        listPanel.setLayout(new GridLayout(0, 1, 0, 5)); // infinite rows, 1 col
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 0);
    }

    public void populateFromDirectory(File dir) {
        listPanel.removeAll();
        if (dir == null || !dir.isDirectory()) {
            Blackboard.getInstance().setStatusBarMessage("Not a directory: " + dir);
            revalidate(); repaint();
            return;
        }

        File[] files = dir.listFiles(File::isFile);
        if (files == null) files = new File[0];

        Arrays.sort(files, Comparator.comparing(f -> f.getName().toLowerCase()));

        for (File f : files) {
            JButton btn = new JButton(f.getName());
            btn.putClientProperty("file", f);
            btn.addActionListener(fileClickListener);
            listPanel.add(btn);
        }

        Blackboard.getInstance().setStatusBarMessage("Loaded " + files.length + " files from " + dir.getName());
        listPanel.revalidate();
        listPanel.repaint();
    }
}