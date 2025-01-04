package Menus;

import java.awt.FontFormatException;
import java.io.IOException;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.*;
import java.awt.*;

public class StrategoMenuRules extends JPanel {

    public StrategoMenuRules() {
        Font customFont = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Layout instellen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Ruimte tussen componenten

        // Titel toevoegen
        JLabel titleLabel = new JLabel("STRATEGO SPELREGELS");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Spelregels toevoegen
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setOpaque(false); // Transparante achtergrond



        // Ruimte toevoegen
        rulesPanel.add(Box.createVerticalStrut(20));

        // Spelregels als opsomming volgens HTML code
        JTextPane rulesTextPane = new JTextPane();
        rulesTextPane.setContentType("text/html"); // HTML opmaak inschakelen
        rulesTextPane.setText("""
    <html>
        <body style="font-family: Arial, sans-serif; font-size: 14px; color: black;">
            <ol>
                <li><strong>Doel:</strong> Vind de vlag van de tegenstander.</li>
                <li><strong>Beweging:</strong> Verplaats je stukken volgens hun bewegingsregels.</li>
                <li><strong>Aanvallen:</strong> Verover vijandelijke stukken.</li>
                <li><strong>Verdediging:</strong> Bescherm je eigen vlag!</li>
            </ol>
        </body>
    </html>
""");
        rulesTextPane.setEditable(false);
        rulesTextPane.setOpaque(false);
        rulesPanel.add(rulesTextPane);

        // Voeg alles toe aan de hoofdlayout
        gbc.gridy = 1;
        add(rulesPanel, gbc);

        // Knoppenpaneel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 0, 10, 0);
        buttonGbc.gridx = 0;

        // "Back to Menu" knop
        buttonGbc.gridy = 0;
        buttonPanel.add(new StrategoButtons("Back to Menu", "images/strategobutton.png", e -> goToMenu()), buttonGbc);

        // Voeg knoppenpaneel toe
        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    private void goToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new StrategoMenu()); // Zet terug naar het hoofdmenu
            frame.revalidate();
            frame.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY); // Een neutrale achtergrondkleur
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
