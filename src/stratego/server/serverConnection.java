package stratego.server;

import java.awt.*;
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
    private Set<String> previousMoves = new HashSet<>();

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public serverConnection(boardMP board) {
        this.board = board;
        Map<String, String> settings = stratego.gui.strategoSettings.loadSettings("src/stratego/utils/settings.txt");
        this.ip_address = settings.getOrDefault("IP Address", "");
        this.port = settings.getOrDefault("Port", "");
        this.username = settings.getOrDefault("Username", "");
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
                    System.out.println("Match found");
                    placed = false;
                    board.resetBoard();
                }

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

                if (inputMessage.startsWith("SVR GAME DEFENSE RESULT")) {
                    handleDefenseResult(inputMessage);
                }

                if (inputMessage.startsWith("SVR GAME ATTACK RESULT")) {
                    handleAttackResult(inputMessage);
                }

                if (inputMessage.startsWith("SVR GAME MOVE")) {
                    handleOpponentMove(inputMessage);
                }

                if (inputMessage.startsWith("SVR GAME Opponent Placed")) {
                    System.out.println("Opponent placed their pieces");
                    placeOpponentPiece(inputMessage);
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Server error: " + e.getMessage());
            }
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
            if (message.contains("Result: \"")) {
                String result = message.substring(
                        message.indexOf("Result: \"") + 8,
                        message.indexOf("\"}")
                );

                // Get the position from the last known move
                int row = lastAttackedPosition / 8;
                int col = lastAttackedPosition % 8;

                if (result.equals("TIE") || result.equals("LOSS")) {
                    // Remove our piece
                    boardMP.removePiece(row, col);
                    System.out.println("Our piece removed at position " + lastAttackedPosition + " (Result: " + result + ")");

                    // If it's a TIE, also remove the opponent's piece
                    if (result.equals("TIE")) {
                        // The opponent's piece was at the attacking position
                        int attackerRow = lastMoveFrom / 8;
                        int attackerCol = lastMoveFrom % 8;
                        boardMP.removePiece(attackerRow, attackerCol);
                        System.out.println("Opponent piece removed at position " + lastMoveFrom + " (TIE)");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing defense result: " + message);
            e.printStackTrace();
        }
    }

    private void handleAttackResult(String message) {
        try {
            if (message.contains("Result: \"")) {
                String result = message.substring(
                        message.indexOf("Result: \"") + 8,
                        message.indexOf("\"}")
                );

                if (result.equals("TIE") || result.equals("LOSS")) {
                    // Remove our attacking piece
                    int row = lastMoveTo / 8;
                    int col = lastMoveTo % 8;
                    boardMP.removePiece(row, col);
                    System.out.println("Our piece removed at position " + lastMoveTo + " (Result: " + result + ")");

                    // If it's a TIE, also remove the opponent's piece
                    if (result.equals("TIE")) {
                        // The opponent's piece was at the target position
                        boardMP.removePiece(row, col);
                        System.out.println("Opponent piece removed at position " + lastMoveTo + " (TIE)");
                    }
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
    private void makeMove() {
        if (!myTurn) {
            return;
        }

        if (multiplayerMove.makeStrategicMove(board, out, previousMoves)) {
            myTurn = false;
        } else {
            System.out.println("No valid strategic move found");
        }
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