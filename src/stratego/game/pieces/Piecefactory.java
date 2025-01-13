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
            pieces.add(new Generic(name, rank, team));
        }
    }

    // Nieuwe methode om een stuk aan te maken op basis van naam en team
    public static Piece createPiece(String name, String team) {
        switch (name) {
            case "Maarschalk":
                return new Generic("Maarschalk", 10, team);
            case "Generaal":
                return new Generic("Generaal", 9, team);
            case "Kolonel":
                return new Generic("Kolonel", 8, team);
            case "Majoor":
                return new Generic("Majoor", 7, team);
            case "Kapitein":
                return new Generic("Kapitein", 6, team);
            case "Luitenant":
                return new Generic("Luitenant", 5, team);
            case "Sergeant":
                return new Generic("Sergeant", 4, team);
            case "Spion":
                return new Spion(team);
            case "Flag":
                return new Flag(team);
            case "Bom":
                return new Generic("Bom", 0, team);
            case "Verkenner":
                return new Generic("Verkenner", 2, team);
            case "Mineur":
                return new Generic("Mineur", 3, team);
            default:
                throw new IllegalArgumentException("Onbekend stuk: " + name);
        }
    }
}
