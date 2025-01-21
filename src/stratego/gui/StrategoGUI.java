package stratego.gui;

import stratego.game.pieces.Piece;
import stratego.game.Board;
import stratego.gui.panels.CapturedPiecesPanel;
import stratego.gui.panels.ScorePanel;

import javax.swing.*;
import java.awt.*;

public class StrategoGUI extends JPanel {
    private GUI gui;
    private Board board;

    public StrategoGUI(Board board) {
        setLayout(new BorderLayout());
        this.board = board;
        gui = new GUI(board);
        add(gui, BorderLayout.CENTER);

        // Linkerpaneel met geslagen stukken en score
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        leftPanel.add(new CapturedPiecesPanel("Geslagen stukken van rood"));
        leftPanel.add(new ScorePanel("Score"));
        leftPanel.add(new CapturedPiecesPanel("Geslagen stukken van blauw"));
        add(leftPanel, BorderLayout.WEST);
    }

    public void updateBoard(Board board) {
        this.board = board;
        JButton[][] cells = GUI.getCells();

        // Controleer of een rood stuk geselecteerd is
        boolean pieceSelected = ClickHandler.getInstance().hasSelectedPiece();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                if (board.isWaterTile(row, col)) {
                    GUI.updateCell(row, col, "", Color.BLACK);
                    cells[row][col].setEnabled(false); // Watercellen blijven disabled
                } else if (piece == null) {
                    GUI.updateCell(row, col, "", Color.GREEN);
                    cells[row][col].setEnabled(true); // Lege cellen blijven klikbaar
                } else {
                    boolean isAI = piece.getTeam().equalsIgnoreCase("Blue");
                    Color color = isAI ? Color.BLUE : Color.RED;
                    GUI.updateCell(row, col, piece.getName(), color);

                    if (isAI && pieceSelected) {
                        cells[row][col].setEnabled(true); // ✅ Blauwe stukken klikbaar maken als er een rood stuk is geselecteerd
                    } else if (isAI) {
                        cells[row][col].setEnabled(false);
                    } else {
                        cells[row][col].setEnabled(true); // Rode stukken blijven aanklikbaar
                    }
                }
            }
        }

        revalidate();
        repaint();
    }
}
