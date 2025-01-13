package stratego.gui;

import stratego.game.Board;
import stratego.game.pieces.Piece;

public class ClickHandler {
    private static ClickHandler instance;
    private Board board;

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

    public void handleCellClick(int row, int col) {
        System.out.println("Cell clicked at row: " + row + ", col: " + col);

        // Haal het geselecteerde stuk op
        String selectedPieceName = SinglePlayerGUI.getSelectedPiece();

        if ("Geen stuk geselecteerd".equals(selectedPieceName)) {
            System.out.println("Geen stuk geselecteerd!");
            return;
        }

        // Haal het stuk uit de PieceSelectionPanel
        Piece piece = SinglePlayerGUI.pieceSelectionPanel.getPieceToPlace(selectedPieceName);

        if (piece == null) {
            System.out.println("Geen " + selectedPieceName + " meer beschikbaar om te plaatsen!");
            return;
        }

        // Probeer het stuk op het bord te plaatsen
        if (board.placePiece(piece, row, col)) {
            System.out.println("Stuk geplaatst: " + selectedPieceName + " op (" + row + ", " + col + ")");
            SinglePlayerGUI.getGameBoard().updateBoard(board); // Update de GUI om het stuk te tonen
        } else {
            System.out.println("Kan het stuk niet plaatsen op (" + row + ", " + col + ")");
            // Voeg het stuk terug als het niet geplaatst kon worden
            SinglePlayerGUI.pieceSelectionPanel.addPieceBack(selectedPieceName, piece);
        }
    }
}
