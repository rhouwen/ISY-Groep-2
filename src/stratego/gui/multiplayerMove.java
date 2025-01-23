package stratego.gui;

import java.io.PrintWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class multiplayerMove {
    public static boolean makeMove(boardMP board, int startRow, int startCol, int endRow, int endCol, PrintWriter out) {
        // First check if there's actually a piece to move
        if (!board.hasPiece(startRow, startCol)) {
            System.out.println("No piece at start position");
            return false;
        }
        if (!isValidPosition(startRow, startCol) || !isValidPosition(endRow, endCol)) {
            System.out.println("Move is outside board boundaries");
            return false;
        }

        // Verify we're moving our piece (must be red)
        if (!board.getCellColor(startRow, startCol).equals(boardMP.PLAYER_COLOR)) {
            System.out.println("Cannot move opponent's piece");
            return false;
        }

        String piece = board.getCellText(startRow, startCol);
        String targetPiece = board.getCellText(endRow, endCol);

        // Basic validation checks
        if (piece.isEmpty() ||
                isWaterCell(board, endRow, endCol) ||
                piece.equals("BOMB") ||
                piece.equals("FLAG")) {
            System.out.println("Invalid piece or destination");
            return false;
        }

        // Check if target cell has our piece
        if (!board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.PLAYER_COLOR)) {
            System.out.println("Cannot capture own piece");
            return false;
        }

        boolean validMove = false;
        if (piece.equals("SCOUT")) {
            validMove = moveScout(board, startRow, startCol, endRow, endCol);
        } else {
            validMove = moveRegularPiece(board, startRow, startCol, endRow, endCol);
        }

        if (validMove) {
            int startIndex = startRow * 8 + startCol;
            int endIndex = endRow * 8 + endCol;
            out.println("move " + startIndex + " " + endIndex);
            return true;
        }

        return false;
    }

    private static class Move {
        int startRow, startCol, endRow, endCol;
        int priority;

        Move(int startRow, int startCol, int endRow, int endCol, int priority) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.priority = priority;
        }
    }

    public static boolean makeStrategicMove(boardMP board, PrintWriter out, Set<String> previousMoves) {
        List<Move> possibleMoves = new ArrayList<>();

        // Verzamel alle mogelijke zetten met hun prioriteiten
        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                if (board.getCellColor(startRow, startCol).equals(boardMP.PLAYER_COLOR)) {
                    String piece = board.getCellText(startRow, startCol);

                    // Controleer alle mogelijke bestemmingen
                    for (int endRow = 0; endRow < 8; endRow++) {
                        for (int endCol = 0; endCol < 8; endCol++) {
                            String moveString = startRow + "," + startCol + "->" + endRow + "," + endCol;
                            if (!previousMoves.contains(moveString)) {
                                int priority = calculateMovePriority(board, piece, startRow, startCol, endRow, endCol);
                                if (priority > 0) {
                                    possibleMoves.add(new Move(startRow, startCol, endRow, endCol, priority));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sorteer zetten op prioriteit (hoogste eerst)
        possibleMoves.sort((m1, m2) -> m2.priority - m1.priority);

        // Probeer de zetten uit te voeren, beginnend met de hoogste prioriteit
        for (Move move : possibleMoves) {
            if (makeMove(board, move.startRow, move.startCol, move.endRow, move.endCol, out)) {
                String moveString = move.startRow + "," + move.startCol + "->" + move.endRow + "," + move.endCol;
                previousMoves.add(moveString);
                return true;
            }
        }

        return false;
    }

    private static int calculateMovePriority(boardMP board, String piece, int startRow, int startCol, int endRow, int endCol) {
        // Basis prioriteit berekening
        int priority = 0;
        String targetPiece = board.getCellText(endRow, endCol);

        // Controleer eerst of de zet überhaupt mogelijk is
        if (!isValidPosition(startRow, startCol) || !isValidPosition(endRow, endCol) ||
                isWaterCell(board, endRow, endCol) || piece.equals("BOMB") || piece.equals("FLAG")) {
            return 0;
        }

        // Prioriteiten voor verschillende situaties

        // 1. Vlag capture heeft hoogste prioriteit
        if (targetPiece.equals("FLAG")) {
            return 1000;
        }

        // 2. Voorkom dat stukken worden geslagen (defensief)
        if (isUnderThreat(board, startRow, startCol)) {
            priority += 50;
        }

        // 3. Sla vijandelijke stukken als we zeker weten dat we winnen
        if (!board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR)) {
            if (isAttackValid(piece, targetPiece)) {
                switch (piece) {
                    case "MARSHAL":
                        priority += 40;
                        break;
                    case "GENERAL":
                        priority += 35;
                        break;
                    case "COLONEL":
                        priority += 30;
                        break;
                    case "MAJOR":
                        priority += 25;
                        break;
                    case "MINER":
                        if (targetPiece.equals("BOMB")) priority += 45;
                        else priority += 20;
                        break;
                    case "SPY":
                        if (targetPiece.equals("MARSHAL")) priority += 45;
                        break;
                    default:
                        priority += 15;
                }
            }
        }

        // 4. Scout speciale bewegingen
        if (piece.equals("SCOUT")) {
            // Verkenning prioriteit
            if (board.isCellEmpty(endRow, endCol)) {
                priority += 10 + Math.abs(startRow - endRow) + Math.abs(startCol - endCol);
            }
        }

        // 5. Voorwaartse beweging voor aanval
        if (startRow > endRow && board.isCellEmpty(endRow, endCol)) {
            priority += 5;
        }

        // 6. Bescherm belangrijke stukken
        if (isNearAlly(board, endRow, endCol)) {
            priority += 3;
        }

        return priority;
    }

    private static boolean isUnderThreat(boardMP board, int row, int col) {
        // Check of er vijandelijke stukken naast staan
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol) &&
                    !isWaterCell(board, newRow, newCol) &&
                    board.getCellColor(newRow, newCol).equals(boardMP.OPPONENT_COLOR)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNearAlly(boardMP board, int row, int col) {
        // Check of er bevriende stukken naast staan
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol) &&
                    !isWaterCell(board, newRow, newCol) &&
                    board.getCellColor(newRow, newCol).equals(boardMP.PLAYER_COLOR)) {
                return true;
            }
        }
        return false;
    }

    private static void updateCellsAfterMove(boardMP board, int startRow, int startCol, int endRow, int endCol, String piece) {
        // Set the destination cell
        boardMP.updateCell(endRow, endCol, piece, boardMP.PLAYER_COLOR);
        // Use removePiece for the starting position
        boardMP.removePiece(startRow, startCol);
        System.out.println("Moved piece " + piece + " from (" + startRow + "," + startCol + ") to (" + endRow + "," + endCol + ")");
    }

    private static boolean moveScout(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Must move in straight line
        if (startRow != endRow && startCol != endCol) {
            System.out.println("Scout must move in straight line");
            return false;
        }

        // Check path for ANY pieces (including opponent pieces)
        if (startRow == endRow) { // Horizontal movement
            int minCol = Math.min(startCol, endCol);
            int maxCol = Math.max(startCol, endCol);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (!board.isCellEmpty(startRow, col)) {
                    System.out.println("Path blocked by piece at column " + col);
                    return false;
                }
                if (isWaterCell(board, startRow, col)) {
                    System.out.println("Cannot move through water at column " + col);
                    return false;
                }
            }
        } else { // Vertical movement
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (!board.isCellEmpty(row, startCol)) {
                    System.out.println("Path blocked by piece at row " + row);
                    return false;
                }
                if (isWaterCell(board, row, startCol)) {
                    System.out.println("Cannot move through water at row " + row);
                    return false;
                }
            }
        }

        // Check destination
        if (!board.isCellEmpty(endRow, endCol)) {
            // Can only attack opponent pieces
            if (board.getCellColor(endRow, endCol).equals(boardMP.PLAYER_COLOR)) {
                System.out.println("Cannot attack own piece");
                return false;
            }
            // Validate attack
            String targetPiece = board.getCellText(endRow, endCol);
            if (!isAttackValid("SCOUT", targetPiece)) {
                System.out.println("Invalid attack target for Scout");
                return false;
            }
        }

        String movingPiece = board.getCellText(startRow, startCol);
        updateCellsAfterMove(board, startRow, startCol, endRow, endCol, movingPiece);
        return true;
    }

    private static boolean moveRegularPiece(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Check if moving only one square
        if (Math.abs(startRow - endRow) + Math.abs(startCol - endCol) != 1) {
            System.out.println("Regular pieces can only move one square");
            return false;
        }

        // Check destination
        if (!board.isCellEmpty(endRow, endCol)) {
            // Can only attack opponent pieces
            if (board.getCellColor(endRow, endCol).equals(boardMP.PLAYER_COLOR)) {
                System.out.println("Cannot attack own piece");
                return false;
            }
            // Validate attack
            String targetPiece = board.getCellText(endRow, endCol);
            String attackerPiece = board.getCellText(startRow, startCol);
            if (!isAttackValid(attackerPiece, targetPiece)) {
                System.out.println("Invalid attack target for " + attackerPiece);
                return false;
            }
        }

        String movingPiece = board.getCellText(startRow, startCol);
        updateCellsAfterMove(board, startRow, startCol, endRow, endCol, movingPiece);
        return true;
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private static boolean isWaterCell(boardMP board, int row, int col) {
        return board.getCellColor(row, col).equals(boardMP.WATER_COLOR);
    }

    private static boolean isAttackValid(String attacker, String defender) {
        if (defender.equals("FLAG")) {
            return true;
        }

        switch (attacker) {
            case "MARSHAL": // Rank 10
                return !defender.equals("BOMB");
            case "GENERAL": // Rank 9
                return !defender.equals("MARSHAL") && !defender.equals("BOMB");
            case "COLONEL": // Rank 8
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") && !defender.equals("BOMB");
            case "MAJOR": // Rank 7
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") &&
                        !defender.equals("COLONEL") && !defender.equals("BOMB");
            case "CAPTAIN": // Rank 6
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") &&
                        !defender.equals("COLONEL") && !defender.equals("MAJOR") && !defender.equals("BOMB");
            case "LIEUTENANT": // Rank 5
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") &&
                        !defender.equals("COLONEL") && !defender.equals("MAJOR") &&
                        !defender.equals("CAPTAIN") && !defender.equals("BOMB");
            case "SERGEANT": // Rank 4
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") &&
                        !defender.equals("COLONEL") && !defender.equals("MAJOR") &&
                        !defender.equals("CAPTAIN") && !defender.equals("LIEUTENANT") && !defender.equals("BOMB");
            case "MINER": // Rank 3
                return true; // Miner can defuse bombs
            case "SCOUT": // Rank 2
                return !defender.equals("MARSHAL") && !defender.equals("GENERAL") &&
                        !defender.equals("COLONEL") && !defender.equals("MAJOR") &&
                        !defender.equals("CAPTAIN") && !defender.equals("LIEUTENANT") &&
                        !defender.equals("SERGEANT") && !defender.equals("MINER") && !defender.equals("BOMB");
            case "SPY": // Rank 1
                return defender.equals("MARSHAL"); // Spy can only defeat Marshal
            default:
                return false;
        }
    }
}