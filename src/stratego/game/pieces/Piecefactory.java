package stratego.game.pieces;

import java.util.ArrayList;
import java.util.List;

public class Piecefactory {

    private static void addMultiplePieces(List<Piece> pieces, String name, int rank, int count, String team){
        for(int i = 0; i < count; i++){
            pieces.add(new Generic(name, rank, team));
        }
    }


    //Maakt de stukken tijdens het opzetten van het bord.
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
        addMultiplePieces(pieces, new Bom(team), 1, 6);
        addMultiplePieces(pieces, new Mineur(team), 3, 5);
        addMultiplePieces(pieces, new Verkenner(team), 2, 8);




        return pieces;

    }

}

