package stratego.game.pieces;

public class Generic extends Piece {

    public Generic(String name, int rank, int count, String team){
        super(name, rank, team, count, true);
    }

    public boolean canDefeat(Piece other){
        return this.getRank() > other.getRank();
    }
}

