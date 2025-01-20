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

    public void handleCellClick(int row, int col) {
        System.out.println("Cell clicked at row: " + row + ", col: " + col);

        if (setupPhase) {
            System.out.println("🔧 Setupfase: probeer stuk te plaatsen.");
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
            } else {
                System.out.println("⛔ Je kunt alleen je eigen stukken selecteren.");
            }
            return;
        }

        // ✅ Tweede klik: Beweeg of val aan
        System.out.println("🔴 Speler probeert stuk te verplaatsen van (" + selectedRow + ", " + selectedCol + ") naar (" + row + ", " + col + ")");

        if (clickedPiece == null) {
            // 🔹 Bewegen als het veld leeg is
            if (board.movePiece(selectedRow, selectedCol, row, col)) {
                System.out.println("✅ Zet geslaagd!");
                SinglePlayerGUI.getGameBoard().updateBoard(board);
                endPlayerTurn();
            } else {
                System.out.println("❌ Ongeldige zet, probeer opnieuw.");
            }
        } else if (!clickedPiece.getTeam().equalsIgnoreCase("Red")) {
            // 🛡️ **Aanvallen als het een vijandig stuk is**
            Piece attacker = board.getPieceAt(selectedRow, selectedCol);
            if (attacker != null && attacker.canDefeat(clickedPiece)) {
                System.out.println("⚔️ Gevecht! " + attacker.getName() + " (" + attacker.getTeam() + ") valt " + clickedPiece.getName() + " (" + clickedPiece.getTeam() + ") aan!");

                // 🔥 Stuk verslagen -> Verwijderen en verplaatsen
                board.removePiece(row, col);
                board.movePiece(selectedRow, selectedCol, row, col);
                System.out.println("💥 " + clickedPiece.getName() + " is verslagen!");

                SinglePlayerGUI.getGameBoard().updateBoard(board);
                endPlayerTurn();
            } else {
                System.out.println("❌ Aanval mislukt! Stuk kan niet winnen.");
            }
        } else {
            System.out.println("⛔ Je kunt niet op je eigen stukken klikken.");
        }

        // Reset selectie
        selectedRow = -1;
        selectedCol = -1;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPieceAt(fromRow, fromCol);
        if (piece == null) return false;

        // 📌 Verkenner mag in een rechte lijn zover als hij wil
        if (piece.getName().equalsIgnoreCase("Verkenner")) {
            return (fromRow == toRow || fromCol == toCol);
        }

        // 📌 Stukken kunnen normaal maar 1 vakje bewegen
        if (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1) {
            return piece.canMove();
        }

        // 📌 Flag en Bom mogen niet bewegen
        if (piece.getName().equalsIgnoreCase("Flag") || piece.getName().equalsIgnoreCase("Bom")) {
            return false;
        }

        return false;
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

    private void endPlayerTurn() {
        playerTurn = false;
        System.out.println("🤖 AI (Blauw) doet een zet...");

        board.executeAITurn(); // 🟦 AI voert een zet uit
        SinglePlayerGUI.getGameBoard().updateBoard(board);

        playerTurn = true; // 🔴 Speler krijgt weer de beurt
        System.out.println("🔴 Rode speler is weer aan zet.");
    }

    private void resolveCombat(int attackerRow, int attackerCol, int defenderRow, int defenderCol) {
        Piece attacker = board.getPieceAt(attackerRow, attackerCol);
        Piece defender = board.getPieceAt(defenderRow, defenderCol);

        if (attacker == null || defender == null) return;

        System.out.println("⚔️ Gevecht! " + attacker.getName() + " (Rood) vs " + defender.getName() + " (Blauw)");

        if (defender.getName().equalsIgnoreCase("Flag")) {
            System.out.println("🏁 De Rode speler heeft de Blauwe vlag veroverd! 🎉");
            board.removePiece(defenderRow, defenderCol);
            board.movePiece(attackerRow, attackerCol, defenderRow, defenderCol);
            SinglePlayerGUI.getGameBoard().updateBoard(board);
            System.out.println("🎉 Spel afgelopen! Rode speler wint!");
            return;
        }

        if (defender.getName().equalsIgnoreCase("Bom")) {
            if (attacker.getName().equalsIgnoreCase("Mineur")) {
                System.out.println("💣 Mineur vernietigt Bom!");
                board.removePiece(defenderRow, defenderCol);
                board.movePiece(attackerRow, attackerCol, defenderRow, defenderCol);
            } else {
                System.out.println("💥 Bom ontploft! Beide stukken vernietigd!");
                board.removePiece(attackerRow, attackerCol);
                board.removePiece(defenderRow, defenderCol);
            }
            SinglePlayerGUI.getGameBoard().updateBoard(board);
            endPlayerTurn();
            return;
        }

        if (attacker.getName().equalsIgnoreCase("Spion") && defender.getName().equalsIgnoreCase("Maarschalk")) {
            System.out.println("🕵️‍♂️ Spion verslaat Maarschalk!");
            board.removePiece(defenderRow, defenderCol);
            board.movePiece(attackerRow, attackerCol, defenderRow, defenderCol);
            SinglePlayerGUI.getGameBoard().updateBoard(board);
            endPlayerTurn();
            return;
        }

        if (attacker.getRank() > defender.getRank()) {
            System.out.println("⚔️ " + attacker.getName() + " verslaat " + defender.getName());
            board.removePiece(defenderRow, defenderCol);
            board.movePiece(attackerRow, attackerCol, defenderRow, defenderCol);
        } else if (attacker.getRank() < defender.getRank()) {
            System.out.println("💀 " + attacker.getName() + " wordt verslagen door " + defender.getName());
            board.removePiece(attackerRow, attackerCol);
        } else {
            System.out.println("☠️ Beide stukken worden vernietigd!");
            board.removePiece(attackerRow, attackerCol);
            board.removePiece(defenderRow, defenderCol);
        }

        SinglePlayerGUI.getGameBoard().updateBoard(board);
        endPlayerTurn();
    }


}


