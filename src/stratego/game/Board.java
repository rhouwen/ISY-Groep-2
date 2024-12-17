package stratego.game;

import stratego.game.pieces.Bom;
import stratego.game.pieces.Piece;

public class Board {
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_COLS = 10;
    private static final String[][] board = new String[BOARD_ROWS][BOARD_COLS];
    // Initialize the board with empty spaces and water obstacles
    public static void initializeBoard() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                board[row][col] = " ";
            }
        }

        // Add water obstacles (example locations)
        setWater(4, 2); setWater(4, 3);
        setWater(5, 2); setWater(5, 3);
        setWater(4, 6); setWater(4, 7);
        setWater(5, 6); setWater(5, 7);
    }

    // Method to set water tiles
    private static void setWater(int row, int col) {
        board[row][col] = "~";
    }

    // Method to set a piece on the board
    public static void setPiece(int row, int col, Piece piece) {
        if (board[row][col].isEmpty())
            board[row][col] = piece.toString();
        else
            throw (IllegalMoveException("This tile contains " + board[row][col]));

    }

    // Check if a tile is water
    public boolean isWater(int row, int col) {
        return "~".equals(board[row][col]);
    }

    // Check if a tile is empty
    public boolean isEmpty(int row, int col) {
        return " ".equals(board[row][col]);
    }

    // Print the board
    public static void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int row = 0; row < BOARD_ROWS; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < BOARD_COLS; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        initializeBoard();
        printBoard();
        Piece bom = new Piece(); // deze moet later nog veranderd worden naar een Bom type. Bom class is nu nog geen Piece
        setPiece(8,0, bom);
    }
}