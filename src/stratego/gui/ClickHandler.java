package stratego.gui;

import stratego.game.Board;
import stratego.game.pieces.Piece;

public class ClickHandler {
    private static ClickHandler instance;
    private Board board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean setupPhase = true; // Stukken plaatsen fase
    private boolean playerTurn = true; // 🔴 Rode speler begint

    private ClickHandler(Board board) {
        this.board = board;
    }

    public static ClickHandler getInstance(Board board) {
        if (instance == null) {
            instance = new ClickHandler(board);
        }
        return instance;
    }

    public static ClickHandler getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ClickHandler is not initialized. Pass a Board object first.");
        }
        return instance;
    }

    public boolean hasSelectedPiece() {
        return selectedRow != -1 && selectedCol != -1;
    }


    public void handleCellClick(int row, int col) {
        System.out.println("🖱️ Cell clicked at (" + row + ", " + col + ")");

        if (setupPhase) {
            System.out.println("🔧 Setupfase: stuk proberen te plaatsen.");
            placeNewPiece(row, col);
            return;
        }

        if (!playerTurn) {
            System.out.println("⛔ Niet jouw beurt!");
            return;
        }

        Piece clickedPiece = board.getPieceAt(row, col);

        // ✅ Eerste klik: Selecteer een eigen stuk
        if (selectedRow == -1 && selectedCol == -1) {
            if (clickedPiece != null && clickedPiece.getTeam().equalsIgnoreCase("Red")) {
                selectedRow = row;
                selectedCol = col;
                System.out.println("🔴 Rood stuk geselecteerd: " + clickedPiece.getName() + " op (" + row + ", " + col + ")");
                SinglePlayerGUI.getGameBoard().updateBoard(board);
            } else {
                System.out.println("⛔ Je kunt alleen je eigen stukken selecteren.");
            }
            return;
        }

        // ✅ Tweede klik: Beweeg of val aan
        if (isValidMove(selectedRow, selectedCol, row, col)) {
            if (clickedPiece == null) {
                board.movePiece(selectedRow, selectedCol, row, col);
                System.out.println("✅ Stuk bewogen naar (" + row + ", " + col + ")");
            } else if (!clickedPiece.getTeam().equalsIgnoreCase("Red")) {
                System.out.println("⚔️ Gevecht tegen " + clickedPiece.getName() + "!");
                resolveCombat(selectedRow, selectedCol, row, col);
            } else {
                System.out.println("⛔ Je kunt niet op je eigen stukken klikken.");
            }

            SinglePlayerGUI.getGameBoard().updateBoard(board);
            endPlayerTurn();
        } else {
            System.out.println("❌ Ongeldige zet.");
        }

        selectedRow = -1;
        selectedCol = -1;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPieceAt(fromRow, fromCol);
        if (piece == null) return false;
        if (!piece.canMove()) return false;

        if (piece.getName().equalsIgnoreCase("Verkenner")) {
            return (fromRow == toRow || fromCol == toCol);
        }
        return Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1;
    }

    private void resolveCombat(int attackerRow, int attackerCol, int defenderRow, int defenderCol) {
        Piece attacker = board.getPieceAt(attackerRow, attackerCol);
        Piece defender = board.getPieceAt(defenderRow, defenderCol);
        if (attacker == null || defender == null) return;

        if (attacker.canDefeat(defender)) {
            board.removePiece(defenderRow, defenderCol);
            board.movePiece(attackerRow, attackerCol, defenderRow, defenderCol);
        } else if (attacker.getRank() < defender.getRank()) {
            board.removePiece(attackerRow, attackerCol);
        } else {
            board.removePiece(attackerRow, attackerCol);
            board.removePiece(defenderRow, defenderCol);
        }

        SinglePlayerGUI.getGameBoard().updateBoard(board);
    }

    private void endPlayerTurn() {
        playerTurn = false;
        System.out.println("🤖 AI doet een zet...");
        board.executeAITurn();
        SinglePlayerGUI.getGameBoard().updateBoard(board);
        playerTurn = true;
    }

    public void startGameFromButton() {
        if (!setupPhase) {
            System.out.println("⚠️ Het spel is al gestart!");
            return;
        }

        if (!board.allRedPiecesPlaced()) {
            System.out.println("⚠️ Je moet eerst alle rode stukken plaatsen!");
            return;
        }

        setupPhase = false;
        playerTurn = true; // 🔴 Rode speler begint
        selectedRow = -1;
        selectedCol = -1;

        System.out.println("🎉 Spel gestart! Rode speler mag beginnen.");
    }

    private void placeNewPiece(int row, int col) {
        String selectedPieceName = SinglePlayerGUI.getSelectedPiece();
        if ("Geen stuk geselecteerd".equals(selectedPieceName)) {
            System.out.println("⚠️ Geen stuk geselecteerd!");
            return;
        }

        Piece piece = SinglePlayerGUI.pieceSelectionPanel.getPieceToPlace(selectedPieceName);
        if (piece == null) {
            System.out.println("⛔ Geen " + selectedPieceName + " meer beschikbaar om te plaatsen!");
            return;
        }

        if (board.placePiece(piece, row, col)) {
            System.out.println("✅ Stuk geplaatst: " + selectedPieceName + " op (" + row + ", " + col + ")");
            SinglePlayerGUI.getGameBoard().updateBoard(board);
        } else {
            System.out.println("❌ Kan het stuk niet plaatsen op (" + row + ", " + col + ")");
            SinglePlayerGUI.pieceSelectionPanel.addPieceBack(selectedPieceName, piece);
        }
    }
}
