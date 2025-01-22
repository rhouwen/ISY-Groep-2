package Menus;

import java.awt.FontFormatException;
import java.io.IOException;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.*;
import java.awt.*;

public class StrategoMenuRules extends JPanel {

    private Font customFont;

    public StrategoMenuRules() {
        // Probeer het lettertype te laden
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 30); // Vervang met standaardlettertype als iets misgaat
        }

        // Layout instellen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Geef ruimte tussen de componenten

        // Titel toevoegen
        JLabel titleLabel = new JLabel("STRATEGO SPELREGELS");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Spelregelcontent toevoegen
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS)); // Verticaal layout
        rulesPanel.setOpaque(false); // Transparante achtergrond

        // Veelgestelde vragen sectie toevoegen
        addFAQSection(rulesPanel);

        // Voeg scrollpaneel toe
        gbc.gridy = 1;
        JScrollPane scrollPane = new JScrollPane(rulesPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, gbc);

        // Knoppenpaneel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 0, 10, 0);
        buttonGbc.gridx = 0;

        // "Back to Menu" knop
        buttonGbc.gridy = 0;
        buttonPanel.add(new StrategoButtons("Back to Menu", "images/strategobutton.png", e -> goToMenu()), buttonGbc);

        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    /**
     * Voeg een sectie met veelgestelde vragen toe.
     */
    private void addFAQSection(JPanel parent) {
        JLabel faqTitle = new JLabel("Veelgestelde vragen bij Stratego".toUpperCase());
        faqTitle.setFont(customFont.deriveFont(22f));
        faqTitle.setForeground(Color.WHITE);
        faqTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(faqTitle);
        parent.add(Box.createVerticalStrut(10));

        // Voeg FAQ items toe
        addFAQItem(parent, "Wie kan een maarschalk verslaan?",
                """
                <ul>
                    <li>Een maarschalk heeft de <b>hoogste rang</b>, maar is kwetsbaar.</li>
                    <li>Een <b>spion</b> kan de maarschalk verslaan als deze aanvalt.</li>
                    <li>Als een maarschalk op een <b>bom</b> stoot, wordt deze uitgeschakeld. Alleen een mineur kan de bom neutraliseren.</li>
                </ul>
                """
        );
        addFAQItem(parent, "Wat kan een spion in Stratego?",
                """
                <ul>
                    <li>Een spion is het enige <b>stuk</b> dat een maarschalk kan verslaan in een aanval.</li>
                    <li>De spion sneuvelt tegen alle andere stukken wanneer deze wordt aangevallen.</li>
                    <li>Gebruik de spion tactisch om de maarschalk van de tegenstander te slim af te zijn.</li>
                </ul>
                """
        );
        addFAQItem(parent, "Wie kan de bom uitschakelen in Stratego?",
                """
                <ul>
                    <li>Een <b>mineur</b> is het enige stuk dat een bom kan uitschakelen door deze aan te vallen.</li>
                    <li>Bommen kunnen niet worden verplaatst en vormen een verdedigingslinie.</li>
                    <li>Als een ander stuk dan een mineur een bom aanvalt, zal dit stuk sneuvelen.</li>
                </ul>
                """
        );
    }

    /**
     * Hulpmethode: Voeg een enkele veelgestelde vraag toe in de interface
     */
    private void addFAQItem(JPanel parent, String question, String answer) {
        JPanel faqItemPanel = new JPanel();
        faqItemPanel.setLayout(new BoxLayout(faqItemPanel, BoxLayout.Y_AXIS));
        faqItemPanel.setOpaque(false);

        JButton questionButton = new JButton(question);
        questionButton.setFont(customFont.deriveFont(18f));
        questionButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextPane answerTextPane = new JTextPane();
        answerTextPane.setContentType("text/html");
        answerTextPane.setText("""
                <html>
                    <body style="font-family: Arial, sans-serif; font-size: 14px; color: black;">
                    %s
                    </body>
                </html>
                """.formatted(answer));
        answerTextPane.setEditable(false);
        answerTextPane.setOpaque(false);
        answerTextPane.setVisible(false); // Verborgen bij start

        questionButton.addActionListener(e -> answerTextPane.setVisible(!answerTextPane.isVisible()));

        faqItemPanel.add(questionButton);
        faqItemPanel.add(answerTextPane);
        parent.add(faqItemPanel);
        parent.add(Box.createVerticalStrut(10)); // Ruimte tussen FAQ-items
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
