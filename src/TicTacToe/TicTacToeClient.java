package TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class TicTacToeClient implements Runnable {
    private char[][] board;
    private char playerSymbol;
    private char opponentSymbol;
    private boolean symbolsAssigned = false;
    private String username;

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    public TicTacToeClient(String username) {
        this.username = username;
        board = new char[3][3];
        resetBoard();
    }

    public void resetBoard() {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    public boolean makeMove(int index, char currentPlayer) {
        int row = index / 3;
        int col = index % 3;
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
    }

    public void startGame(String hostName, int portNumber) {
        try {
            client = new Socket(hostName, portNumber);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Unieke login om identificatie te waarborgen
            out.println("login " + username);
            out.println("subscribe tic-tac-toe");

            new Thread(this).start();  // Start een thread om serverberichten te ontvangen
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null && !done) {
                System.out.println("Server: " + message);
                if (message.startsWith("SVR GAME MATCH")) {
                    handleMatchStart(message);
                } else if (message.contains("YOURTURN")) {
                    if (!symbolsAssigned) {
                        assignSymbols(message);
                    }
                    int move = findBestMoveIndex();
                    if (move != -1) {
                        makeMove(move, playerSymbol);
                        sendMove(move);
                    }
                } else if (message.startsWith("SVR GAME MOVE")) {
                    handleOpponentMove(message);
                } else if (message.startsWith("SVR GAME WIN") || message.startsWith("SVR GAME LOSS") || message.startsWith("SVR GAME DRAW")) {
                    System.out.println("Game result: " + message);
                    resetBoard();  // Reset het bord voor een nieuwe game indien nodig
                    symbolsAssigned = false; // Reset symbolen voor de volgende game
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void sendMove(int index) {
        out.println("move " + index);
    }

    private void handleMatchStart(String message) {
        System.out.println("Match started: " + message);
    }

    private void handleOpponentMove(String message) {
        try {
            int moveStartIndex = message.indexOf("MOVE: \"") + 7;
            int moveEndIndex = message.indexOf("\"", moveStartIndex);
            int move = Integer.parseInt(message.substring(moveStartIndex, moveEndIndex).trim());

            makeMove(move, opponentSymbol);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Fout bij het verwerken van de zet van de tegenstander: " + e.getMessage());
        }
    }

    private void assignSymbols(String message) {
        // Als de clientnaam overeenkomt met de speler die aan zet is, krijgt deze de 'X' en de andere de 'O'
        if (message.contains("Player0")) {  // Pas aan afhankelijk van de unieke login die je gebruikt
            playerSymbol = 'X';
            opponentSymbol = 'O';
        } else {
            playerSymbol = 'O';
            opponentSymbol = 'X';
        }
        symbolsAssigned = true;
        System.out.println("Symbolen toegewezen: Jij bent '" + playerSymbol + "', tegenstander is '" + opponentSymbol + "'");
    }

    public void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int findBestMoveIndex() {
        int bestVal = -1000;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            if (board[row][col] == ' ') {
                board[row][col] = playerSymbol;
                int moveVal = minimax(0, false);
                board[row][col] = ' ';
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax) {
        int score = evaluate();

        if (score == 10 || score == -10) return score;
        if (!isMovesLeft()) return 0;

        int best = isMax ? -1000 : 1000;

        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            if (board[row][col] == ' ') {
                board[row][col] = isMax ? playerSymbol : opponentSymbol;
                best = isMax ? Math.max(best, minimax(depth + 1, !isMax))
                        : Math.min(best, minimax(depth + 1, !isMax));
                board[row][col] = ' ';
            }
        }
        return best;
    }

    public int evaluate() {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == playerSymbol) return 10;
                else if (board[row][0] == opponentSymbol) return -10;
            }
        }
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == playerSymbol) return 10;
                else if (board[0][col] == opponentSymbol) return -10;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == playerSymbol) return 10;
            else if (board[0][0] == opponentSymbol) return -10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == playerSymbol) return 10;
            else if (board[0][2] == opponentSymbol) return -10;
        }
        return 0;
    }

    public boolean checkWin(char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        // Check diagonals
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }
        return false;
    }

    public boolean isMovesLeft() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String username = "Player123";
        TicTacToeClient client = new TicTacToeClient(username);
        client.startGame("127.0.0.1", 7789);
    }
}