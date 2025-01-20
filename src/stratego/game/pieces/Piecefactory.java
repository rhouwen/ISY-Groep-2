package stratego.game.pieces;

import java.util.ArrayList;
import java.util.List;

public class Piecefactory {

    public static List<Piece> createTeamPieces(String team) {
        List<Piece> pieces = new ArrayList<>();

        pieces.add(new Generic("Maarschalk", 10, team));
        pieces.add(new Generic("Generaal", 9, team));
        addMultiplePieces(pieces, "Kolonel", 8, 2, team);
        addMultiplePieces(pieces, "Majoor", 7, 3, team);
        addMultiplePieces(pieces, "Kapitein", 6, 4, team);
        addMultiplePieces(pieces, "Luitenant", 5, 4, team);
        addMultiplePieces(pieces, "Sergeant", 4, 4, team);
        pieces.add(new Spion(team));
        pieces.add(new Flag(team));
        addMultiplePieces(pieces, "Bom", 0, 6, team);
        addMultiplePieces(pieces, "Verkenner", 2, 8, team);
        addMultiplePieces(pieces, "Mineur", 3, 5, team);

        return pieces;
    }

    private static void addMultiplePieces(List<Piece> pieces, String name, int rank, int count, String team) {
        for (int i = 0; i < count; i++) {
            if (name.equals("Bom")) {
                pieces.add(new Bom(team));
            } else if (name.equals("Verkenner")) {
                pieces.add(new Verkenner(team));
            } else if (name.equals("Mineur")) {
                pieces.add(new Mineur(team));
            } else {
                pieces.add(new Generic(name, rank, team));
            }
        }
    }
}
