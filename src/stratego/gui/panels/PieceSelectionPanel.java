package stratego.gui.panels;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceSelectionPanel extends JPanel {

    private JLabel selectedPieceLabel;
    private Map<String, List<Piece>> pieceMap; // Map voor beschikbare stukken
    private String selectedPieceName; // Huidig geselecteerd stuk
    private String team; // ✅ Teamkleur opslaan

    public PieceSelectionPanel(String teamColor) {
        this.team = teamColor; // ✅ Team opslaan

        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(null);

        List<Piece> pieces = Piecefactory.createTeamPieces(teamColor);
        pieceMap = initializePieceMap(pieces);

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
        selectedPieceLabel.setPreferredSize(new Dimension(200, 25));
        selectedPieceLabel.setForeground(new Color(200, 200, 200));
        selectedPieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(selectedPieceLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        JPanel pieceListPanel = new JPanel();
        pieceListPanel.setLayout(new BoxLayout(pieceListPanel, BoxLayout.Y_AXIS));
        pieceListPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(pieceListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        for (String pieceName : pieceMap.keySet()) {
            pieceListPanel.add(createPieceButton(pieceName));

            Component spacer = Box.createVerticalStrut(10);
            spacer.setBackground(null);
            pieceListPanel.add(spacer);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    private Map<String, List<Piece>> initializePieceMap(List<Piece> pieces) {
        Map<String, List<Piece>> map = new HashMap<>();
        for (Piece piece : pieces) {
            map.computeIfAbsent(piece.getName(), k -> new ArrayList<>()).add(piece);
        }
        return map;
    }

    private JButton createPieceButton(String pieceName) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(new Color(50, 50, 50));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);

        JLabel nameLabel = new JLabel(pieceName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        button.add(nameLabel, BorderLayout.WEST);

        JLabel countLabel = new JLabel("Aantal: " + pieceMap.get(pieceName).size());
        countLabel.setForeground(Color.LIGHT_GRAY);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        button.add(countLabel, BorderLayout.EAST);

        button.addActionListener(e -> selectPiece(pieceName));

        return button;
    }

    private void selectPiece(String pieceName) {
        List<Piece> pieces = pieceMap.get(pieceName);
        if (pieces != null && !pieces.isEmpty()) {
            selectedPieceName = pieceName;
            selectedPieceLabel.setText("Geselecteerd stuk: " + pieceName);
        } else {
            JOptionPane.showMessageDialog(this, "Geen " + pieceName + " meer beschikbaar!", "Fout", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isEmpty() {
        return pieceMap.values().stream().allMatch(List::isEmpty);
    }

    public Piece getPieceToPlace(String pieceName) {
        List<Piece> pieces = pieceMap.get(pieceName);
        if (pieces != null && !pieces.isEmpty()) {
            Piece piece = pieces.remove(0);
            System.out.println("🔹 VOOR aanpassen: " + piece.getName() + " (Team: " + piece.getTeam() + ")");

            // ✅ **Forceer de juiste teamkleur**
            piece.setTeam(this.team);

            System.out.println("✅ NA aanpassen: " + piece.getName() + " (Team: " + piece.getTeam() + ")");
            return piece;
        }
        return null;
    }




    public void addPieceBack(String pieceName, Piece piece) {
        pieceMap.computeIfAbsent(pieceName, k -> new ArrayList<>()).add(piece);
        updatePieceCounts();
    }

    private void updatePieceCounts() {
        for (Component comp : getComponents()) {
            if (comp instanceof JScrollPane) {
                JPanel pieceListPanel = (JPanel) ((JScrollPane) comp).getViewport().getView();
                for (Component buttonComp : pieceListPanel.getComponents()) {
                    if (buttonComp instanceof JButton) {
                        JButton button = (JButton) buttonComp;
                        String pieceName = ((JLabel) button.getComponent(0)).getText();
                        button.getComponent(1).setVisible(true);
                        ((JLabel) button.getComponent(1)).setText("Aantal: " + pieceMap.get(pieceName).size());
                    }
                }
            }
        }
    }

    public String getSelectedPiece() {
        return selectedPieceName != null ? selectedPieceName : "Geen stuk geselecteerd";
    }
}
