package GUI;

public class Mens extends Player {
    public Mens(Board board) {
        super(board);
    }

    @Override
    public void placeShips() {
        // Geen implementatie nodig, de plaatsing gebeurt via de GUI
    }

    @Override
    public int[] makeMove() {
        // Hier zou je kunnen logica toevoegen voor de menselijke zet,
        // maar de zetten worden nu gedaan via de GUI.
        return null;
    }
}
