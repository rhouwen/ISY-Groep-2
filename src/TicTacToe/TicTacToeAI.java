package TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class TicTacToeAI implements Runnable {
    private char[][] board;
    private char playerSymbol;
    private char opponentSymbol;
    private boolean symbolsAssigned = false;

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done = false;

    public TicTacToeAI() {
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

    public void startGame(String hostName, int portNumber) {
        try {
            client = new Socket(hostName, portNumber);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println("login " + "Player" + System.currentTimeMillis() % 1000);
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
            boolean myTurn = false;
            while ((message = in.readLine()) != null && !done) {
                System.out.println("Server: " + message);
                if (message.startsWith("SVR GAME MATCH")) {
                    handleMatchStart(message);
                } else if (message.contains("YOURTURN")) {
                    if (!symbolsAssigned) {
                        assignSymbols(message);
                    }
                    myTurn = true;

                    // Zorg ervoor dat de AI niet beweegt buiten zijn beurt
                    if (myTurn) {
                        int move = findBestMoveIndex();
                        if (move != -1) {
                            makeMove(move, playerSymbol);
                            sendMove(move);
                            if (checkWin(playerSymbol)) {
                                System.out.println("Ik heb gewonnen!");
                            }
                            myTurn = false;
                        }
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
        playerSymbol = message.contains("Player0") ? 'X' : 'O';
        opponentSymbol = playerSymbol == 'X' ? 'O' : 'X';
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
        TicTacToeAI client = new TicTacToeAI();
        client.startGame("127.0.0.1", 7789);
    }
}