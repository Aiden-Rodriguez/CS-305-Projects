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

    private final Color[][] pattern;

    public GridPanel(int cols, int rows, int cellSize) {
        this.cols = cols; this.rows = rows; this.cell = cellSize;
        setPreferredSize(new Dimension(cols * cellSize + 2, rows * cellSize + 2));
        pattern = makePattern(rows, cols);

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
    }

    public void setOnCellClicked(CellClickListener l) { this.listener = l; }

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

                g2.setColor(pattern[r][c]);
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
