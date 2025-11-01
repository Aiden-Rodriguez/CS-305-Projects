import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Panel that displays a grid of cells representing files.
 * Handles visualization and user interaction with the grid.
 * Self-registers with Blackboard and listens for grid data updates.
 * @author Aiden Rodriguez - GH Aiden-Rodriguez
 * @author Brandon Powell - GH - Bpowell5184
 * @version 1.3
 */
public class GridPanel extends JComponent {

    private final int cols, rows, cell;
    private final int pad = 2;
    private Point selected = null;

    private static final Color GREEN = Color.GREEN;
    private static final Color YELLOW = Color.YELLOW;
    private static final Color RED  = Color.RED;
    private static final Color WHITE = Color.WHITE;

    private Color[][] pattern;
    private float[][] alphas;
    private String[][] fileNames;
    private FileAnalyzer.AnalysisResult[][] analysisResults;

    public GridPanel(int cols, int rows, int cellSize) {
        this.cols = cols;
        this.rows = rows;
        this.cell = cellSize;
        setPreferredSize(new Dimension(cols * cellSize + 2, rows * cellSize + 2));

        initializeGrid();
        setupMouseListeners();
        registerWithBlackboard();
    }

    private void initializeGrid() {
        pattern = new Color[rows][cols];
        alphas = new float[rows][cols];
        fileNames = new String[rows][cols];
        analysisResults = new FileAnalyzer.AnalysisResult[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                alphas[r][c] = 0.0f;
                fileNames[r][c] = "";
                pattern[r][c] = WHITE;
                analysisResults[r][c] = null;
            }
        }
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCellClick(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateTooltip(e);
            }
        });
    }

    /**
     * Self-registers with Blackboard during construction.
     * Subscribes to gridData updates.
     */
    private void registerWithBlackboard() {
        Blackboard bb = Blackboard.getInstance();

        bb.registerGrid(this);

        bb.addPropertyChangeListener("gridData", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Blackboard.GridData newData = (Blackboard.GridData) evt.getNewValue();
                if (newData != null && newData.results != null) {
                    SwingUtilities.invokeLater(() ->
                            updateFromFiles(newData.results, newData.maxLines, newData.names)
                    );
                }
            }
        });
    }

    private void handleCellClick(MouseEvent e) {
        int c = (e.getX() - 1) / cell;
        int r = (e.getY() - 1) / cell;
        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            selected = new Point(c, r);

            String fileName = fileNames[r][c];
            if (fileName != null && !fileName.isEmpty()) {
                Blackboard.getInstance().setSelectedFileName(fileName);
            } else {
                Blackboard.getInstance().setSelectedFileName("(empty)");
            }

            repaint();
        }
    }

    private void updateTooltip(MouseEvent e) {
        int c = (e.getX() - 1) / cell;
        int r = (e.getY() - 1) / cell;

        if (r >= 0 && r < rows && c >= 0 && c < cols) {
            if (fileNames[r][c] != null && !fileNames[r][c].isEmpty()) {
                FileAnalyzer.AnalysisResult result = analysisResults[r][c];
                if (result != null) {
                    int complexity = result.ifCount + result.switchCount +
                            result.forCount + result.whileCount;
                    String tooltip = String.format(
                            "<html><b>%s</b><br>Lines: %d<br>Complexity: %d</html>",
                            fileNames[r][c], result.lineCount, complexity
                    );
                    setToolTipText(tooltip);
                    return;
                }
            }
        }
        setToolTipText(null);
    }

    private void updateFromFiles(FileAnalyzer.AnalysisResult[] results, long maxLines, String[] names) {
        pattern = new Color[rows][cols];
        alphas = new float[rows][cols];
        this.fileNames = new String[rows][cols];
        this.analysisResults = new FileAnalyzer.AnalysisResult[rows][cols];

        int fileIndex = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (fileIndex < results.length) {
                    updateCellWithFile(r, c, results[fileIndex], names[fileIndex], maxLines);
                    fileIndex++;
                } else {
                    updateCellEmpty(r, c);
                }
            }
        }

        repaint();
    }

    private void updateCellWithFile(int row, int col, FileAnalyzer.AnalysisResult result,
                                    String fileName, long maxLines) {
        int controlStatements = result.ifCount + result.switchCount +
                result.forCount + result.whileCount;

        if (controlStatements > 10) {
            pattern[row][col] = RED;
        } else if (controlStatements > 5) {
            pattern[row][col] = YELLOW;
        } else {
            pattern[row][col] = GREEN;
        }

        if (maxLines == 0) {
            alphas[row][col] = 0.0f;
        } else {
            alphas[row][col] = (float) result.lineCount / maxLines;
        }

        this.fileNames[row][col] = fileName;
        this.analysisResults[row][col] = result;
    }

    private void updateCellEmpty(int row, int col) {
        pattern[row][col] = WHITE;
        alphas[row][col] = 0.0f;
        this.fileNames[row][col] = "";
        this.analysisResults[row][col] = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawCells(g2);

        g2.dispose();
    }

    private void drawBackground(Graphics2D g2) {
        int w = cols * cell + 2;
        int h = rows * cell + 2;

        g2.setColor(new Color(245, 245, 245));
        g2.fillRoundRect(0, 0, w, h, 12, 12);
        g2.setColor(new Color(210, 210, 210));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
    }

    private void drawCells(Graphics2D g2) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                drawCell(g2, r, c);
            }
        }
    }

    private void drawCell(Graphics2D g2, int r, int c) {
        int x = 1 + c * cell;
        int y = 1 + r * cell;

        Color baseColor = pattern[r][c];
        float alpha = alphas[r][c];
        Color colorWithAlpha = new Color(
                baseColor.getRed() / 255f,
                baseColor.getGreen() / 255f,
                baseColor.getBlue() / 255f,
                alpha
        );
        g2.setColor(colorWithAlpha);
        g2.fillRect(x + pad, y + pad, cell - pad * 2, cell - pad * 2);

        drawCellBorders(g2, x, y, r, c);

        if (selected != null && selected.equals(new Point(c, r))) {
            drawSelection(g2, x, y);
        }
    }

    private void drawCellBorders(Graphics2D g2, int x, int y, int r, int c) {
        g2.setColor(Color.BLACK);

        if (r == 0) {
            g2.drawLine(x, y, x + cell, y);
        }
        if (c == 0) {
            g2.drawLine(x, y, x, y + cell);
        }
        g2.drawLine(x, y + cell, x + cell, y + cell);
        g2.drawLine(x + cell, y, x + cell, y + cell);
    }

    private void drawSelection(Graphics2D g2, int x, int y) {
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(30, 144, 255, 180));
        g2.drawRect(x + 2, y + 2, cell - 4, cell - 4);
        g2.setStroke(new BasicStroke(1f));
    }
}