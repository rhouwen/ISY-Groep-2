import java.util.Random;

public class AI extends Player {
    private Random random = new Random();

    public AI(String name, Board board) {
        super(name, board);
    }

    // AI move without intelligence just random
    @Override
    public void makeMove(Board opponentBoard) {
        // random coordinates
        int x = random.nextInt(8); //
        int y = random.nextInt(8);

        System.out.println(getName() + " schiet op (" + x + ", " + y + ")");

        // Take shot
        opponentBoard.takeShot(x, y);
    }
}
