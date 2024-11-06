import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGame extends JFrame {
    private JButton[] buttons = new JButton[9];
    private TicTacToeClient client = new TicTacToeClient();
    private boolean againstAI = true;  // Toggle for playing against AI

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonClickListener());
            add(buttons[i]);
        }
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton buttonClicked = (JButton) e.getSource();
            int index = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == buttonClicked) {
                    index = i;
                    break;
                }
            }
            if (index != -1 && client.getBoard()[index / 3][index % 3] == ' ') {
                client.makeMove(index / 3, index % 3, 'X');
                buttonClicked.setText("X");
                if (checkGameStatus()) return;

                if (againstAI) {
                    int[] aiMove = client.findBestMove();
                    if (client.makeMove(aiMove[0], aiMove[1], 'O')) {
                        buttons[aiMove[0] * 3 + aiMove[1]].setText("O");
                        checkGameStatus();
                    }
                }
            }
        }

        private boolean checkGameStatus() {
            if (client.evaluate() == 10) {
                JOptionPane.showMessageDialog(null, "AI wins!");
                resetButtons();
                return true;
            } else if (client.evaluate() == -10) {
                JOptionPane.showMessageDialog(null, "Player X wins!");
                resetButtons();
                return true;
            } else if (!client.isMovesLeft()) {
                JOptionPane.showMessageDialog(null, "The game is a tie!");
                resetButtons();
                return true;
            }
            return false;
        }

        private void resetButtons() {
            for (JButton button : buttons) {
                button.setText("");
            }
            client.resetBoard();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGame::new);
    }
}