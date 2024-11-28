package stratego.game.pieces;

public class Bom extends Piece {
    public Bom(String team) {
        super("Bom", 0, null, false);
    }

    @Override
    public boolean canDefeat(Piece other) {
        return false;
    }
}
