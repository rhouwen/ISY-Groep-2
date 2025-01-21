package stratego.game.pieces;

public class Flag extends Piece {
    public Flag(String team) {
        super("Flag", 0, team, false);
    }

    @Override
    public boolean canDefeat(Piece other) {
        return false; // Vlag kan niet aanvallen
    }
}
