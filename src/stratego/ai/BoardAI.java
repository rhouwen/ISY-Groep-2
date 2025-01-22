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

        // Indeling van rijen (achter, midden, voor)
        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < cols; col++) {
                int[] pos = {row, col};
                if (row < startRow + 2) {
                    backRows.add(pos); // Achterste rijen
                } else if (row < startRow + 3) {
                    midRows.add(pos); // Middelste rijen
                } else {
                    frontRows.add(pos); // Voorste rijen
                }
            }
        }

        // Schud alle posities
        Collections.shuffle(backRows);
        Collections.shuffle(midRows);
        Collections.shuffle(frontRows);

        // 1. Vlag plaatsen (geheel random in backRows)
        int[] flagPosition = null;
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Flag")) {
                flagPosition = backRows.remove(0); // Altijd in backRows
                board.placePiece(piece, flagPosition[0], flagPosition[1]);
                break;
            }
        }

        // 2. Bommen rondom de vlag strategisch plaatsen
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Bom")) {
                if (!backRows.isEmpty()) { // Als er plekken over zijn in backRows
                    int[] pos = backRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // 3. Sterke verdediger dicht bij de vlag plaatsen
        for (Piece piece : pieces) {
            if (piece.getRank() >= 8) { // Kies een Maarschalk (10) of Generaal (9)
                if (flagPosition != null && !backRows.isEmpty()) {
                    int[] pos = backRows.remove(0); // Plaats dicht bij bommen/vlag
                    board.placePiece(piece, pos[0], pos[1]);
                    break; // Zorg ervoor dat slechts één sterk stuk de vlag beschermt
                }
            }
        }

        // 4. Verkenners plaatsen in de voorste rij
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Verkenner")) {
                if (!frontRows.isEmpty()) {
                    int[] pos = frontRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // 5. Miner in de middelste of voorste rij plaatsen
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Miner")) {
                if (!midRows.isEmpty()) { // Geef de voorkeur aan de middelste rij
                    int[] pos = midRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                } else if (!frontRows.isEmpty()) { // Als middelste rij leeg is, plaats in front
                    int[] pos = frontRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // 6. Spion in de middelste of achterste rij plaatsen
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Spion")) {
                if (!midRows.isEmpty()) { // Geef de voorkeur aan de middelste rij
                    int[] pos = midRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                } else if (!backRows.isEmpty()) { // Anders in de achterste rij
                    int[] pos = backRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // 7. Middelmatige rangen (3-7) in midRows of frontRows
        for (Piece piece : pieces) {
            if (piece.getRank() >= 3 && piece.getRank() <= 7) {
                if (!frontRows.isEmpty()) { // Probeer eerst vooraan
                    int[] pos = frontRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                } else if (!midRows.isEmpty()) { // Anders in het midden
                    int[] pos = midRows.remove(0);
                    board.placePiece(piece, pos[0], pos[1]);
                }
            }
        }

        // 8. Overige stukken verwerken (willekeurige posities)
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
