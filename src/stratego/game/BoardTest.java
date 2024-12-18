package stratego.game;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;

public class BoardTest {
    public static void main(String[] args) {

        Board board = new Board(10, 10);

        Piecefactory factory = new Piecefactory();

        board.initializeBoard(factory);
        System.out.println("Initial Board Setup:");
        board.printBoard();

        System.out.println("\nTesting Move:");
        boolean moveSuccessful = board.movePiece(3, 1, 4, 1);

        if (moveSuccessful) {
            System.out.println("Move successful!");
        } else {
            System.out.println("Move failed!");
        }

        board.printBoard();


        System.out.println("\nGame State:");
        System.out.println(board.checkBoardState());
    }
}