package stratego.gui;

import java.awt.Color;

public class multiplayerMove {

    public static boolean makeMove(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        String piece = board.getCellText(startRow, startCol);
        String targetPiece = board.getCellText(endRow, endCol);

        if (piece.isEmpty() || (!targetPiece.isEmpty() && !isAttackValid(piece, targetPiece))) {
            return false; // Invalid move or attack
        }

        switch (piece) {
            case "MARSHAL":
            case "GENERAL":
            case "MINER":
            case "SPY":
                return moveOneSquare(board, startRow, startCol, endRow, endCol);
            case "SCOUT":
                return moveScout(board, startRow, startCol, endRow, endCol);
            case "BOMB":
            case "FLAG":
                return false; // Cannot move
            default:
                return false; // Unknown piece
        }
    }

    private static boolean moveOneSquare(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        if (Math.abs(startRow - endRow) + Math.abs(startCol - endCol) == 1) {
            boardMP.updateCell(endRow, endCol, board.getCellText(startRow, startCol), Color.RED);
            boardMP.updateCell(startRow, startCol, "", board.customGreen);
            return true;
        }
        return false;
    }

    private static boolean moveScout(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow) {
            int step = (endCol > startCol) ? 1 : -1;
            for (int col = startCol + step; col != endCol; col += step) {
                if (!board.isCellEmpty(startRow, col)) {
                    return false; // Path blocked
                }
            }
            boardMP.updateCell(endRow, endCol, board.getCellText(startRow, startCol), Color.RED);
            boardMP.updateCell(startRow, startCol, "", board.customGreen);
            return true;
        } else if (startCol == endCol) {
            int step = (endRow > startRow) ? 1 : -1;
            for (int row = startRow + step; row != endRow; row += step) {
                if (!board.isCellEmpty(row, startCol)) {
                    return false; // Path blocked
                }
            }
            boardMP.updateCell(endRow, endCol, board.getCellText(startRow, startCol), Color.RED);
            boardMP.updateCell(startRow, startCol, "", board.customGreen);
            return true;
        }
        return false;
    }

    private static boolean isAttackValid(String attacker, String defender) {
        // Implement the attack rules based on Stratego rules
        switch (attacker) {
            case "MARSHAL":
                return !defender.equals("BOMB") && !defender.equals("FLAG");
            case "GENERAL":
                return !defender.equals("MARSHAL") && !defender.equals("BOMB") && !defender.equals("FLAG");
            case "MINER":
                return defender.equals("BOMB") || defender.equals("FLAG");
            case "SPY":
                return defender.equals("MARSHAL");
            case "SCOUT":
                return !defender.equals("BOMB") && !defender.equals("FLAG");
            default:
                return false;
        }
    }
}