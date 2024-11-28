package stratego.game.pieces;

public class Generic extends Piece {

    public Generic(String name, int rank, String team){
        super(name, rank, team, true);
    }

    public boolean canDefeat(Piece other){
        return this.getRank() > other.getRank();
    }
}

