package stratego.gui;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;
import stratego.gui.panels.CapturedPiecesPanel;
import stratego.gui.panels.ScorePanel;
import stratego.game.Board;

import javax.swing.*;
import java.awt.*;

public class StrategoGUI extends JPanel {
    // Abstracte class voor gedeelde GUI functies

    private GUI gui;

    public StrategoGUI(Board board) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Geef het board door aan de GUI
        gui = new GUI(board);
        add(gui, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new GridLayout(3, 1)); // 3 rijen, 1 kolom
        leftPanel.add(new CapturedPiecesPanel("Geslagen stukken van rood"));
        leftPanel.add(new ScorePanel("Score"));
        leftPanel.add(new CapturedPiecesPanel("Geslagen stukken van blauw"));
        add(leftPanel, BorderLayout.WEST);
    }

    public void updateBoard(Board board) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.isWaterTile(row, col)) {
                    GUI.updateCell(row, col, "", Color.black); // Water
                } else {
                    Piece piece = board.getPieceAt(row, col);
                    if (piece == null) {
                        GUI.updateCell(row, col, "", Color.GREEN); // Leeg = groen
                    } else {
                        Color color = piece.getTeam().equalsIgnoreCase("Red") ? Color.RED : Color.BLUE;
                        GUI.updateCell(row, col, piece.getName(), color);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stratego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Board board = new Board(10, 10);
        board.initializeBoard(new Piecefactory());

        StrategoGUI strategoGUI = new StrategoGUI(board); // Geef het board door
        frame.add(strategoGUI);

        strategoGUI.updateBoard(board);

        frame.setVisible(true);
    }
}
