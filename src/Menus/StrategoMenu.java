package Menus;

import stratego.gui.MultiplayerGUI;
import stratego.gui.SinglePlayerGUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class StrategoMenu extends JPanel {

    private Image backgroundImage;

    public StrategoMenu() {
        // Laad de achtergrondafbeelding
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/strategobackground.png")).getImage();

        // Laad het aangepaste lettertype
        Font customFont = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(110f);  // Lettergrootte aanpassen
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Layout instellen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Ruimte tussen componenten

        // Titel toevoegen
        JLabel titleLabel = new JLabel("STRATEGO");
        titleLabel.setFont(customFont);  // Pas aangepast lettertype toe
        titleLabel.setForeground(Color.WHITE);  // Witte tekstkleur
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centreer de tekst
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Titel centreren
        add(titleLabel, gbc);

        // Knoppenpaneel maken
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Achtergrond doorzichtig
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 0, 10, 0); // Ruimte tussen knoppen
        buttonGbc.gridx = 0;

        // Knoppen toevoegen
        buttonGbc.gridy = 0;
        buttonPanel.add(new StrategoButtons("Singleplayer", "images/strategobutton.png", e -> startSinglePlayer()), buttonGbc);

        buttonGbc.gridy++;
        buttonPanel.add(new StrategoButtons("Multiplayer", "images/strategobutton.png", e -> startMultiplayer()), buttonGbc);

        buttonGbc.gridy++;
        buttonPanel.add(new StrategoButtons("Options", "images/strategobutton.png", e -> goToOptions()), buttonGbc);

        buttonGbc.gridy++;
        buttonPanel.add(new StrategoButtons("Back to Home", "images/strategobutton.png", e -> goToHome()), buttonGbc);

        // Voeg het knoppenpaneel toe aan het hoofdvenster
        gbc.gridy = 1;
        add(buttonPanel, gbc);
    }

    private void startSinglePlayer() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new SinglePlayerGUI()); // Placeholder voor de singleplayer GUI
            frame.revalidate();
            frame.repaint();
        }
    }

    private void startMultiplayer() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new MultiplayerGUI()); // Placeholder voor de multiplayer GUI
            frame.revalidate();
            frame.repaint();
        }
    }

    private void goToOptions() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new Options("StrategoMenu")); // Terugknop toevoegen aan Opties
            frame.revalidate();
            frame.repaint();
        }
    }

    private void goToHome() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.setContentPane(new Home());
            frame.revalidate();
            frame.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Zorg voor correcte schildering
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Achtergrondafbeelding tekenen
    }
}
