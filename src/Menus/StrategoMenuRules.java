package Menus;

import java.awt.FontFormatException;
import java.io.IOException;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StrategoMenuRules extends JPanel {

    private Font customFont;

    public StrategoMenuRules() {
        // Probeer de custom font te laden
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 18); // Vervang door standaard font indien iets misgaat
        }

        // Layout instellen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Ruimte tussen componenten

        // Titel toevoegen
        JLabel titleLabel = new JLabel("STRATEGO SPELREGELS");
        titleLabel.setFont(customFont.deriveFont(30f)); // Gebruik grotere custom font voor de titel
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Scrollpaneel toevoegen voor de regels
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setOpaque(false); // Transparante achtergrond

        // Eerste sectie: Verplaatsen
        addSection(rulesPanel, "Verplaatsen",
                """
                Bij Stratego verplaats je door een stuk een vakje naar links, naar rechts, naar voren of naar achteren te zetten. Je mag maar een speelstuk op een veld zetten. Je kunt stukken niet over andere stukken heen laten springen. Diagonaal plaatsen is ook niet toegestaan. Daarnaast mogen ze niet in de twee meren op het midden van het bord komen.<br><br>
                Met een speelstuk mag je niet ononderbroken tussen twee velden heen en weer blijven schuiven: 5 keer is de max. Het kan tot het verlies van een speelstuk leiden. Je mag een bom en een vlag ook nooit verplaatsen, dus deze twee stukken blijven het hele spel op hun plaats staan.<br><br>
                Verkenners krijgen uitzonderingen. Ze mogen onbeperkt over lege velden heen springen, maar ze kunnen niet over eigen of vijandelijke stukken heen. Ook over de meertjes kunnen ze niet springen. Met een verkenner kun je over grote afstand aanvallen, als de tussenliggende velden vrij zijn.
                """
        );

        // Tweede sectie: Aanvallen
        addSection(rulesPanel, "Aanvallen",
                """
                Je mag aanvallen als je direct voor, naast of achter een stuk van de tegenstander staat. Je neemt dan je stuk, tikt het vijandelijke stuk aan en noemt de rang van jouw stuk. De tegenstander moet dan ook zijn rang noemen. Het stuk met de laagste rang sneuvelt en moet van het bord af. Het winnende stuk neemt de plaats over van het gesneuvelde stuk. Val je iemand aan van een gelijke rang, dan moeten jullie beiden het veld ruimen.<br><br>
                Weet dat aanvallen nooit verplicht is, en alleen een verkenner over grotere afstand mag aanvallen.
                """
        );

        // Derde sectie: De rangen
        addSection(rulesPanel, "De rangen",
                """
                Bij Stratego heb je verschillende rangen. De maarschalk verslaat bijvoorbeeld een generaal en alle lagere rangen, terwijl de generaal alle kolonels en lagere rangen verslaat. Dat gaat zo door tot aan de laagste rang: de spion. Wat de juiste volgorde is, zie je aan de rand van het bord.<br><br>
                Bommen zorgen ervoor dat stukken sneuvelen. Kom je tegen een mineur? Dan wordt de bom onschadelijk gemaakt. De mineur neemt dan de plaats in van de bom.<br><br>
                Je kunt ook een spion hebben: de laagste in rang. Elk stuk dat hem aanvalt, wint. Maar de spion is niet nutteloos: als de spion de maarschalk aanvalt, wint deze. Andersom verliest de spion altijd.
                """
        );

        // Vierde sectie: Veelgemaakte fouten
        addSection(rulesPanel, "Veelgemaakte fouten",
                """
                De meeste fouten bij Stratego hebben te maken met de opstelling. Al je verkenners vooraan zetten of al je mineurs verspreiden, is geen goed idee. Bij het eindspel kom je erachter dat je opstelling eigenlijk al de helft van het spel was. Goed nadenken over een strategie, is dus heel belangrijk om te kunnen winnen.
                """
        );

        // Vijfde sectie: Veelgestelde vragen
        addFAQSection(rulesPanel);

        // Voeg regels toe aan layout
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

        // Voeg knoppenpaneel toe
        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    /**
     * Hulpmethode: Maak een sectie met een titel en bijbehorende tekst.
     */
    private void addSection(JPanel parent, String title, String content) {
        // Titel
        JLabel sectionTitle = new JLabel(title.toUpperCase());
        sectionTitle.setFont(customFont.deriveFont(22f)); // Gebruik custom font voor de sectietitel
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(sectionTitle);

        // Tekst
        JTextPane sectionText = new JTextPane();
        sectionText.setContentType("text/html"); // HTML opmaak inschakelen
        sectionText.setText("""
                <html>
                    <body style="font-family: '%s'; font-size: 14px; color: black;">
                    %s
                    </body>
                </html>
                """.formatted(customFont.getFontName(), content)); // Gebruik custom fontnaam voor de tekst
        sectionText.setEditable(false);
        sectionText.setOpaque(false);
        sectionText.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(sectionText);

        // Ruimte tussen secties
        parent.add(Box.createVerticalStrut(20));
    }

    /**
     * Hulpmethode: Voeg een sectie met veelgestelde vragen toe.
     */
    private void addFAQSection(JPanel parent) {
        JLabel faqTitle = new JLabel("Veelgestelde vragen bij Stratego".toUpperCase());
        faqTitle.setFont(customFont.deriveFont(22f));
        faqTitle.setForeground(Color.WHITE);
        faqTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(faqTitle);
        parent.add(Box.createVerticalStrut(10));

        // FAQ items
        addFAQItem(parent, "Wie kan een maarschalk verslaan?",
                """
                Een maarschalk heeft de hoogste rang, maar is ook kwetsbaar. Als deze op een bom springt die alleen ontmanteld kan worden door een mineur, of als deze aangevallen wordt door een spion, kan de maarschalk heel zwak blijken. De spion wint het namelijk altijd van de maarschalk.
                """
        );
        addFAQItem(parent, "Wat kan een spion in Stratego?",
                """
                Een spion heeft als eigenschap dat dit het enige stuk is dat een maarschalk kan verslaan. Pas dus op dat je tegenspeler geen spion op je maarschalk afstuurt, want dan zou de vlag snel veroverd kunnen worden.
                """
        );
        addFAQItem(parent, "Wie kan de bom uitschakelen in Stratego?",
                """
                Een bom kan worden uitgeschakeld door een mineur. Je kunt een bom niet verplaatsen, maar als een mineur een bom aanvalt, is deze onschadelijk. Is er geen mineur in de buurt? Dan verliest elk stuk dat de bom aanvalt.
                """
        );
    }

    /**
     * Hulpmethode om een enkele FAQ toe te voegen
     */
    private void addFAQItem(JPanel parent, String question, String answer) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setOpaque(false);

        JButton questionButton = new JButton(question);
        questionButton.setFont(customFont.deriveFont(18f));
        questionButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextPane answerText = new JTextPane();
        answerText.setContentType("text/html");
        answerText.setText("""
                <html>
                    <body style="font-family: '%s'; font-size: 14px; color: black;">
                    %s
                    </body>
                </html>
                """.formatted(customFont.getFontName(), answer));
        answerText.setEditable(false);
        answerText.setOpaque(false);
        answerText.setVisible(false);

        questionButton.addActionListener(e -> answerText.setVisible(!answerText.isVisible()));

        itemPanel.add(questionButton);
        itemPanel.add(answerText);
        parent.add(itemPanel);
        parent.add(Box.createVerticalStrut(10));
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
