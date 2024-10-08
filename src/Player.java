public abstract class Player {
    protected String name;  // Name of player
    protected Board board;  // Board from player

    public Player(String name, Board board) {
        this.name = name;
        this.board = board;
    }

    // Method for making a move
    public abstract void makeMove(Board opponentBoard);

    // Get board from player
    public Board getBoard() {
        return board;
    }

    // Get name from player
    public String getName() {
        return name;
    }
}
