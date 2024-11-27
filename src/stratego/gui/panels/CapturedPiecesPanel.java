package stratego.gui.panels;

import javax.swing.*;
import java.awt.*;

public class CapturedPiecesPanel extends JPanel {

    //Laat zien welke stukken er geslagen zijn

    public CapturedPiecesPanel(String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title)); // Titel voor het paneel

        JLabel placeholderLabel = new JLabel("Nog geen stukken geslagen", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(placeholderLabel, BorderLayout.CENTER);
    }
}
