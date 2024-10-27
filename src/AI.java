import java.util.Random;

public class AI {
    private Board board;

    public AI(Board board) {
        this.board = board;
    }

    public void placeShips() {
        Random random = new Random();
        int[] shipLengths = {6, 4, 3, 2}; // De lengtes van de schepen

        for (int length : shipLengths) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(8); // Willekeurige x-coördinaat
                int y = random.nextInt(8); // Willekeurige y-coördinaat
                boolean horizontal = random.nextBoolean(); // Willekeurig horizontaal of verticaal

                Ship ship = new Ship(length);
                if (board.placeShip(ship, x, y, horizontal)) {
                    placed = true; // Schip is succesvol geplaatst
                }
            }
        }
    }

    public int[] makeMove() {
        // Hier kan de AI implementatie van zetten komen. Voor nu retourneren we een random schot.
        Random random = new Random();
        int x = random.nextInt(8);
        int y = random.nextInt(8);
        return new int[]{x, y}; // Retourneer een random zet
    }
}
