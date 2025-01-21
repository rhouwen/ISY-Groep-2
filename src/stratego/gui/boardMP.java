package stratego.gui;

import javax.swing.*;
import java.awt.*;

public class boardMP extends JPanel {

    private final int rows = 8;
    private final int cols = 8;
    private static JButton[][] cells;
    public Color customGreen = new Color(34, 87, 25);

    public boardMP() {
        setLayout(new GridLayout(rows, cols));
        cells = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setLayout(new BorderLayout());

                if (isWaterTile(row, col)) {
                    cell.setBackground(Color.BLUE);
                    cell.setEnabled(false);
                } else {
                    cell.setBackground(customGreen);
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
                cells[row][col].setText("");
                cells[row][col].setBackground(Color.GREEN);
            }
        }
    }
}