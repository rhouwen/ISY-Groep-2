package stratego.gui;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class multiplayerPlace {

    public static void placePieces(boardMP board) {
        List<multiplayerPieces.PieceType> pieces = multiplayerPieces.createPieces();
        Collections.shuffle(pieces);
        Random rand = new Random();

        int rowStart = 6; // Bottom two rows for placing pieces
        int rowEnd = 7;
        int colStart = 0;
        int colEnd = 7;

        for (multiplayerPieces.PieceType piece : pieces) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt((rowEnd - rowStart) + 1) + rowStart;
                int col = rand.nextInt((colEnd - colStart) + 1) + colStart;
                if (board.isCellEmpty(row, col)) {
                    boardMP.updateCell(row, col, piece.name(), Color.RED);
                    placed = true;
                }
            }
        }
    }
}