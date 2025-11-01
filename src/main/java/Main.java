import javax.swing.*;
import java.awt.*;

/**
 * File that controls the program; stitches everything together.
 * Components self-register with Blackboard - Main just handles layout.
 * @author Aiden Rodriguez - GH - Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.3
 */
public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.pack();
            main.setSize(550, 450);
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            main.setTitle("Assignment 02");
            main.setVisible(true);
        });
    }

    public Main() {
        setupMenuBar();
        setupLayout();
    }

    private void setupMenuBar() {
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
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        TopBarPanel topBar = new TopBarPanel();
        GridPanel grid = new GridPanel(12, 7, 42);
        SelectedFileNamePanel bottom = new SelectedFileNamePanel();
        StatusBar statusBar = new StatusBar();

        add(topBar, BorderLayout.NORTH);

        JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.add(grid, BorderLayout.CENTER);
        add(gridHolder, BorderLayout.CENTER);

        JPanel southStack = new JPanel(new BorderLayout());
        southStack.add(bottom, BorderLayout.CENTER);
        southStack.add(statusBar, BorderLayout.SOUTH);
        add(southStack, BorderLayout.SOUTH);
    }
}