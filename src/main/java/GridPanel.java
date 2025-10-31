import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GridPanel extends JComponent {

    public interface CellClickListener { void onClick(int row, int col); }

    private final int cols, rows, cell;
    private final int pad = 2;
    private Point selected = null;
    private CellClickListener listener;

    private static final Color LIME = new Color(170,235,170);
    private static final Color YELL = new Color(250,245,160);
    private static final Color RED  = new Color(225,35,35);
    private static final Color WHITE = new Color(255,255,255);

    private Color[][] pattern;
    private float[][] alphas;
    private String[][] fileNames;
    private FileAnalyzer.AnalysisResult[][] analysisResults;

    public GridPanel(int cols, int rows, int cellSize) {
        this.cols = cols; this.rows = rows; this.cell = cellSize;
        setPreferredSize(new Dimension(cols * cellSize + 2, rows * cellSize + 2));
        pattern = makePattern(rows, cols);
        alphas = new float[rows][cols];
        fileNames = new String[rows][cols];
        analysisResults = new FileAnalyzer.AnalysisResult[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                alphas[r][c] = 1.0f;
                fileNames[r][c] = "";
                pattern[r][c] = WHITE;
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int c = (e.getX() - 1) / cell;
                int r = (e.getY() - 1) / cell;
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    Point p = new Point(c, r);
                    selected = p;
                    if (listener != null) listener.onClick(r, c);
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
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
        });
    }

    public void setOnCellClicked(CellClickListener l) { this.listener = l; }

    public String getFileNameAt(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return fileNames[row][col];
        }
        return "";
    }

    /**
     * Update the grid based on file analysis results.
     * Maps each file to a cell in row-major order.
     */
    public void updateFromFiles(FileAnalyzer.AnalysisResult[] results, long maxLines, String[] names) {
        pattern = new Color[rows][cols];
        alphas = new float[rows][cols];
        this.fileNames = new String[rows][cols];
        this.analysisResults = new FileAnalyzer.AnalysisResult[rows][cols];

        int fileIndex = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (fileIndex < results.length) {
                    FileAnalyzer.AnalysisResult result = results[fileIndex];

                    int controlStatements = result.ifCount + result.switchCount +
                            result.forCount + result.whileCount;

                    Color baseColor;
                    if (controlStatements > 10) {
                        baseColor = RED;
                    } else if (controlStatements > 5) {
                        baseColor = YELL;
                    } else {
                        baseColor = LIME;
                    }

                    pattern[r][c] = baseColor;

                    if (maxLines == 0) {
                        alphas[r][c] = 0.0f;
                    } else {
                        alphas[r][c] = (float) result.lineCount / maxLines;
                    }

                    this.fileNames[r][c] = names[fileIndex];
                    this.analysisResults[r][c] = result;

                    fileIndex++;
                } else {
                    pattern[r][c] = WHITE;
                    alphas[r][c] = 0.0f;
                    this.fileNames[r][c] = "";
                    this.analysisResults[r][c] = null;
                }
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = cols * cell + 2, h = rows * cell + 2;

        g2.setColor(new Color(245,245,245));
        g2.fillRoundRect(0,0,w,h,12,12);
        g2.setColor(new Color(210,210,210));
        g2.drawRoundRect(0,0,w-1,h-1,12,12);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
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

                g2.setColor(Color.BLACK);
                if (r == rows - 1) {
                    g2.drawLine(x, y + cell, x + cell, y + cell);
                }
                if (c == cols - 1) {
                    g2.drawLine(x + cell, y, x + cell, y + cell);
                }
                if (r == 0) g2.drawLine(x, y, x + cell, y);
                if (c == 0) g2.drawLine(x, y, x, y + cell);

                if (selected != null && selected.equals(new Point(c, r))) {
                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(new Color(30,144,255,180));
                    g2.drawRect(x + 2, y + 2, cell - 4, cell - 4);
                    g2.setStroke(new BasicStroke(1f));
                }
            }
        }
        g2.dispose();
    }


    private Color[][] makePattern(int rows, int cols) {
        Color[][] p = new Color[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                p[r][c] = WHITE;
            }
        }
        return p;
    }
}