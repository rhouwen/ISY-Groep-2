package stratego.game.pieces;

public abstract class Piece {

    //basisklasse voor alle stukken
    private String name;
    private int rank;
    private String team;
    private boolean canMove;

    public Piece(String name, int rank, String team, boolean canMove) {

        this.name = name;
        this.rank = rank;
        this.team = (team != null) ? team : "Geen team";
        this.canMove = canMove;

    }

    //getters
    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public String getTeam() {
        return team;
    }

    public boolean canMove() {
        return canMove;
    }

    //standaard gevechtsregel
    public boolean canDefeat(Piece other){
        return this.rank > other.getRank();
    }

    public void setTeam(String team) {
        this.team = team;
    }


}
