package stratego.game;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;
import java.util.Collections;
import java.util.List;

public class Board {
    private final Piece[][] board; // 2D grid for pieces
    private final int rows;
    private final int cols;
    private final boolean [][] waterTiles;

    // constructor grid
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Piece[rows][cols];
        this.waterTiles = new boolean[rows][cols];
        initializeWaterTiles();
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = null; // Empty cell
            }
        }
    }

    // initialiseer board
    public void initializeBoard(Piecefactory pieceFactory) {
        List<Piece> redPieces = Piecefactory.createTeamPieces("Red");
        List<Piece> bluePieces = Piecefactory.createTeamPieces("Blue");

        //Shuffeled de pieces random moet vervangen worden uiteindelijk door boardAI
        Collections.shuffle(redPieces);


        // plaats rode pieces random vervangen voor handmatig kunnen plaatsen
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < cols; j++) {
                if (index < redPieces.size()) {
                    board[i][j] = redPieces.get(index++);
                }
            }
        }

        // blauwe pieces random moet vervangen worden daar plaatsStuk.
        index = 0;

    }

    private void initializeWaterTiles() {
        setWaterTile(4, 2);
        setWaterTile(4, 3);
        setWaterTile(5, 3);
        setWaterTile(5, 2);
        setWaterTile(4, 6);
        setWaterTile(4, 7);
        setWaterTile(5, 7);
        setWaterTile(5, 6);
    }

    private void setWaterTile(int row, int col) {
        if (isWithinBounds(row, col)) {
            waterTiles[row][col] = true;
        }
    }

    public boolean isWaterTile(int row, int col) {
        return isWithinBounds(row,col) && waterTiles[row][col];
    }

    public boolean movePiece(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        if (!isWithinBounds(sourceRow, sourceCol) || !isWithinBounds(targetRow, targetCol)) {
            return false;
        }

        Piece sourcePiece = board[sourceRow][sourceCol];
        Piece targetPiece = board[targetRow][targetCol];

        // Is positie valid en kan hij bewegen
        if (sourcePiece == null || !sourcePiece.canMove()) {
            return false;
        }

        // als de target leeg is gelijk verplaatsen
        if (targetPiece == null) {
            board[targetRow][targetCol] = sourcePiece;
            board[sourceRow][sourceCol] = null;
            return true;
        }

        // start combat als target bezet is
        if (!sourcePiece.getTeam().equals(targetPiece.getTeam())) {
            return handleCombat(sourceRow, sourceCol, targetRow, targetCol);
        }

        // als target ally is
        return false;
    }

    private boolean handleCombat(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        Piece attacker = board[sourceRow][sourceCol];
        Piece defender = board[targetRow][targetCol];

        // attacker wint
        if (attacker.canDefeat(defender)) {
            board[targetRow][targetCol] = attacker;
            board[sourceRow][sourceCol] = null;
            return true;
        }

        // defender wint
        if (!defender.canDefeat(attacker)) {
            board[sourceRow][sourceCol] = null;
            return true;
        }

        // zelfde rank (beide dood)
        if (attacker.getRank() == defender.getRank()) {
            board[sourceRow][sourceCol] = null;
            board[targetRow][targetCol] = null;
            return true;
        }

        return false;
    }

    public String checkBoardState() {
        boolean redFlagPresent = false;
        boolean blueFlagPresent = false;

        // checkt welke vlaggen er zijn
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getName().equalsIgnoreCase("Flag")) {
                    if (piece.getTeam().equalsIgnoreCase("Red")) {
                        redFlagPresent = true;
                    } else if (piece.getTeam().equalsIgnoreCase("Blue")) {
                        blueFlagPresent = true;
                    }
                }
            }
        }

        if (!redFlagPresent) {
            return "Blue Wins!";
        }
        if (!blueFlagPresent) {
            return "Red Wins!";
        }
        return "In Progress";
    }

    // helper om te checken of de positie legaal binnen het board is
    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }


    //getters
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public Piece getPieceAt(int row, int col) {
        if (isWithinBounds(row, col)) {
            return board[row][col];
        }
        return null;
    }


    // print het board (voor debuggen)
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null) {
                    System.out.print("- "); // Empty cell
                } else {
                    System.out.print(board[i][j].getName().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }
}