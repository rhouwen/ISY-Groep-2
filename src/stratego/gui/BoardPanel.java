package stratego.gui;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel{

    private final int rows = 10;
    private final int cols = 10;
    private static JButton[][] cells;

    public BoardPanel() {

        setLayout(new GridLayout(rows, cols));
        cells = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setBackground(Color.GREEN);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[row][col] = cell;
                add(cell);
            }
        }
    }

    public static void updateCell(int row, int col, String text, Color color) {
        cells[row][col].setText(text);
        cells[row][col].setBackground(color);
    }

    public void resetBoard() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                cells[row][col].setText("");
                cells[row][col].setBackground(Color.GREEN);
            }
        }
    }
}
