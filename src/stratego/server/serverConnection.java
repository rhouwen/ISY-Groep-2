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
    private boardMP board;

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
                        // Call a method to make a move
                        makeMove();
                    }
                }

                if (inputMessage.startsWith("SVR GAME DEFENSE RESULT")) {
                    System.out.println("Resultaat defensie");
                }

                if (inputMessage.startsWith("SVR GAME ATTACK RESULT")) {
                    System.out.println("Resultaat aanval");
                }

                if (inputMessage.startsWith("SVR GAME MOVE")) {
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
        // Example move from (6, 0) to (5, 0)
        int startRow = 6;
        int startCol = 0;
        int endRow = 5;
        int endCol = 0;

        if (multiplayerMove.makeMove(board, startRow, startCol, endRow, endCol)) {
            int startIndex = startRow * 8 + startCol;
            int endIndex = endRow * 8 + endCol;
            out.println("move " + startIndex + " " + endIndex);
        } else {
            System.out.println("Invalid move");
        }
    }

    private void placeOpponentPiece(String message) {
        // Extract the index from the message
        int startIndex = message.indexOf("{At: \"") + 6;
        int endIndex = message.indexOf("\"}", startIndex);
        int index = Integer.parseInt(message.substring(startIndex, endIndex));

        // Calculate the row and column from the index
        int row = index / 8;
        int col = index % 8;

        // Update the cell on the board
        boardMP.updateCell(row, col, "", Color.RED);
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