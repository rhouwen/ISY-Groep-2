package stratego.gui;

import java.util.ArrayList;
import java.util.List;

public class multiplayerPieces {

    public enum PieceType {
        MARSHAL, GENERAL, MINER, SCOUT, SPY, BOMB, FLAG
    }

    public static List<PieceType> createPieces() {
        List<PieceType> pieces = new ArrayList<>();
        pieces.add(PieceType.MARSHAL);
        pieces.add(PieceType.GENERAL);
        pieces.add(PieceType.MINER);
        pieces.add(PieceType.MINER);
        pieces.add(PieceType.SCOUT);
        pieces.add(PieceType.SCOUT);
        pieces.add(PieceType.SPY);
        pieces.add(PieceType.BOMB);
        pieces.add(PieceType.BOMB);
        pieces.add(PieceType.FLAG);
        return pieces;
    }
}
