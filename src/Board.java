// Board.java
public class Board {
    private char[] board;
    private char currentPlayer;

    public Board() {
        board = new char[9];
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
        }
        currentPlayer = 'X';
    }

    public boolean makeMove(int position) {
        if (position < 0 || position >= 9 || board[position] != ' ') {
            return false;
        }
        board[position] = currentPlayer;
        return true;
    }

    public boolean checkForWin() {
        return (checkLine(0, 1, 2) ||  // first row
                checkLine(3, 4, 5) ||  // second row
                checkLine(6, 7, 8) ||  // third row
                checkLine(0, 3, 6) ||  // first column
                checkLine(1, 4, 7) ||  // second column
                checkLine(2, 5, 8) ||  // third column
                checkLine(0, 4, 8) ||  // diagonal
                checkLine(2, 4, 6));   // anti diagonal
    }

    private boolean checkLine(int a, int b, int c) {
        return board[a] == currentPlayer && board[a] == board[b] && board[a] == board[c];
    }

    public boolean isBoardFull() {
        for (char cell : board) {
            if (cell == ' ') {
                return false;
            }
        }
        return true;
    }

    public void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
        }
        currentPlayer = 'X';
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char[] getBoard() {
        return board;
    }
}