package stratego.game.pieces;

public abstract class Piece {
    private String name;
    private int rank;
    private String team;
    private boolean canMove;

    public Piece(String name, int rank, String team, boolean canMove) {
        this.name = name;
        this.rank = rank;
        this.team = team;
        this.canMove = canMove;
    }

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

    // ✅ Voeg deze methode toe om het team te kunnen wijzigen
    public void setTeam(String team) {
        this.team = team;
    }

    public abstract boolean canDefeat(Piece other);
}
