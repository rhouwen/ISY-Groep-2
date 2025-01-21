package stratego.ai;

import stratego.game.Board;
import stratego.game.pieces.Piece;
import stratego.game.pieces.Verkenner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AI {

    public static void setupBoard(Board board, List<Piece> pieces) {
        String team = "Blue"; // ✅ AI speelt als blauw
        int startRow = 0; // ✅ AI plaatst zijn stukken aan de bovenkant (rijen 0-3)
        int endRow = 4;

        List<int[]> allPositions = new ArrayList<>();
        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (!board.isWaterTile(row, col)) {
                    allPositions.add(new int[]{row, col});
                }
            }
        }

        Collections.shuffle(allPositions);

        // 1️⃣ Flag veilig achterin
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Flag")) {
                int[] pos = allPositions.remove(0);
                piece.setTeam(team); // ✅ Zorg ervoor dat alle AI-stukken blauw zijn
                board.placePiece(piece, pos[0], pos[1]);
                break;
            }
        }

        // 2️⃣ Bommen rond de vlag
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Bom") && !allPositions.isEmpty()) {
                int[] pos = allPositions.remove(0);
                piece.setTeam(team);
                board.placePiece(piece, pos[0], pos[1]);
            }
        }

        // 3️⃣ Hoge rangen in het midden
        for (Piece piece : pieces) {
            if (piece.getRank() >= 8 && !allPositions.isEmpty()) {
                int[] pos = allPositions.remove(0);
                piece.setTeam(team);
                board.placePiece(piece, pos[0], pos[1]);
            }
        }

        // 4️⃣ Verkenners vooraan
        for (Piece piece : pieces) {
            if (piece.getName().equalsIgnoreCase("Verkenner") && !allPositions.isEmpty()) {
                int[] pos = allPositions.remove(0);
                piece.setTeam(team);
                board.placePiece(piece, pos[0], pos[1]);
            }
        }

        // 5️⃣ Overige stukken plaatsen
        for (Piece piece : pieces) {
            if (!allPositions.isEmpty() && boardIsEmpty(board, piece)) {
                int[] pos = allPositions.remove(0);
                piece.setTeam(team);
                board.placePiece(piece, pos[0], pos[1]);
            }
        }

        System.out.println("✅ AI heeft zijn stukken correct aan de bovenkant geplaatst!");
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

    public static void makeMove(Board board) {
        List<int[]> possibleMoves = new ArrayList<>();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getTeam().equalsIgnoreCase("Blue") && piece.canMove()) {
                    addValidMoves(board, piece, row, col, possibleMoves);
                }
            }
        }

        if (!possibleMoves.isEmpty()) {
            Collections.shuffle(possibleMoves);
            int[] move = possibleMoves.get(0);
            board.movePiece(move[0], move[1], move[2], move[3]);
            System.out.println("AI verplaatst stuk naar (" + move[2] + ", " + move[3] + ")");
        } else {
            System.out.println("AI kon geen zet doen.");
        }
    }

    private static void addValidMoves(Board board, Piece piece, int row, int col, List<int[]> moves) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isWithinBounds(newRow, newCol) && board.getPieceAt(newRow, newCol) == null) {
                moves.add(new int[]{row, col, newRow, newCol});
            }
        }
    }

    private static void addStandardMoves(Board board, int row, int col, List<int[]> moves) {
        int[][] possibleDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : possibleDirections) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isWithinBounds(newRow, newCol) && board.getPieceAt(newRow, newCol) == null) {
                moves.add(new int[]{row, col, newRow, newCol});
            }
        }
    }

    private static void addVerkennerMoves(Board board, int row, int col, List<int[]> moves) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            while (board.isWithinBounds(newRow, newCol) && board.getPieceAt(newRow, newCol) == null) {
                moves.add(new int[]{row, col, newRow, newCol});
                newRow += dir[0];
                newCol += dir[1];
            }
        }
    }
}
