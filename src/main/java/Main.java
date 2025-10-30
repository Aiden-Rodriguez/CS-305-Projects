import javax.swing.*;
import java.awt.*;

/**
 * File that controls the program; stitches everything together.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.0
 */

public class Main extends JFrame{

    public static void main(String[] args) {
        Main main = new Main();
        main.pack();
        main.setSize(600, 400);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setTitle("Assignment 02");
        main.setVisible(true);
    }

    public Main() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu actionMenu = new JMenu("Action");
        JMenu helpMenu = new JMenu("Help");

        ToolBarListener tbListener = new ToolBarListener();

        JMenuItem openURLMenuItem = new JMenuItem("Open from URL...");
        openURLMenuItem.addActionListener(tbListener);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(tbListener);

        JMenuItem reloadMenuItem = new JMenuItem("Reload");
        reloadMenuItem.addActionListener(tbListener);
        JMenuItem clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.addActionListener(tbListener);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(tbListener);

        fileMenu.add(openURLMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        actionMenu.add(reloadMenuItem);
        actionMenu.add(clearMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(actionMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        CenterPanel centerPanel = new CenterPanel();
        TopBar topBar = new TopBar(centerPanel);

        JPanel bottomPanel = new JPanel();

        JPanel bottomArea = new JPanel();
        JLabel selectedFile = new JLabel("Selected File Name:");
        JTextField fileName = new JTextField("text");

        StatusBar statusBar = new StatusBar();

        bottomArea.setLayout(new BorderLayout());
        bottomArea.add(selectedFile, BorderLayout.WEST);
        bottomArea.add(fileName, BorderLayout.CENTER);

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(bottomArea, BorderLayout.NORTH);
        bottomPanel.add(statusBar, BorderLayout.SOUTH);

        add(topBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}