package stratego.gui;

public class GameRules {

    public static boolean isValidMove(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Validate coordinates
        if (!isValidPosition(startRow, startCol) || !isValidPosition(endRow, endCol)) {
            return false;
        }

        // Check if there's a piece at start position
        if (!board.hasPiece(startRow, startCol)) {
            return false;
        }

        // Verify we're moving our piece
        if (!board.getCellColor(startRow, startCol).equals(boardMP.PLAYER_COLOR)) {
            return false;
        }

        String piece = board.getCellText(startRow, startCol);

        // Validate piece and destination
        if (piece.isEmpty() || isWaterCell(board, endRow, endCol) ||
                piece.equals("BOMB") || piece.equals("FLAG")) {
            return false;
        }

        // Check opponent piece at destination
        if (!board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR) &&
                board.getCellText(endRow, endCol).equals("X")) {
            return false;
        }

        // Execute move based on piece type
        return piece.equals("SCOUT") ?
                isValidScoutMove(board, startRow, startCol, endRow, endCol) :
                isValidRegularMove(board, startRow, startCol, endRow, endCol);
    }

    private static boolean isValidScoutMove(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Must be in straight line
        if (startRow != endRow && startCol != endCol) {
            return false;
        }

        // Check path
        if (startRow == endRow) {
            int step = Integer.compare(endCol, startCol);
            for (int col = startCol + step; col != endCol; col += step) {
                if (!board.isCellEmpty(startRow, col) || isWaterCell(board, startRow, col)) {
                    return false;
                }
            }
        } else {
            int step = Integer.compare(endRow, startRow);
            for (int row = startRow + step; row != endRow; row += step) {
                if (!board.isCellEmpty(row, startCol) || isWaterCell(board, row, startCol)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isValidRegularMove(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Validate one-square movement
        return Math.abs(startRow - endRow) + Math.abs(startCol - endCol) == 1;
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private static boolean isWaterCell(boardMP board, int row, int col) {
        return board.getCellColor(row, col).equals(boardMP.WATER_COLOR);
    }
}