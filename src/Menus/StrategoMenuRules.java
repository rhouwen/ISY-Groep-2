package Menus;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class StrategoMenuRules extends JPanel {
    private Font customFont;
    private JLabel titleLabel;
    private JPanel rulesPanel;

    private Image backgroundImage;

    public StrategoMenuRules() {
        // Laad de achtergrondafbeelding "strategobackground.png"
        try {
            backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/strategobackground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Laad font of gebruik een standaard font als fallback
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
        } catch (FontFormatException | IOException e) {
            customFont = new Font("Arial", Font.PLAIN, 18);
        }

        // Gebruik een GridBagLayout om dynamische centrering te ondersteunen
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        // Panel die alle onderdelen bevat
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // Titelbalk
        titleLabel = new JLabel("STRATEGO SPELREGELS", SwingConstants.CENTER);
        titleLabel.setFont(customFont.deriveFont(30f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(800, 50)); // Vaste hoogte voor titel
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel met de regels
        rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setOpaque(false);

        // Voeg secties toe
        addSection("Verplaatsen", "Bij Stratego verplaats je door een stuk een vakje naar links, naar rechts, naar voren of naar achteren te zetten. Je mag maar een speelstuk op een veld zetten. Je kunt stukken niet over andere stukken heen laten springen. Diagonaal plaatsen is ook niet toegestaan. Daarnaast mogen ze niet in de twee meren op het midden van het bord komen.");
        addSection("Aanvallen", "Je mag aanvallen als je direct voor, naast of achter een stuk van de tegenstander staat. Je neemt dan je stuk, tikt het vijandelijke stuk aan en noemt de rang van jouw stuk.");
        addSection("De rangen", "Bij Stratego heb je verschillende rangen. De maarschalk verslaat bijvoorbeeld een generaal en alle lagere rangen, terwijl de generaal alle kolonels en lagere rangen verslaat.");
        addSection("Veelgemaakte fouten", "De meeste fouten bij Stratego hebben te maken met de opstelling. Al je verkenners vooraan zetten of al je mineurs verspreiden, is geen goed idee.");
        addExpandableSection("Veelgestelde vragen", new String[][]{
                {"Kan een maarschalk sneuvelen?", "Een maarschalk kan verloren gaan door een spion of een bom."},
                {"Hoe werkt een spion?", "Een spion kan een maarschalk verslaan als deze aanvalt."},
                {"Wie kan een bom uitschakelen?", "Een mineur kan een bom onschadelijk maken."}
        });

        // Scrollpaneel
        JScrollPane scrollPane = new JScrollPane(rulesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Voor soepele scroll-ervaring

        // Zorg dat het scrollpaneel meeschaalt en text dynamisch centreert
        scrollPane.setViewportView(centerContent(rulesPanel));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Voeg het hoofdpanel toe aan het GridBagLayout
        add(mainPanel, gbc);

        // Dynamisch lettertypen aanpassen bij vensterverandering
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustFontSizes(mainPanel.getWidth());
            }
        });
    }

    private void addSection(String title, String content) {
        // Maak een sectietitel
        JLabel sectionTitle = new JLabel(title.toUpperCase());
        sectionTitle.setFont(customFont.deriveFont(22f));
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesPanel.add(sectionTitle);

        // Inhoud
        JTextPane sectionText = new JTextPane();
        sectionText.setContentType("text/html");
        sectionText.setText("<html><body style='font-family: Arial; font-size: 14px; color: white;'>" + content + "</body></html>");
        sectionText.setEditable(false);
        sectionText.setOpaque(false);
        sectionText.setMaximumSize(new Dimension(600, Integer.MAX_VALUE)); // Vaste breedte
        rulesPanel.add(sectionText);

        rulesPanel.add(Box.createVerticalStrut(20)); // Ruimte toevoegen na secties
    }

    private void addExpandableSection(String title, String[][] faqData) {
        JLabel sectionTitle = new JLabel(title.toUpperCase());
        sectionTitle.setFont(customFont.deriveFont(22f));
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesPanel.add(sectionTitle);

        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        faqPanel.setOpaque(false);
        faqPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String[] faq : faqData) {
            String buttonText = "VEELGESTELDE VRAAG"; // Vaste knoptekst
            String answerTextContent = faq[1]; // Dynamisch antwoord

            JToggleButton toggleButton = createCustomToggleButton(buttonText);
            JTextPane answerText = createAnswerPane(answerTextContent);

            toggleButton.addActionListener(e -> answerText.setVisible(toggleButton.isSelected()));
            faqPanel.add(toggleButton);
            faqPanel.add(answerText);
        }

        rulesPanel.add(faqPanel);
        rulesPanel.add(Box.createVerticalStrut(20)); // Ruimte toevoegen na secties
    }

    private JToggleButton createCustomToggleButton(String buttonText) {
        JToggleButton toggleButton = new JToggleButton(buttonText);
        toggleButton.setFont(customFont.deriveFont(18f)); // Lettertype en grootte
        toggleButton.setForeground(Color.BLACK);
        toggleButton.setBackground(Color.LIGHT_GRAY);
        toggleButton.setFocusPainted(false);
        toggleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        return toggleButton;
    }

    private JTextPane createAnswerPane(String content) {
        JTextPane answerText = new JTextPane();
        answerText.setContentType("text/html");
        answerText.setText("<html><body style='font-family: Arial; font-size: 14px; color: white;'>" + content + "</body></html>");
        answerText.setEditable(false);
        answerText.setOpaque(false);
        answerText.setVisible(false);
        answerText.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        return answerText;
    }

    private JPanel centerContent(JPanel panel) {
        JPanel centeredPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centeredPanel.setOpaque(false);
        centeredPanel.add(panel, gbc);
        return centeredPanel;
    }

    private void adjustFontSizes(int width) {
        float baseFontSize = Math.max(14f, width / 50f);
        titleLabel.setFont(customFont.deriveFont(baseFontSize * 2));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Teken de achtergrondafbeelding
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Als de afbeelding niet kan worden geladen, gebruik een fallback-achtergrondkleur
            g.setColor(new Color(0, 128, 0)); // Fallback kleur
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
