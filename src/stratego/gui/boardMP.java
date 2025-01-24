package stratego.gui;

import javax.swing.*;
import java.awt.*;

public class boardMP extends JPanel {
    private final int rows = 8;
    private final int cols = 8;
    private static JButton[][] cells;

    // Centralized color constants
    public static final Color PLAYER_COLOR = Color.RED;      // Our pieces
    public static final Color OPPONENT_COLOR = Color.GRAY;   // Enemy pieces
    public static final Color EMPTY_COLOR = new Color(34, 87, 25); // Empty cells (custom green)
    public static final Color WATER_COLOR = Color.BLUE;      // Water cells

    public boardMP() {
        setLayout(new GridLayout(rows, cols));
        cells = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setLayout(new BorderLayout());

                if (isWaterTile(row, col)) {
                    cell.setBackground(WATER_COLOR);
                    cell.setEnabled(false);
                } else {
                    cell.setBackground(EMPTY_COLOR);
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                addNumberToCell(cell, row, col);
                cells[row][col] = cell;
                add(cell);
            }
        }
    }

    private boolean isWaterTile(int row, int col) {
        return (row == 3 || row == 4) && (col == 2 || col == 5);
    }

    private void addNumberToCell(JButton cell, int row, int col) {
        JLabel numberLabel = new JLabel(String.valueOf(row * cols + col));
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        numberLabel.setForeground(Color.BLACK);
        cell.add(numberLabel, BorderLayout.SOUTH);
    }

    public static void updateCell(int row, int col, String text, Color color) {
        cells[row][col].setText(text);
        cells[row][col].setBackground(color);
    }

    public boolean isCellEmpty(int row, int col) {
        // Add bounds checking
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return false;  // Consider out-of-bounds cells as non-empty
        }
        return cells[row][col].getText().isEmpty();
    }

    public String getCellText(int row, int col) {
        return cells[row][col].getText();
    }

    public Color getCellColor(int row, int col) {
        return cells[row][col].getBackground();
    }

    public void resetBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!isWaterTile(row, col)) {
                    cells[row][col].setText("");
                    cells[row][col].setBackground(EMPTY_COLOR);
                }
            }
        }
    }
    public static void removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].setText("");
            cells[row][col].setBackground(EMPTY_COLOR);
        }
    }

    // Add helper method to check position validity
    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    // Add method to check if a cell has a valid piece
    public boolean hasPiece(int row, int col) {
        return !cells[row][col].getText().isEmpty() &&
                cells[row][col].getBackground() != WATER_COLOR;
    }

    public static void clearCell(int row, int col) {
        cells[row][col].setText("");
        cells[row][col].setBackground(EMPTY_COLOR);
    }
}