package stratego.gui;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    //toont de scores van beide teams (148 - score)

    public ScorePanel(String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title)); // Titel voor het paneel

        JLabel placeholderLabel = new JLabel("Score wordt hier weergegeven", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(placeholderLabel, BorderLayout.CENTER);
    }
}
