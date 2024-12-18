package stratego.game.pieces;

public class Verkenner extends Piece {

    public Verkenner(String team) {
        super("Verkenner", 2, team, true);
    }

    public boolean canDefeat(Piece other){
        return this.getRank() > other.getRank();
    }

}
