package GUI;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected List<Ship> ships = new ArrayList<>();
    protected Board board;

    public Player(Board board) {
        this.board = board;
        // Voeg schepen toe: 1 van 6, 1 van 4, 1 van 3, 1 van 2
        ships.add(new Ship(6));
        ships.add(new Ship(4));
        ships.add(new Ship(3));
        ships.add(new Ship(2));
    }

    public abstract void placeShips();
    public abstract int[] makeMove();
}
