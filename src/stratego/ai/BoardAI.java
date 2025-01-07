package stratego.ai;

import stratego.game.Board;
import stratego.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardAI {

    public static void setupBoard(Board board, List<Piece> pieces, String team) {
        List<int[]> backRows = new ArrayList<>();
        List<int[]> midRows = new ArrayList<>();
        List<int[]> frontRows = new ArrayList<>();

        int rows = board.getRows();
        int cols = board.getCols();

        int startRow = team.equalsIgnoreCase("Red") ? 0 : rows - 4;
        int endRow = team.equalsIgnoreCase("Red") ? 4 : rows;

        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < cols; col++) {
                int[] pos = {row, col};
                if (row < startRow + 2) {
                    backRows.add(pos);
                } else if (row < startRow + 3) {
                    midRows.add(pos);
                } else {
                    frontRows.add(pos);
                }
            }
        }

        // shuffle voor de rest
        Collections.shuffle(backRows);
        Collections.shuffle(midRows);
        Collections.shuffle(frontRows);

        // vlag achterin
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Flag")) {
                int[] pos = backRows.remove(0);
                board.placePiece(piece, pos[0], pos[1]);
                break;
            }
        }

        // bommen rondom vlag
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Bom")) {
                if (!backRows.isEmpty()) {
                    int[] pos = backRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // hoge rangen in het midden
        for (Piece piece : pieces) {
            if (piece.getRank() >= 8) {
                if (!midRows.isEmpty()) {
                    int[] pos = midRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // verkenners voorin
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Verkenner")) {
                if (!frontRows.isEmpty()) {
                    int[] pos = frontRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // rest van pieces
        List<int[]> allRemainingPositions = new ArrayList<>();
        allRemainingPositions.addAll(frontRows);
        allRemainingPositions.addAll(midRows);
        allRemainingPositions.addAll(backRows);
        Collections.shuffle(allRemainingPositions);

        for (Piece piece : pieces) {
            if (boardIsEmpty(board, piece)) {
                if (!allRemainingPositions.isEmpty()) {
                    int[] pos = allRemainingPositions.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }
    }

    private static boolean boardIsEmpty(Board board, Piece piece) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.getPieceAt(row, col) == piece) {
                    return false;
                }
            }
        }
        return true;
    }
}
