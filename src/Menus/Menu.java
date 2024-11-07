package Menus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends JPanel {

    private Image backgroundImage;

    public Menu() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/battleshipbackground.jpg")).getImage();

        // Load the custom font
        Font customFont = null;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/battleshipmenu.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(110f);  // Adjust size as needed
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Use GridBagLayout to arrange the components centrally
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Padding to space out components

        // Title section (centered above the buttons)
        JLabel titleLabel = new JLabel("BATTLESHIPS");
        titleLabel.setFont(customFont);  // Apply custom font
        titleLabel.setForeground(Color.WHITE);  // White text color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Ensure the title is centered
        add(titleLabel, gbc);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Ensure background is transparent
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 0, 10, 0); // Padding to space out buttons
        buttonGbc.gridx = 0;

        // Add the Start Game button
        buttonGbc.gridy = 0;
        MenuButtons startButton = new MenuButtons("Start Singleplayer Game");
        startButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                // Ensure the content pane is switched without creating a new JFrame
                frame.getContentPane().removeAll(); // Remove current content
                frame.setContentPane(new GUI.GameGUI()); // Add the GameGUI JPanel
                frame.revalidate(); // Refresh the frame
                frame.repaint(); // Repaint to apply changes
            }
        });
        buttonPanel.add(startButton, buttonGbc);

        buttonGbc.gridy++;
        MenuButtons AIvsAI = new MenuButtons("AIvsAI");
        AIvsAI.addActionListener(e -> {
            // Vraag om een naam met een invoerdialoog
            String username = JOptionPane.showInputDialog(this, "Choose a name:");

            // Controleer of de gebruiker een naam heeft ingevoerd
            if (username != null && !username.trim().isEmpty()) {
                // Start de Client voor AI vs AI gameplay met de opgegeven naam in een nieuwe thread
                Thread clientThread = new Thread(new GUI.Client(username));
                clientThread.start();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            }
        });
        buttonPanel.add(AIvsAI, buttonGbc);

        // Options button
        buttonGbc.gridy++;
        MenuButtons optionsButton = new MenuButtons("Options");
        optionsButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll(); // Remove existing content
                frame.setContentPane(new Options("Menu")); // Pass "Menu" to return to Menu
                frame.revalidate(); // Refresh the frame layout
                frame.repaint(); // Repaint to ensure everything is refreshed
            }
        });
        buttonPanel.add(optionsButton, buttonGbc);

        // Exit button
        buttonGbc.gridy++;
        MenuButtons exitButton = new MenuButtons("Return to homescreen");
        exitButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll();
                frame.setContentPane(new Home()); // Return to Home menu
                frame.revalidate();
                frame.repaint();
            }
        });
        buttonPanel.add(exitButton, buttonGbc);

        // Add the button panel to the main layout, below the title
        gbc.gridy = 1;
        add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Call superclass method to ensure proper painting

        // Draw the background image, scale to fit the entire panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
