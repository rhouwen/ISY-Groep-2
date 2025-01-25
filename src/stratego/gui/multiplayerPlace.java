package stratego.gui;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class multiplayerPlace {

    public static void placePieces(boardMP board) {
        List<multiplayerPieces.PieceType> pieces = multiplayerPieces.createPieces();

        // Clear the board first
        for (int row = 6; row <= 7; row++) {
            for (int col = 0; col < 8; col++) {
                boardMP.updateCell(row, col, "", boardMP.EMPTY_COLOR);
            }
        }

        // Strategic placement algorithm
        placeStrategically(board, pieces);
    }

    private static void placeStrategically(boardMP board, List<multiplayerPieces.PieceType> pieces) {
        // Separate pieces by type for strategic placement
        List<multiplayerPieces.PieceType> bombs = new ArrayList<>();
        List<multiplayerPieces.PieceType> scouts = new ArrayList<>();
        List<multiplayerPieces.PieceType> highRanks = new ArrayList<>();
        List<multiplayerPieces.PieceType> lowRanks = new ArrayList<>();
        multiplayerPieces.PieceType flag = null;

        // Categorize pieces
        for (multiplayerPieces.PieceType piece : pieces) {
            switch (piece) {
                case BOMB:
                    bombs.add(piece);
                    break;
                case SCOUT:
                    scouts.add(piece);
                    break;
                case FLAG:
                    flag = piece;
                    break;
                case MARSHAL:
                case GENERAL:
                    highRanks.add(piece);
                    break;
                default:
                    lowRanks.add(piece);
                    break;
            }
        }

        // Step 1: Place the flag on the bottom row in a strategic position
        int[] flagPositions = {
                7 * 8 + 1,  // One space from left corner
                7 * 8 + 6   // One space from right corner
        };
        int flagPos = flagPositions[(int)(Math.random() * flagPositions.length)];
        int flagRow = 7; // Always bottom row
        int flagCol = flagPos % 8;
        boardMP.updateCell(flagRow, flagCol, flag.name(), boardMP.PLAYER_COLOR);

        // Step 2: Place bombs around the flag for protection
        placeBombsAroundFlag(board, bombs, flagRow, flagCol);

        // Step 3: Place high-ranking pieces in strategic positions
        placeHighRankingPieces(board, highRanks);

        // Step 4: Place scouts in strategic positions
        placeScouts(board, scouts);

        // Step 5: Fill remaining spaces with lower-ranking pieces
        fillRemainingSpaces(board, lowRanks);
    }

    private static void placeBombsAroundFlag(boardMP board, List<multiplayerPieces.PieceType> bombs, int flagRow, int flagCol) {
        // First, place bombs directly adjacent to the flag (prioritize protection)
        int[][] priorityDirections = {
                {-1,0},  // Above
                {-1,-1}, // Above-left
                {-1,1},  // Above-right
                {0,-1},  // Left
                {0,1}    // Right
        };

        // Place bombs in priority positions first
        for (int[] dir : priorityDirections) {
            int newRow = flagRow + dir[0];
            int newCol = flagCol + dir[1];

            if (isValidPosition(newRow, newCol) &&
                    board.isCellEmpty(newRow, newCol) &&
                    !bombs.isEmpty()) {
                boardMP.updateCell(newRow, newCol, bombs.remove(0).name(), boardMP.PLAYER_COLOR);
            }
        }

        // Place remaining bombs strategically on row 6
        int[] strategicBombCols = {1, 2, 5, 6}; // Strategic columns for remaining bombs
        for (int col : strategicBombCols) {
            if (board.isCellEmpty(6, col) && !bombs.isEmpty()) {
                boardMP.updateCell(6, col, bombs.remove(0).name(), boardMP.PLAYER_COLOR);
            }
        }

        // If any bombs remain, place them where possible
        while (!bombs.isEmpty()) {
            for (int col = 0; col < 8; col++) {
                if (board.isCellEmpty(6, col) && !bombs.isEmpty()) {
                    boardMP.updateCell(6, col, bombs.remove(0).name(), boardMP.PLAYER_COLOR);
                }
            }
        }
    }

    private static void placeHighRankingPieces(boardMP board, List<multiplayerPieces.PieceType> highRanks) {
        // Place high-ranking pieces in strategic positions
        // Marshal and General should be placed where they can move effectively
        int[] strategicCols = {3, 4, 2, 5}; // Center positions for high-ranking pieces

        for (int col : strategicCols) {
            if (!highRanks.isEmpty() && board.isCellEmpty(7, col)) {
                boardMP.updateCell(7, col, highRanks.remove(0).name(), boardMP.PLAYER_COLOR);
            }
        }

        // Place any remaining high-rank pieces
        for (int col = 0; col < 8; col++) {
            if (board.isCellEmpty(7, col) && !highRanks.isEmpty()) {
                boardMP.updateCell(7, col, highRanks.remove(0).name(), boardMP.PLAYER_COLOR);
            }
        }
    }

    private static void placeScouts(boardMP board, List<multiplayerPieces.PieceType> scouts) {
        // Place scouts in positions where they can move effectively
        int[] scoutPositions = {
                6 * 8 + 0, // Upper left
                6 * 8 + 7, // Upper right
                6 * 8 + 3, // Upper middle left
                6 * 8 + 4  // Upper middle right
        };

        for (int pos : scoutPositions) {
            int row = pos / 8;
            int col = pos % 8;
            if (!scouts.isEmpty() && board.isCellEmpty(row, col)) {
                boardMP.updateCell(row, col, scouts.remove(0).name(), boardMP.PLAYER_COLOR);
            }
        }

        // Place remaining scouts
        while (!scouts.isEmpty()) {
            for (int col = 0; col < 8; col++) {
                if (board.isCellEmpty(6, col) && !scouts.isEmpty()) {
                    boardMP.updateCell(6, col, scouts.remove(0).name(), boardMP.PLAYER_COLOR);
                }
            }
        }
    }

    private static void fillRemainingSpaces(boardMP board, List<multiplayerPieces.PieceType> remainingPieces) {
        // Fill any remaining empty spaces with lower-ranking pieces
        for (int row = 7; row >= 6; row--) { // Start from bottom row
            for (int col = 0; col < 8; col++) {
                if (board.isCellEmpty(row, col) && !remainingPieces.isEmpty()) {
                    boardMP.updateCell(row, col, remainingPieces.remove(0).name(), boardMP.PLAYER_COLOR);
                }
            }
        }
    }

    private static boolean isValidPosition(int row, int col) {
        return row >= 6 && row <= 7 && col >= 0 && col < 8;
    }
}