package stratego.gui;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class multiplayerMove {
    // Constants for piece ranks and priorities
    private static final int FLAG_CAPTURE_PRIORITY = 1000;
    private static final int MINER_ATTACK_PRIORITY = 90;
    private static final int SPY_MARSHAL_ATTACK_PRIORITY = 100;
    private static final int BASE_ATTACK_PRIORITY = 40;

    public static boolean makeMove(boardMP board, int startRow, int startCol, int endRow, int endCol, PrintWriter out) {
        // Validate coordinates
        if (!isValidPosition(startRow, startCol) || !isValidPosition(endRow, endCol)) {
            System.out.println("Invalid coordinates: move is out of bounds");
            return false;
        }

        // Check if there's a piece at start position
        if (!board.hasPiece(startRow, startCol)) {
            System.out.println("No piece at start position");
            return false;
        }

        // Verify we're moving our piece
        if (!board.getCellColor(startRow, startCol).equals(boardMP.PLAYER_COLOR)) {
            System.out.println("Cannot move opponent's piece");
            return false;
        }

        String piece = board.getCellText(startRow, startCol);

        // Validate piece and destination
        if (piece.isEmpty() || isWaterCell(board, endRow, endCol) ||
                piece.equals("BOMB") || piece.equals("FLAG")) {
            System.out.println("Invalid piece or destination");
            return false;
        }

        // Check opponent piece at destination
        if (!board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR) &&
                board.getCellText(endRow, endCol).equals("X")) {
            System.out.println("Cannot move to position occupied by opponent: " + endRow + "," + endCol);
            return false;
        }

        // Execute move based on piece type
        boolean validMove = piece.equals("SCOUT") ?
                moveScout(board, startRow, startCol, endRow, endCol) :
                moveRegularPiece(board, startRow, startCol, endRow, endCol);

        if (validMove) {
            out.println("move " + (startRow * 8 + startCol) + " " + (endRow * 8 + endCol));
            return true;
        }

        return false;
    }

    private static class Move {
        final int startRow, startCol, endRow, endCol;
        final int priority;

        Move(int startRow, int startCol, int endRow, int endCol, int priority) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.priority = priority;
        }
    }
    public static boolean makeStrategicMove(boardMP board, PrintWriter out, Set<String> previousMoves) {
        List<Move> strategicMoves = new ArrayList<>();
        List<Move> allPossibleMoves = new ArrayList<>();

        // First collect all pieces and their possible moves
        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                if (!board.hasPiece(startRow, startCol) ||
                        !board.getCellColor(startRow, startCol).equals(boardMP.PLAYER_COLOR)) {
                    continue;
                }

                String piece = board.getCellText(startRow, startCol);
                if (piece.equals("BOMB") || piece.equals("FLAG")) {
                    continue;
                }

                // Add strategic moves with priorities
                addPossibleMoves(board, startRow, startCol, piece, strategicMoves, previousMoves);

                // Also collect basic valid moves for fallback
                addBasicMoves(board, startRow, startCol, piece, allPossibleMoves, previousMoves);
            }
        }

        // Try strategic moves first
        if (!strategicMoves.isEmpty()) {
            strategicMoves.sort((m1, m2) -> m2.priority - m1.priority);
            for (Move move : strategicMoves) {
                if (makeMove(board, move.startRow, move.startCol, move.endRow, move.endCol, out)) {
                    String moveString = move.startRow + "," + move.startCol + "->" + move.endRow + "," + move.endCol;
                    previousMoves.add(moveString);
                    System.out.println("Making strategic move: " + moveString + " with priority " + move.priority);
                    return true;
                }
            }
        }

        // Fall back to any valid move if no strategic moves are available
        if (!allPossibleMoves.isEmpty()) {
            System.out.println("Attempting random valid move as fallback...");
            java.util.Collections.shuffle(allPossibleMoves);
            for (Move move : allPossibleMoves) {
                if (makeMove(board, move.startRow, move.startCol, move.endRow, move.endCol, out)) {
                    String moveString = move.startRow + "," + move.startCol + "->" + move.endRow + "," + move.endCol;
                    previousMoves.add(moveString);
                    System.out.println("Made random move: " + moveString);
                    return true;
                }
            }
        }

        System.out.println("No valid moves found!");
        return false;
    }

    private static void addBasicMoves(boardMP board, int startRow, int startCol, String piece,
                                      List<Move> moves, Set<String> previousMoves) {
        if (piece.equals("SCOUT")) {
            // Scout's long-range moves
            for (int i = 0; i < 8; i++) {
                if (i != startRow && isValidScoutMove(board, startRow, startCol, i, startCol)) {
                    addMoveIfNew(startRow, startCol, i, startCol, 1, moves, previousMoves);
                }
                if (i != startCol && isValidScoutMove(board, startRow, startCol, startRow, i)) {
                    addMoveIfNew(startRow, startCol, startRow, i, 1, moves, previousMoves);
                }
            }
        } else {
            // Regular one-square moves
            int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
            for (int[] dir : directions) {
                int newRow = startRow + dir[0];
                int newCol = startCol + dir[1];
                if (isValidPosition(newRow, newCol) && !isWaterCell(board, newRow, newCol)) {
                    addMoveIfNew(startRow, startCol, newRow, newCol, 1, moves, previousMoves);
                }
            }
        }
    }
    private static void addPossibleMoves(boardMP board, int startRow, int startCol, String piece,
                                         List<Move> possibleMoves, Set<String> previousMoves) {
        if (piece.equals("SCOUT")) {
            addScoutMoves(board, startRow, startCol, possibleMoves, previousMoves);
            return;
        }

        // Regular pieces: check all adjacent squares
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : directions) {
            int endRow = startRow + dir[0];
            int endCol = startCol + dir[1];

            if (!isValidPosition(endRow, endCol) || isWaterCell(board, endRow, endCol)) {
                continue;
            }

            // Calculate priority and add move if valid
            int priority = calculateMovePriority(board, piece, startRow, startCol, endRow, endCol);
            if (priority > 0) {
                addMoveIfNew(startRow, startCol, endRow, endCol, priority, possibleMoves, previousMoves);
            }
        }
    }

    private static void addMoveIfNew(int startRow, int startCol, int endRow, int endCol,
                                     int priority, List<Move> moves, Set<String> previousMoves) {
        String moveString = startRow + "," + startCol + "->" + endRow + "," + endCol;
        if (!previousMoves.contains(moveString)) {
            moves.add(new Move(startRow, startCol, endRow, endCol, priority));
        }
    }

    private static void addScoutMoves(boardMP board, int startRow, int startCol,
                                      List<Move> possibleMoves, Set<String> previousMoves) {
        // Horizontal moves
        for (int col = 0; col < 8; col++) {
            if (col != startCol && isValidScoutMove(board, startRow, startCol, startRow, col)) {
                int priority = calculateMovePriority(board, "SCOUT", startRow, startCol, startRow, col);
                if (priority > 0) {
                    addMoveIfNew(startRow, startCol, startRow, col, priority, possibleMoves, previousMoves);
                }
            }
        }

        // Vertical moves
        for (int row = 0; row < 8; row++) {
            if (row != startRow && isValidScoutMove(board, startRow, startCol, row, startCol)) {
                int priority = calculateMovePriority(board, "SCOUT", startRow, startCol, row, startCol);
                if (priority > 0) {
                    addMoveIfNew(startRow, startCol, row, startCol, priority, possibleMoves, previousMoves);
                }
            }
        }
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

        // Check destination
        return board.isCellEmpty(endRow, endCol) ||
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR);
    }
    private static int calculateMovePriority(boardMP board, String piece, int startRow, int startCol, int endRow, int endCol) {
        // Early validation
        if (!isValidPosition(startRow, startCol) || !isValidPosition(endRow, endCol) ||
                isWaterCell(board, endRow, endCol)) {
            return 0;
        }

        int priority = 0;
        boolean isForwardMove = endRow < startRow;
        boolean isAttackMove = !board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR);

        // Miner strategy
        if (piece.equals("MINER")) {
            priority += calculateMinerPriority(board, startRow, endRow, endCol, isForwardMove, isAttackMove);
        }
        // Scout strategy
        // In calculateMovePriority method, update these lines:
        else if (piece.equals("SCOUT")) {
            priority += calculateScoutPriority(startRow, startCol, endRow, endCol, isForwardMove);
        }
// Strong pieces strategy
        else if (piece.equals("MARSHAL") || piece.equals("GENERAL")) {
            priority += calculateStrongPiecePriority(board, startRow, startCol, endRow, endCol, isForwardMove);
        }
        // Spy strategy
        else if (piece.equals("SPY")) {
            priority += calculateSpyPriority(board, endRow, endCol);
        }

        // Global position evaluation
        priority += evaluatePosition(board, startRow, endRow, endCol, isForwardMove, isAttackMove);

        return priority;
    }

    private static int calculateMinerPriority(boardMP board, int startRow, int endRow, int endCol,
                                              boolean isForwardMove, boolean isAttackMove) {
        int priority = 0;

        // Aggressive miner movement towards likely bomb locations
        if (isForwardMove) {
            priority += 50;
            if (endRow < 3) {  // First three rows priority
                priority += 30;
            }
            if (isPotentialBombLocation(board, endRow, endCol)) {
                priority += 40;
            }
        }

        // High priority for any attack (might be a bomb)
        if (isAttackMove) {
            priority += MINER_ATTACK_PRIORITY;
        }

        return priority;
    }

    private static int calculateScoutPriority(int startRow, int startCol, int endRow, int endCol, boolean isForwardMove) {
        int priority = 0;
        int distance = Math.abs(startRow - endRow) + Math.abs(startCol - endCol);

        if (isForwardMove) {
            priority += 40 + distance * 5;  // Reward longer forward moves
            if (endRow < 2) {  // Deep penetration bonus
                priority += 30;
            }
        }

        // Prefer central columns for scouts
        if (endCol >= 2 && endCol <= 5) {
            priority += 15;
        }

        return priority;
    }

    private static int calculateStrongPiecePriority(boardMP board, int startRow, int startCol, int endRow, int endCol, boolean isForwardMove) {
        int priority = 0;

        if (isForwardMove) {
            if (hasMinerAhead(board, endRow)) {
                priority += 40;  // Move forward if miners are ahead
            } else {
                priority += 10;  // Careful advance without miner support
            }
        }

        // Strong pieces should protect weaker ones
        if (isNearAlly(board, endRow, endCol)) {
            priority += 20;
        }

        return priority;
    }

    private static int calculateSpyPriority(boardMP board, int endRow, int endCol) {
        int priority = 0;

        // High priority for potential Marshal attacks
        if (!board.isCellEmpty(endRow, endCol) &&
                board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR)) {
            priority += SPY_MARSHAL_ATTACK_PRIORITY;
        }

        // Keep spy safe until needed
        if (isUnderThreat(board, endRow, endCol)) {
            priority -= 30;
        }

        return priority;
    }

    private static int evaluatePosition(boardMP board, int startRow, int endRow, int endCol,
                                        boolean isForwardMove, boolean isAttackMove) {
        int priority = 0;

        // Forward movement bonus
        if (isForwardMove) {
            priority += 20 + (startRow - endRow) * 5;
        }

        // Attack evaluation
        if (isAttackMove) {
            priority += BASE_ATTACK_PRIORITY;
            if (endRow < 2) {  // Attacking pieces near opponent's back row
                priority += 25;
            }
        }

        // Position control
        if (endCol >= 2 && endCol <= 5) {  // Central control
            priority += 10;
        }

        // Avoid threats
        if (isUnderThreat(board, endRow, endCol)) {
            priority -= 20;
        }

        return priority;
    }
    /*
     * Last modified: 2025-01-25 12:14:37 UTC
     * Modified by: rhouwen
     */

    private static boolean moveScout(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Must be in straight line
        if (startRow != endRow && startCol != endCol) {
            System.out.println("Scout must move in straight line");
            return false;
        }

        // Check path
        if (startRow == endRow) {
            int step = Integer.compare(endCol, startCol);
            for (int col = startCol + step; col != endCol; col += step) {
                if (!board.isCellEmpty(startRow, col) || isWaterCell(board, startRow, col)) {
                    System.out.println("Path blocked at " + startRow + "," + col);
                    return false;
                }
            }
        } else {
            int step = Integer.compare(endRow, startRow);
            for (int row = startRow + step; row != endRow; row += step) {
                if (!board.isCellEmpty(row, startCol) || isWaterCell(board, row, startCol)) {
                    System.out.println("Path blocked at " + row + "," + startCol);
                    return false;
                }
            }
        }

        return executeMove(board, startRow, startCol, endRow, endCol);
    }

    private static boolean moveRegularPiece(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        // Validate one-square movement
        if (Math.abs(startRow - endRow) + Math.abs(startCol - endCol) != 1) {
            System.out.println("Regular pieces can only move one square");
            return false;
        }

        return executeMove(board, startRow, startCol, endRow, endCol);
    }

    private static boolean executeMove(boardMP board, int startRow, int startCol, int endRow, int endCol) {
        if (!board.isCellEmpty(endRow, endCol)) {
            if (board.getCellColor(endRow, endCol).equals(boardMP.PLAYER_COLOR)) {
                System.out.println("Cannot move onto own piece");
                return false;
            }
            if (board.getCellColor(endRow, endCol).equals(boardMP.OPPONENT_COLOR) &&
                    board.getCellText(endRow, endCol).equals("X")) {
                System.out.println("Cannot move onto opponent's unknown piece");
                return false;
            }
        }

        String movingPiece = board.getCellText(startRow, startCol);
        updateCellsAfterMove(board, startRow, startCol, endRow, endCol, movingPiece);
        return true;
    }

    private static void updateCellsAfterMove(boardMP board, int startRow, int startCol, int endRow, int endCol, String piece) {
        boardMP.updateCell(endRow, endCol, piece, boardMP.PLAYER_COLOR);
        boardMP.removePiece(startRow, startCol);
        System.out.println("Moved piece " + piece + " from (" + startRow + "," + startCol + ") to (" + endRow + "," + endCol + ")");
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private static boolean isWaterCell(boardMP board, int row, int col) {
        return board.getCellColor(row, col).equals(boardMP.WATER_COLOR);
    }

    private static boolean isPotentialBombLocation(boardMP board, int row, int col) {
        return row < 2 || (row < 3 && (col == 3 || col == 4));
    }

    private static boolean isUnderThreat(boardMP board, int row, int col) {
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        int threatCount = 0;

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol) &&
                    !isWaterCell(board, newRow, newCol) &&
                    board.getCellColor(newRow, newCol).equals(boardMP.OPPONENT_COLOR)) {
                threatCount++;
            }
        }

        return threatCount > 1;
    }

    private static boolean isNearAlly(boardMP board, int row, int col) {
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

    private static boolean hasMinerAhead(boardMP board, int row) {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < 8; c++) {
                if (board.getCellColor(r, c).equals(boardMP.PLAYER_COLOR) &&
                        board.getCellText(r, c).equals("MINER")) {
                    return true;
                }
            }
        }
        return false;
    }
}
