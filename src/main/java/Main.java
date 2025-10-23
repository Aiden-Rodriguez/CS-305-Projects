import javax.swing.*;
import java.awt.*;

/**
 * File that controls the whole program; stitches everything together.
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
        main.setTitle("Assignment 01");
        main.setVisible(true);
    }

    public Main() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu actionMenu = new JMenu("Action");
        JMenu helpMenu = new JMenu("Help");

        ToolBarListener tbListener = new ToolBarListener();

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(tbListener);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(tbListener);
        JMenuItem actionMenuItem = new JMenuItem("Action");
        actionMenuItem.addActionListener(tbListener);
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(tbListener);

        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        actionMenu.add(actionMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(actionMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        FileGrid fileGrid = new FileGrid();
        Blackboard.fileGrid = fileGrid;
        add(fileGrid, BorderLayout.WEST);

        StatusBar statusBar = new StatusBar();
        Blackboard.statusBar = statusBar;
        add (statusBar, BorderLayout.SOUTH);

        GraphicsPanel graphics = new GraphicsPanel();
        add(graphics, BorderLayout.CENTER);
    }
}