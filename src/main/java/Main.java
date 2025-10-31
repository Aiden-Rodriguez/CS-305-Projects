import javax.swing.*;
import java.awt.*;

/**
 * File that controls the program; stitches everything together.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.1
 */

public class Main extends JFrame{

    private final TopBarPanel topBar = new TopBarPanel();
    private final GridPanel grid = new GridPanel(12, 7, 42);
    private final SelectedFileNamePanel bottom = new SelectedFileNamePanel();

    public static void main(String[] args) {
        Main main = new Main();
        main.pack();
        main.setSize(550, 450);
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

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);

        JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.add(grid, BorderLayout.CENTER);
        add(gridHolder, BorderLayout.CENTER);

        StatusBar statusBar = new StatusBar();
        JPanel southStack = new JPanel(new BorderLayout());
        southStack.add(bottom, BorderLayout.CENTER);
        southStack.add(statusBar, BorderLayout.SOUTH);
        add(southStack, BorderLayout.SOUTH);

        // Test with: https://github.com/CSC3100/Tool-Maven/tree/main
        // https://github.com/Aiden-Rodriguez/CS-305-Projects/tree/main/src/main/java
        grid.setOnCellClicked((row, col) -> {
            String fileName = grid.getFileNameAt(row, col);
            if (fileName != null && !fileName.isEmpty()) {
                bottom.setSelectedName(fileName);
            } else {
                bottom.setSelectedName("(empty)");
            }
        });

        Blackboard bb = Blackboard.getInstance();
        bb.setStatusBar(statusBar);
        statusBar.registerWithBlackboard();
        bb.setGrid(grid);
    }
}