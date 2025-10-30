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

        JPanel topBar = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel bottomArea = new JPanel();
        StatusBar statusBar = new StatusBar();

        add(topBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomArea, BorderLayout.SOUTH);
        add(statusBar, BorderLayout.SOUTH);
    }
}