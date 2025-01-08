package stratego.gui;

import stratego.game.Board;
import stratego.game.pieces.Piecefactory;
import stratego.gui.panels.PieceSelectionPanel;
import stratego.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;

public class SinglePlayerGUI extends JFrame {

    private StrategoGUI gameBoard;
    public static PieceSelectionPanel pieceSelectionPanel;

    public SinglePlayerGUI() {
        //screen settings
        setTitle("Stratego - Singleplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        //achtergrond via resoureceloader
        JPanel mainPanel = ResourceLoader.createBackgroundPanel("images/strategobackground.png");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        //leftpanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(150, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(Box.createVerticalStrut(20));

        //Buttons
        JButton backButton = new JButton("Back to home");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(20)); //spacing tussen knoppen

        JButton startButton = new JButton("Start game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(startButton);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        //centerpanel
        gameBoard = new StrategoGUI();
        JPanel boardPanel = new JPanel(new BorderLayout());
        boardPanel.setOpaque(false);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Interne marges
        boardPanel.add(gameBoard, BorderLayout.CENTER);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        Board board = new Board(10, 10);
        board.initializeBoard(new Piecefactory()); // Voeg stukken toe aan het bord
        gameBoard.updateBoard(board);

        //rightpanel
        pieceSelectionPanel = new PieceSelectionPanel("Rood");
        pieceSelectionPanel.setPreferredSize(new Dimension(300, 0));
        pieceSelectionPanel.setOpaque(false);
        mainPanel.add(pieceSelectionPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    public static String getSelectedPiece() {
        return pieceSelectionPanel.getSelectedPiece();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SinglePlayerGUI frame = new SinglePlayerGUI();
            frame.setVisible(true);
        });
    }
}
