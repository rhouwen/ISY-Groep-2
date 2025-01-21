package stratego.game.pieces;

public class Spion extends Piece {
    public Spion(String team) {
        super("Spion", 1, team, true);
    }

    @Override
    public boolean canDefeat(Piece other) {
        return other.getRank() == 10; // Spion verslaat alleen de Maarschalk
    }
}
