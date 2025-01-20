package stratego.gui;

import stratego.game.Board;
import stratego.gui.panels.PieceSelectionPanel;
import stratego.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;

public class SinglePlayerGUI extends JFrame {

    private static StrategoGUI gameBoard;
    public static PieceSelectionPanel pieceSelectionPanel;

    public SinglePlayerGUI() {
        setTitle("Stratego - Singleplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        JPanel mainPanel = ResourceLoader.createBackgroundPanel("images/strategobackground.png");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(150, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Back to home");
        buttonPanel.add(backButton);

        JButton startButton = new JButton("Start game");
        startButton.addActionListener(e -> {
            ClickHandler handler = ClickHandler.getInstance();
            if (handler != null) {
                handler.startGameFromButton();
            } else {
                System.out.println("⚠️ ClickHandler is niet geïnitialiseerd!");
            }
        });
        buttonPanel.add(startButton);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Maak een Board-object en geef het door aan de StrategoGUI
        Board board = new Board(10, 10);
        board.initializeBoard(null); // Initializeer met een lege state

        gameBoard = new StrategoGUI(board); // Geef het bord door aan StrategoGUI
        ClickHandler.getInstance(board); // Initialiseer ClickHandler met het bord

        gameBoard.updateBoard(board);
        mainPanel.add(gameBoard, BorderLayout.CENTER);

        pieceSelectionPanel = new PieceSelectionPanel("Blauw");
        mainPanel.add(pieceSelectionPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    public static String getSelectedPiece() {
        return pieceSelectionPanel.getSelectedPiece();
    }

    public static StrategoGUI getGameBoard() {
        return gameBoard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SinglePlayerGUI frame = new SinglePlayerGUI();
            frame.setVisible(true);
        });
    }
}
