import java.util.Scanner;

public class Mens extends Player {

    public Mens(String name, Board board) {
        super(name, board);
    }

    // Human shot
    @Override
    public void makeMove(Board opponentBoard) {
        // Console input, later we need to make this working with GUI
        Scanner scanner = new Scanner(System.in);
        System.out.println(getName() + ", voer de coördinaten in om te schieten (bijv. 2 3): ");
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        // make shot
        opponentBoard.takeShot(x, y);
    }
}

