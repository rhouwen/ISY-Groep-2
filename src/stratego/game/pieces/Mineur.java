package stratego.game.pieces;

public class Mineur extends Piece {
    public Mineur(String team) {
        super("Mineur", 3, team, true);
    }

    @Override
    public boolean canDefeat(Piece other) {
        if (other instanceof Bom) {
            return true; // Mineur kan bommen vernietigen
        }
        return this.getRank() > other.getRank(); // Standaard vechtregel
    }
}
