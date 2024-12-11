package stratego.ai;

import stratego.game.pieces.Piece;
import stratego.gui.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardAI {

    private static final int BOARD_SIZE = 10;

    public static void placePiecesRandomly(List<Piece> pieces, String team, GUI gui) {
        List<Point> positions = new ArrayList<>();
        int startRow = team.equals("Rood") ? 6 : 0;
        int endRow = team.equals("Rood") ? 10 : 4;

        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                positions.add(new Point(row, col));
            }
        }

        Collections.shuffle(positions);

        for (int i = 0; i < pieces.size(); i++) {
            if (i >= positions.size()) break;
            Point pos = positions.get(i);
            Piece piece = pieces.get(i);
            gui.updateCell(pos.x, pos.y, piece.getName(), team.equals("Rood") ? Color.RED : Color.BLUE);
        }
    }
}

