import java.util.Arrays;

public class TicTacToeClient {
    private char[][] board;
    private char player = 'X';
    private char computer = 'O';

    public TicTacToeClient() {
        board = new char[3][3];
        resetBoard();
    }

    public void resetBoard() {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    public boolean makeMove(int row, int col, char currentPlayer) {
        if (board[row][col] == ' ') {
            board[row][col] = currentPlayer;
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

    public int[] findBestMove() {
        int bestVal = -1000;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = computer;
                    int moveVal = minimax(0, false);
                    board[i][j] = ' ';
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = isMax ? computer : player;
                    best = isMax ? Math.max(best, minimax(depth + 1, !isMax))
                            : Math.min(best, minimax(depth + 1, !isMax));
                    board[i][j] = ' ';
                }
            }
        }
        return best;
    }

    // Wijzig deze methode naar public
    public int evaluate() {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == computer) return 10;
                else if (board[row][0] == player) return -10;
            }
        }
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == computer) return 10;
                else if (board[0][col] == player) return -10;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == computer) return 10;
            else if (board[0][0] == player) return -10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == computer) return 10;
            else if (board[0][2] == player) return -10;
        }
        return 0;
    }

    public char[][] getBoard() {
        return board;
    }
}