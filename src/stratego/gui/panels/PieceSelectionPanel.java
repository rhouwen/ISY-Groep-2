package stratego.gui.panels;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceSelectionPanel extends JPanel {

    private JLabel selectedPieceLabel;
    private Map<String, PieceInfo> pieceInfoMap;

    public PieceSelectionPanel(String teamColor) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(null);

        List<Piece> pieces = Piecefactory.createTeamPieces(teamColor);
        pieceInfoMap = initializePieceInfo(pieces);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Kies een Stuk");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        selectedPieceLabel = new JLabel("Geselecteerd stuk: Geen");
        selectedPieceLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        selectedPieceLabel.setForeground(new Color(200, 200, 200));
        selectedPieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(selectedPieceLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        //stukkenlist
        JPanel pieceListPanel = new JPanel();
        pieceListPanel.setLayout(new BoxLayout(pieceListPanel, BoxLayout.Y_AXIS));
        pieceListPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(pieceListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        for (String pieceName : pieceInfoMap.keySet()) {
            PieceInfo info = pieceInfoMap.get(pieceName);
            pieceListPanel.add(createPieceButton(pieceName, info));

            //maakt de spacing tussen de stukken transparant
            Component spacer = Box.createVerticalStrut(10);
            spacer.setBackground(null);
            spacer.isOpaque();
            pieceListPanel.add(spacer);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    private Map<String, PieceInfo> initializePieceInfo(List<Piece> pieces) {
        Map<String, PieceInfo> infoMap = new HashMap<>();
        for (Piece piece : pieces) {
            String pieceName = piece.getName();
            infoMap.putIfAbsent(pieceName, new PieceInfo(pieceName, 0, 0));
            infoMap.get(pieceName).incrementTotal();
        }
        return infoMap;
    }

    private JButton createPieceButton(String pieceName, PieceInfo info) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(250, 50)); // Smalle breedte
        button.setBackground(new Color(50, 50, 50)); // Donkere achtergrond
        button.setBorderPainted(false); // Geen border
        button.setFocusPainted(false);
        button.setOpaque(true);

        JLabel nameLabel = new JLabel(pieceName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(new EmptyBorder(0, 10, 0, 0)); // Ruimte links
        button.add(nameLabel, BorderLayout.WEST);

        JLabel countLabel = new JLabel("Aantal: " + info.getTotal() + ", Geplaatst: " + info.getPlaced());
        countLabel.setForeground(Color.LIGHT_GRAY);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countLabel.setBorder(new EmptyBorder(0, 0, 0, 10)); // Ruimte rechts
        button.add(countLabel, BorderLayout.EAST);

        //action listener selecteren van stukken
        button.addActionListener(e -> selectPiece(pieceName));

        return button;
    }

    private void selectPiece(String pieceName) {
        PieceInfo info = pieceInfoMap.get(pieceName);
        if (info.getPlaced() < info.getTotal()) {
            selectedPieceLabel.setText("Geselecteerd stuk: " + pieceName);
        } else {
            JOptionPane.showMessageDialog(this, "Geen " + pieceName + " meer beschikbaar!", "Fout", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static class PieceInfo {
        private final String name;
        private int total;
        private int placed;

        public PieceInfo(String name, int total, int placed) {
            this.name = name;
            this.total = total;
            this.placed = placed;
        }

        public void incrementTotal() {
            total++;
        }

        public int getTotal() {
            return total;
        }

        public int getPlaced() {
            return placed;
        }
    }
}
