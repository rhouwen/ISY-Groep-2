package stratego.server;


import java.io.*;
import java.net.Socket;
import java.util.*;
import stratego.gui.boardMP;
import stratego.gui.multiplayerMove;
import stratego.gui.multiplayerPlace;


public class serverConnection implements Runnable {
    private String ip_address;
    private String port;
    private String username;
    private boolean placed;
    private boolean running = true;
    private boolean myTurn = false;
    private boardMP board;
    private Set<String> previousMoves;
    private boolean gameInProgress;

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public serverConnection(boardMP board) {
        this.board = board;
        Map<String, String> settings = stratego.gui.strategoSettings.loadSettings("src/stratego/utils/settings.txt");
        this.ip_address = settings.getOrDefault("IP Address", "");
        this.port = settings.getOrDefault("Port", "");
        this.username = settings.getOrDefault("Username", "");
        resetGame();
    }

    private void resetGame() {
        placed = false;
        myTurn = false;
        previousMoves = new HashSet<>();
        gameInProgress = false;
        board.resetBoard();
    }

    public void run() {
        try {
            System.out.println("Connecting to " + ip_address + ":" + port + " as " + username + ".....");

            client = new Socket(ip_address, Integer.parseInt(port));
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println("login " + username);

            System.out.println("Connected to " + ip_address + ":" + port + " as " + username + ".....");

            String inputMessage;
            while ((inputMessage = in.readLine()) != null && running) {
                System.out.println(inputMessage);

                if (inputMessage.startsWith("SVR GAME MATCH")) {
                    System.out.println("Match found - Starting new game");
                    resetGame();
                    gameInProgress = true;
                }

                if (gameInProgress) {
                    handleGameMessage(inputMessage);
                }

                // Handle game end conditions
                if (inputMessage.startsWith("SVR GAME WIN") ||
                        inputMessage.startsWith("SVR GAME LOSS") ||
                        inputMessage.startsWith("SVR GAME DRAW")) {

                    System.out.println("Game ended - Waiting for new match");
                    resetGame();
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Server error: " + e.getMessage());
            }
        }
    }

    private void handleGameMessage(String inputMessage) {
        if (inputMessage.startsWith("SVR GAME YOURTURN")) {
            if (!placed) {
                System.out.println("Placing pieces");
                placePieces();
                placed = true;
            } else {
                System.out.println("Your turn to move");
                myTurn = true;
                makeMove();
            }
        }
        else if (inputMessage.startsWith("SVR GAME MOVE")) {
            handleOpponentMove(inputMessage);
        }
        else if (inputMessage.startsWith("SVR GAME ATTACK RESULT")) {
            handleAttackResult(inputMessage);
        }
        else if (inputMessage.startsWith("SVR GAME DEFENSE RESULT")) {
            handleDefenseResult(inputMessage);
        }
        else if (inputMessage.startsWith("SVR GAME Opponent Placed")) {
            System.out.println("Opponent placed their pieces");
            placeOpponentPiece(inputMessage);
        }
    }

    private void placePieces() {
        multiplayerPlace.placePieces(board);

        for (int row = 6; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                if (!board.isCellEmpty(row, col)) {
                    String piece = board.getCellText(row, col);
                    int index = row * 8 + col;
                    boardMP.updateCell(row, col, piece, boardMP.PLAYER_COLOR); // Ensure our pieces are red
                    out.println("place " + index + " " + piece);
                }
            }
        }
    }


    private void handleDefenseResult(String message) {
        try {
            if (message.contains("Result: \"") && message.contains("Rank: \"")) {
                String result = message.substring(
                        message.indexOf("Result: \"") + 8,
                        message.indexOf("\"}")
                );
                String rank = message.substring(
                        message.indexOf("Rank: \"") + 7,
                        message.indexOf("\", Result")
                );

                // Get the position that was attacked (defender's position)
                int defenderRow = lastAttackedPosition / 8;
                int defenderCol = lastAttackedPosition % 8;

                if (result.equals("LOSS") || result.equals("TIE")) {
                    // Remove our defending piece
                    boardMP.removePiece(defenderRow, defenderCol);
                    System.out.println("Our " + rank + " was removed at position (" +
                            defenderRow + "," + defenderCol + ") (Result: " + result + ")");

                    if (result.equals("TIE")) {
                        // Remove opponent's attacking piece and clear the cell completely
                        int attackerRow = lastMoveFrom / 8;
                        int attackerCol = lastMoveFrom % 8;

                        // First remove the piece
                        boardMP.removePiece(attackerRow, attackerCol);

                        // Then ensure the cell is completely empty
                        boardMP.updateCell(attackerRow, attackerCol, "", boardMP.EMPTY_COLOR);
                        boardMP.updateCell(defenderRow, defenderCol, "", boardMP.EMPTY_COLOR);

                        System.out.println("Opponent piece removed and cell cleared at (" +
                                attackerRow + "," + attackerCol + ") (TIE)");
                    }
                }

                // Print board state for verification
                System.out.println("Board state after defense result:");
                printBoardState();
            }
        } catch (Exception e) {
            System.err.println("Error parsing defense result: " + message);
            e.printStackTrace();
        }
    }

    // Add this helper method to debug board state
    private void printBoardState() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (!board.isCellEmpty(row, col)) {
                    System.out.println("Piece at (" + row + "," + col + "): " +
                            board.getCellText(row, col) + " - Color: " +
                            (board.getCellColor(row, col).equals(boardMP.PLAYER_COLOR) ? "RED" : "GRAY"));
                }
            }
        }
    }

    private void handleAttackResult(String message) {
        try {
            if (message.contains("Result: \"")) {
                String result = message.substring(
                        message.indexOf("Result: \"") + 8,
                        message.indexOf("\"}")
                );

                // Get positions for both pieces
                int attackerRow = lastMoveFrom / 8;
                int attackerCol = lastMoveFrom % 8;
                int defenderRow = lastMoveTo / 8;
                int defenderCol = lastMoveTo % 8;

                if (result.equals("TIE")) {
                    // Remove both pieces involved in the tie
                    boardMP.removePiece(defenderRow, defenderCol);  // Remove opponent's piece
                    boardMP.removePiece(attackerRow, attackerCol);  // Remove our piece
                    System.out.println("Both pieces removed due to TIE at positions (" +
                            attackerRow + "," + attackerCol + ") and (" +
                            defenderRow + "," + defenderCol + ")");

                    // Print board state to verify removal
                    System.out.println("Board state after TIE:");
                    printBoardState();
                } else if (result.equals("LOSS")) {
                    // Remove our attacking piece
                    boardMP.removePiece(attackerRow, attackerCol);
                    System.out.println("Our piece removed due to LOSS at position (" +
                            attackerRow + "," + attackerCol + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing attack result: " + message);
            e.printStackTrace();
        }
    }

    // Add these class variables to track positions
    private int lastMoveFrom = -1;
    private int lastMoveTo = -1;
    private int lastAttackedPosition = -1;

    // Update handleOpponentMove to track positions
    private void handleOpponentMove(String message) {
        int fromIndex = Integer.parseInt(message.substring(message.indexOf("From: \"") + 7, message.indexOf("\", To:")));
        int toIndex = Integer.parseInt(message.substring(message.indexOf("To: \"") + 5, message.indexOf("\"}")));

        lastMoveFrom = fromIndex;
        lastAttackedPosition = toIndex;

        int fromRow = fromIndex / 8;
        int fromCol = fromIndex % 8;
        int toRow = toIndex / 8;
        int toCol = toIndex % 8;

        // Keep track of opponent piece (always gray)
        boardMP.updateCell(toRow, toCol, "X", boardMP.OPPONENT_COLOR);
        // Set the vacated cell back to empty (green)
        boardMP.removePiece(fromRow, fromCol);

        System.out.println("Opponent moved from " + fromIndex + " to " + toIndex);
    }

    // Update makeMove to track our moves
    // In de makeMove methode van serverConnection, voeg toe:
    private void makeMove() {
        if (!myTurn) {
            return;
        }

        // First verify that we still have pieces to move
        boolean havePieces = false;
        Map<String, Integer> pieceCount = new HashMap<>();
        List<int[]> movablePieces = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getCellColor(row, col).equals(boardMP.PLAYER_COLOR)) {
                    String piece = board.getCellText(row, col);
                    if (!piece.equals("BOMB") && !piece.equals("FLAG")) {  // Only count movable pieces
                        havePieces = true;
                        pieceCount.put(piece, pieceCount.getOrDefault(piece, 0) + 1);
                        movablePieces.add(new int[]{row, col});
                        System.out.println("Found piece: " + piece + " at (" + row + "," + col + ")");
                    }
                }
            }
        }

        if (!havePieces) {
            System.out.println("No pieces left to move!");
            return;
        }

        // Print current piece count for debugging
        System.out.println("Current piece count:");
        pieceCount.forEach((piece, count) ->
                System.out.println(piece + ": " + count));

        // Try strategic move first
        if (multiplayerMove.makeStrategicMove(board, out, previousMoves)) {
            myTurn = false;
            return;
        }

        // If no strategic move was possible, try a random move
        System.out.println("Attempting random move as fallback...");
        Collections.shuffle(movablePieces);

        for (int[] piecePos : movablePieces) {
            int startRow = piecePos[0];
            int startCol = piecePos[1];
            String piece = board.getCellText(startRow, startCol);

            // Define possible moves based on piece type
            List<int[]> possibleMoves = new ArrayList<>();

            if (piece.equals("SCOUT")) {
                // Scout can move multiple squares in straight lines
                for (int i = 1; i < 8; i++) {
                    // Only add moves that are within bounds
                    if (startCol + i < 8) possibleMoves.add(new int[]{startRow, startCol + i});
                    if (startCol - i >= 0) possibleMoves.add(new int[]{startRow, startCol - i});
                    if (startRow + i < 8) possibleMoves.add(new int[]{startRow + i, startCol});
                    if (startRow - i >= 0) possibleMoves.add(new int[]{startRow - i, startCol});
                }
            } else {
                // Regular pieces move one square
                if (startRow > 0) possibleMoves.add(new int[]{startRow - 1, startCol});
                if (startRow < 7) possibleMoves.add(new int[]{startRow + 1, startCol});
                if (startCol > 0) possibleMoves.add(new int[]{startRow, startCol - 1});
                if (startCol < 7) possibleMoves.add(new int[]{startRow, startCol + 1});
            }

            Collections.shuffle(possibleMoves);

            for (int[] move : possibleMoves) {
                if (multiplayerMove.makeMove(board, startRow, startCol, move[0], move[1], out)) {
                    String moveString = startRow + "," + startCol + "->" + move[0] + "," + move[1];
                    previousMoves.add(moveString);
                    System.out.println("Made random move: " + moveString);
                    myTurn = false;
                    return;
                }
            }
        }

        System.out.println("No valid moves found at all!");
    }


    private void placeOpponentPiece(String message) {
        int startIndex = message.indexOf("{At: \"") + 6;
        int endIndex = message.indexOf("\"}", startIndex);
        int index = Integer.parseInt(message.substring(startIndex, endIndex));

        int row = index / 8;
        int col = index % 8;

        // Place opponent piece (always gray, marked with "X")
        boardMP.updateCell(row, col, "X", boardMP.OPPONENT_COLOR);
        System.out.println("Opponent placed piece at " + index);
    }

    public void close() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (client != null && !client.isClosed()) client.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}