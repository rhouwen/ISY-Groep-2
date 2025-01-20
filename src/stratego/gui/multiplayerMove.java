package stratego.gui;

import java.awt.Color;
import java.io.PrintWriter;

public class multiplayerMove {

    public static boolean makeMove(boardMP board, int startRow, int startCol, int endRow, int endCol, PrintWriter out) {
        String piece = board.getCellText(startRow, startCol);
        String targetPiece = board.getCellText(endRow, endCol);

        if (piece.isEmpty() || (!targetPiece.isEmpty() && !isAttackValid(piece, targetPiece)) || isWaterCell(board, endRow, endCol)) {
            return false; // Invalid move, attack, or move to water
        }

        boolean validMove = false;
        switch (piece) {
            case "MARSHAL":
            case "GENERAL":
            case "MINER":
            case "SPY":
                validMove = moveOneSquare(board, startRow, startCol, endRow, endCol);
                break;
            case "SCOUT":
                validMove = moveScout(board, startRow, startCol, endRow, endCol);
                break;
            case "BOMB":
            case "FLAG":
                validMove = false; // Cannot move
                break;
            default:
                validMove = false; // Unknown piece
        }

        if (validMove) {
            int startIndex = startRow * 8 + startCol;
            int endIndex = endRow * 8 + endCol;
            System.out.println("Move made from " + startIndex + " to " + endIndex);
            out.println("move " + startIndex + " " + endIndex); // Corrected format
            return true; // Return immediately after making the move
        }

        return false;
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
                if (!board.isCellEmpty(startRow, col) || isWaterCell(board, startRow, col)) {
                    return false; // Path blocked or water cell
                }
            }
            boardMP.updateCell(endRow, endCol, board.getCellText(startRow, startCol), Color.RED);
            boardMP.updateCell(startRow, startCol, "", board.customGreen);
            return true;
        } else if (startCol == endCol) {
            int step = (endRow > startRow) ? 1 : -1;
            for (int row = startRow + step; row != endRow; row += step) {
                if (!board.isCellEmpty(row, startCol) || isWaterCell(board, row, startCol)) {
                    return false; // Path blocked or water cell
                }
            }
            boardMP.updateCell(endRow, endCol, board.getCellText(startRow, startCol), Color.RED);
            boardMP.updateCell(startRow, startCol, "", board.customGreen);
            return true;
        }
        return false;
    }

    private static boolean isWaterCell(boardMP board, int row, int col) {
        return board.getCellColor(row, col).equals(Color.BLUE); // Assuming water cells are blue
    }

    private static boolean isAttackValid(String attacker, String defender) {
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