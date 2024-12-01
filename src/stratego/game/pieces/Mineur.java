package stratego.game.pieces;

public class Mineur extends Piece {

    public Mineur(String team){
        super("Mineur", 3, team, true);
    }

    public boolean canDefeat(Piece other){
        if (other instanceof Bom){
            return true;
        }
        return this.getRank() > other.getRank();
    }

}
