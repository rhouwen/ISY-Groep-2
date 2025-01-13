package stratego.gui;

import stratego.game.Board;

import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {

    private final int rows = 10;
    private final int cols = 10;
    private static JButton[][] cells;
    Color customGreen = new Color(34, 87, 25);

    public GUI(Board board) {
        setLayout(new GridLayout(rows, cols));
        cells = new JButton[rows][cols];

        // Initialiseer de ClickHandler met het Board-object
        ClickHandler.getInstance(board);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();

                if (isWaterTile(row, col)) {
                    cell.setBackground(Color.BLUE);
                    cell.setEnabled(false);
                }
                else {
                    cell.setBackground(customGreen);
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Voeg een ActionListener toe die de ClickHandler aanroept
                int finalRow = row;
                int finalCol = col;
                cell.addActionListener(e -> ClickHandler.getInstance().handleCellClick(finalRow, finalCol));

                cells[row][col] = cell;
                add(cell);
            }
        }
    }

    private boolean isWaterTile(int row, int col) {
        // De watertegels
        return (row == 4 || row == 5) && ((col == 2 || col == 3) || (col == 6 || col == 7));
    }

    public static void updateCell(int row, int col, String text, Color color) {
        cells[row][col].setText(text);
        cells[row][col].setBackground(color);
    }

    public void resetBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col].setText("");
                cells[row][col].setBackground(Color.GREEN);
            }
        }
    }
}
