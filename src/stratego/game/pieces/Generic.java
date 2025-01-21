package stratego.game.pieces;

public class Generic extends Piece {
    public Generic(String name, int rank, String team) {
        super(name, rank, team, true);
    }

    @Override
    public boolean canDefeat(Piece other) {
        return this.getRank() > other.getRank(); // Standaard regel: hoger rang verslaat lager rang
    }
}
