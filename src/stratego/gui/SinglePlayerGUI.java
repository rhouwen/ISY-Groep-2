package stratego.gui;

import stratego.game.pieces.Piece;
import stratego.game.pieces.Piecefactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinglePlayerGUI extends JPanel {

    private JLabel selectedPieceLabel;
    private Map<String, PieceInfo> pieceInfoMap; // Slaat informatie over elk stuk op

    public SinglePlayerGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); 

        // Pieces maken, de array moet hier door worden gegeven, dat werkt nog niet
        List<Piece> pieces = Piecefactory.createTeamPieces("Rood");
        pieceInfoMap = initializePieceInfo(pieces);

        // Titel en geselecteerd stuk
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 50));
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

        // Paneel met tabelachtige lijst voor stukken
        JPanel pieceListPanel = new JPanel();
        pieceListPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Een rij per stuk
        pieceListPanel.setBackground(new Color(30, 30, 30));
        JScrollPane scrollPane = new JScrollPane(pieceListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        for (String pieceName : pieceInfoMap.keySet()) {
            PieceInfo info = pieceInfoMap.get(pieceName);
            pieceListPanel.add(createPieceRow(pieceName, info));
        }

        add(scrollPane, BorderLayout.CENTER);

        // Terugknop
        JButton backButton = new JButton("Terug naar Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 70, 70));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        backButton.addActionListener(e -> returnToMenu());
        add(backButton, BorderLayout.SOUTH);
    }
    // de stuk info in een map
    private Map<String, PieceInfo> initializePieceInfo(List<Piece> pieces) {
        Map<String, PieceInfo> infoMap = new HashMap<>();
        for (Piece piece : pieces) {
            String pieceName = piece.getName();
            infoMap.putIfAbsent(pieceName, new PieceInfo(pieceName, 0, 0));
            infoMap.get(pieceName).incrementTotal();
        }
        return infoMap;
    }

    // De rijen voor de stukken
    private JPanel createPieceRow(String pieceName, PieceInfo info) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        rowPanel.setBackground(new Color(50, 50, 50));
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel nameLabel = new JLabel(pieceName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel countLabel = new JLabel("Aantal: " + info.getTotal() + ", Geplaatst: " + info.getPlaced());
        countLabel.setForeground(Color.LIGHT_GRAY);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton selectButton = new JButton("Selecteer");
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.setForeground(Color.WHITE);
        selectButton.setBackground(new Color(70, 70, 70));
        selectButton.setFocusPainted(false);
        selectButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        selectButton.addActionListener(e -> selectPiece(pieceName));
        selectButton.addMouseListener(new HoverEffect(selectButton, new Color(90, 90, 90), new Color(70, 70, 70)));

        rowPanel.add(nameLabel);
        rowPanel.add(countLabel);
        rowPanel.add(selectButton);

        return rowPanel;
    }

    // Geselecteerde stuk
    private void selectPiece(String pieceName) {
        PieceInfo info = pieceInfoMap.get(pieceName);
        if (info.getPlaced() < info.getTotal()) {
            selectedPieceLabel.setText("Geselecteerd stuk: " + pieceName);
        } else {
            JOptionPane.showMessageDialog(this, "Geen " + pieceName + " meer beschikbaar om te plaatsen!", "Fout", JOptionPane.WARNING_MESSAGE);
        }
    }
    // Terug naar menu knop, alleen opent de verkeerde StrategoGUI (bord) of klopt dit wel zo?
    private void returnToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new StrategoGUI());
            frame.revalidate();
            frame.repaint();
        }
    }

    // Hover effect als je met muis erover gaat
    private static class HoverEffect extends java.awt.event.MouseAdapter {
        private final JButton button;
        private final Color hoverColor;
        private final Color defaultColor;

        public HoverEffect(JButton button, Color hoverColor, Color defaultColor) {
            this.button = button;
            this.hoverColor = hoverColor;
            this.defaultColor = defaultColor;
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(hoverColor);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(defaultColor);
        }
    }

    // Totaal en geplaatst, alvast klasses voor als er 1 word geplaatst
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
