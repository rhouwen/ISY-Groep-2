package stratego.gui;

import stratego.game.Board;
import stratego.game.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class plaatsStuk {

    public void plaatsStuk(Board board, List<Piece> pieces, String team) {


    }

    // deze functie helpt met debuggen
    public void printSelectedPiece() {
        String selectedPiece = SinglePlayerGUI.getSelectedPiece();
        System.out.println("Stuk geselecteerd: " + selectedPiece);
    }
}
