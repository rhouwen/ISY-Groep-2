package stratego.gui;

import stratego.game.Board;

import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {

    private final int rows = 10;
    private final int cols = 10;
    private static JButton[][] cells;

    public GUI(Board board) {
        setLayout(new GridLayout(rows, cols));
        cells = new JButton[rows][cols];

        // Initialiseer de ClickHandler met het Board-object
        ClickHandler.getInstance(board);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setBackground(Color.GREEN);
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
