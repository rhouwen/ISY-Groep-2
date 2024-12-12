package stratego.utils;

import javax.swing.*;
import java.awt.*;

public class Static {

    // Spelregels van Stratego
    public static String getGameRules() {
        return """
                Stratego Singleplayer Spelregels:
                1. Het doel is om de vlag van de tegenstander te vinden.
                2. Verplaats je stukken over het bord volgens hun bewegingsregels.
                3. Aanvallen en veroveren van vijandelijke stukken.
                4. Verlies niet je eigen vlag!
                """;
    }

    // Valideer een zet
    public static boolean isMoveValid(String piece, int startX, int startY, int endX, int endY) {
        if (!"Flag".equals(piece)) {
            return Math.abs(endX - startX) <= 1 && Math.abs(endY - startY) <= 1;
        }
        return false;
    }

    // Controleer of het spel voorbij is
    public static boolean isGameOver(boolean isFlagCaptured) {
        return isFlagCaptured;
    }

    // Toon de GUI en de spelregels
    private static void createAndShowGUI() {
        // Maak het hoofdvenster
        JFrame frame = new JFrame("Stratego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Knop om de spelregels te tonen
        JButton rulesButton = new JButton("Bekijk Spelregels");
        JTextArea rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        rulesTextArea.setWrapStyleWord(true);
        rulesTextArea.setLineWrap(true);

        // Stel actie in voor de regels-knop
        rulesButton.addActionListener(e -> rulesTextArea.setText(getGameRules()));

        // Layout van het venster
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(rulesButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(rulesTextArea), BorderLayout.CENTER);

        // Voeg inhoud aan frame toe
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        
        // Toon de regels bij game-start gedurende 5 seconden
        JOptionPane.showMessageDialog(frame, getGameRules(), "Spelregels", JOptionPane.INFORMATION_MESSAGE);
    }

    // Start de applicatie
    public static void main(String[] args) {
        // Toon spelregels aan het begin van het spel gedurende 5 seconden
        SwingUtilities.invokeLater(Static::createAndShowGUI);
    }
}
