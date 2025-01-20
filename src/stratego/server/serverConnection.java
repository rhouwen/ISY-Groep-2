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
                if (inputMessage.startsWith("SVR GAME MATCH")) {
                    System.out.println("Match gevonden");
                    placed = false;
                }

                if (inputMessage.startsWith("SVR GAME YOURTURN")) {
                    if (!placed) {
                        System.out.println("Wij moeten stukken plaatsen");
                        placePieces();
                        placed = true;
                    } else {
                        System.out.println("Wij moeten een zet doen");
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
                    System.out.println("Tegenstander heeft stukken geplaatst");
                    placeOpponentPiece(inputMessage);
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Serverfout: " + e.getMessage());
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
                    out.println("place " + index + " " + piece);
                }
            }
        }
    }

    private void makeMove() {
        if (!myTurn) {
            return;
        }

        boolean moveMade = false;
        for (int startRow = 6; startRow <= 7 && !moveMade; startRow++) {
            for (int startCol = 0; startCol <= 7 && !moveMade; startCol++) {
                for (int endRow = 0; endRow <= 7 && !moveMade; endRow++) {
                    for (int endCol = 0; endCol <= 7 && !moveMade; endCol++) {
                        String move = startRow + "," + startCol + "->" + endRow + "," + endCol;
                        if (!previousMoves.contains(move) && multiplayerMove.makeMove(board, startRow, startCol, endRow, endCol, out)) {
                            previousMoves.add(move);
                            moveMade = true;
                            myTurn = false; // Set myTurn to false after making a move
                            System.out.println("Move made from " + (startRow * 8 + startCol) + " to " + (endRow * 8 + endCol));
                        }
                    }
                }
            }
        }
        if (!moveMade) {
            System.out.println("No valid move found");
        }
    }

    private void handleDefenseResult(String message) {
        String result = message.substring(message.indexOf("Result: \"") + 9, message.indexOf("\"}"));
        if (result.equals("LOSS")) {
            // Remove the losing piece from the board
            int index = Integer.parseInt(message.substring(message.indexOf("At: \"") + 5, message.indexOf("\", Rank:")));
            int row = index / 8;
            int col = index % 8;
            boardMP.updateCell(row, col, "", board.customGreen);
        }
        System.out.println("Defense result: " + result);
    }

    private void handleAttackResult(String message) {
        String result = message.substring(message.indexOf("Result: \"") + 9, message.indexOf("\"}"));
        if (result.equals("LOSS")) {
            // Remove the losing piece from the board
            int index = Integer.parseInt(message.substring(message.indexOf("At: \"") + 5, message.indexOf("\", Rank:")));
            int row = index / 8;
            int col = index % 8;
            boardMP.updateCell(row, col, "", board.customGreen);
        }
        System.out.println("Attack result: " + result);
    }

    private void handleOpponentMove(String message) {
        int fromIndex = Integer.parseInt(message.substring(message.indexOf("From: \"") + 7, message.indexOf("\", To:")));
        int toIndex = Integer.parseInt(message.substring(message.indexOf("To: \"") + 5, message.indexOf("\"}")));

        int fromRow = fromIndex / 8;
        int fromCol = fromIndex % 8;
        int toRow = toIndex / 8;
        int toCol = toIndex % 8;

        String piece = board.getCellText(fromRow, fromCol);
        boardMP.updateCell(toRow, toCol, piece, Color.GRAY); // Ensure opponent's pieces are grey
        boardMP.updateCell(fromRow, fromCol, "", board.customGreen);

        System.out.println("Opponent moved from " + fromIndex + " to " + toIndex);
    }

    private void placeOpponentPiece(String message) {
        int startIndex = message.indexOf("{At: \"") + 6;
        int endIndex = message.indexOf("\"}", startIndex);
        int index = Integer.parseInt(message.substring(startIndex, endIndex));

        int row = index / 8;
        int col = index % 8;

        boardMP.updateCell(row, col, "", Color.GRAY); // Ensure opponent's pieces are grey
    }

    public void close() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (client != null && !client.isClosed()) client.close();
            System.out.println("Verbinding verbroken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}