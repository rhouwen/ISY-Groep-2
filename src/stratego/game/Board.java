package stratego.game;

import stratego.ai.AI;
import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;
import stratego.gui.SinglePlayerGUI;

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

        //gebruikt board ai om de pieces te plaatsen voor het rode team.
        AI.setupBoard(this, redPieces);
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

    public void removePiece(int row, int col) {
        if (isWithinBounds(row, col)) {
            board[row][col] = null;
            System.out.println("🗑️ Stuk verwijderd op (" + row + ", " + col + ")");
        }
    }




    public boolean isWaterTile(int row, int col) {
        return isWithinBounds(row,col) && waterTiles[row][col];
    }

    public boolean movePiece(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        System.out.println("Probeer stuk te verplaatsen van (" + sourceRow + ", " + sourceCol + ") naar (" + targetRow + ", " + targetCol + ")");

        if (!isWithinBounds(sourceRow, sourceCol) || !isWithinBounds(targetRow, targetCol)) {
            System.out.println("❌ Ongeldige zet: Positie buiten het bord.");
            return false;
        }

        Piece sourcePiece = board[sourceRow][sourceCol];
        Piece targetPiece = board[targetRow][targetCol];

        if (sourcePiece == null || !sourcePiece.canMove()) {
            System.out.println("❌ Ongeldige zet: Geen beweegbaar stuk op de bronpositie.");
            return false;
        }

        if (targetPiece == null) {
            System.out.println("✅ Lege plek, stuk verplaatst!");
            board[targetRow][targetCol] = sourcePiece;
            board[sourceRow][sourceCol] = null;
            return true;
        }

        // Controleer of de stukken vijanden zijn
        if (!sourcePiece.getTeam().equalsIgnoreCase(targetPiece.getTeam())) {
            System.out.println("⚔️ Stuk valt een vijand aan!");
            return handleCombat(sourceRow, sourceCol, targetRow, targetCol);
        }

        System.out.println("❌ Ongeldige zet: Doelpositie bevat een eigen stuk.");
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
    public boolean isWithinBounds(int row, int col) {
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

    public boolean placePiece(Piece piece, int row, int col) {
        if (!isWithinBounds(row, col) || isWaterTile(row, col) || board[row][col] != null) {
            System.out.println("❌ Kan stuk niet plaatsen: " + piece.getName() + " (Team: " + piece.getTeam() + ") op (" + row + ", " + col + ")");
            return false;
        }

        // ✅ **Forceer dat het team correct is voordat het stuk op het bord wordt gezet**
        piece.setTeam(piece.getTeam().equalsIgnoreCase("Blue") ? "Blue" : "Red");

        board[row][col] = piece;

        System.out.println("✅ Stuk correct geplaatst: " + piece.getName() + " (Team: " + piece.getTeam() + ") op (" + row + ", " + col + ")");
        return true;
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

    public boolean allRedPiecesPlaced() {
        return SinglePlayerGUI.pieceSelectionPanel.isEmpty();
    }



    public void executeAITurn() {
        System.out.println("AI is aan zet...");
        AI.makeMove(this); // ✅ AI doet een zet
        SinglePlayerGUI.getGameBoard().updateBoard(this);
    }



}