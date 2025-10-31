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
    private float[][] alphas;  // Transparency values for each cell
    private String[][] fileNames;  // Store filename for each cell
    private FileAnalyzer.AnalysisResult[][] analysisResults;  // Store analysis for tooltips

    public GridPanel(int cols, int rows, int cellSize) {
        this.cols = cols; this.rows = rows; this.cell = cellSize;
        setPreferredSize(new Dimension(cols * cellSize + 2, rows * cellSize + 2));
        pattern = makePattern(rows, cols);
        alphas = new float[rows][cols];
        fileNames = new String[rows][cols];
        analysisResults = new FileAnalyzer.AnalysisResult[rows][cols];

        // Initialize all alphas to 1.0 (fully opaque)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                alphas[r][c] = 1.0f;
                fileNames[r][c] = "";
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

                    // Calculate total control statements
                    int controlStatements = result.ifCount + result.switchCount +
                            result.forCount + result.whileCount;

                    // Determine color based on complexity
                    Color baseColor;
                    if (controlStatements > 10) {
                        baseColor = RED;
                    } else if (controlStatements > 5) {
                        baseColor = YELL;
                    } else {
                        baseColor = LIME;
                    }

                    pattern[r][c] = baseColor;

                    // Calculate transparency based on line count
                    if (maxLines == 0) {
                        alphas[r][c] = 0.0f;
                    } else {
                        alphas[r][c] = (float) result.lineCount / maxLines;
                    }

                    // Store filename and analysis result
                    this.fileNames[r][c] = names[fileIndex];
                    this.analysisResults[r][c] = result;

                    fileIndex++;
                } else {
                    // No file for this cell, make it white and fully transparent
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

                // Set color with alpha transparency
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

                // Draw grid lines
                g2.setColor(Color.BLACK);
                if (r == rows - 1) {
                    g2.drawLine(x, y + cell, x + cell, y + cell);
                }
                if (c == cols - 1) {
                    g2.drawLine(x + cell, y, x + cell, y + cell);
                }
                if (r == 0) g2.drawLine(x, y, x + cell, y);
                if (c == 0) g2.drawLine(x, y, x, y + cell);

                // Draw selection highlight
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
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                p[r][c] = LIME;

        for (int c = cols - 4; c < cols; c++) p[rows - 1][c] = WHITE;
        p[rows - 2][cols - 3] = WHITE; p[rows - 2][cols - 2] = WHITE;

        int[][] yell = {{0,2},{0,3},{0,10},{1,0},{1,1},{1,10},{3,1},{3,2},{3,10},{5,1},{6,1},{6,10}};
        for (int[] rc : yell) p[rc[0]][rc[1]] = YELL;

        int[][] reds = {{0,0},{1,2},{1,3},{1,4},{2,0},{2,4},{2,5},{2,6},{3,0},{3,5},{4,0},{4,5},{5,0}};
        for (int[] rc : reds) p[rc[0]][rc[1]] = RED;

        return p;
    }
}