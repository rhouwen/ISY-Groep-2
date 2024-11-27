package Menus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Home extends JPanel {

    private Image backgroundImage;

    public Home() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/homebackground.jpg")).getImage();

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
        gbc.insets = new Insets(20, 0, 20, 0); // Padding to space out components
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title section (centered above the buttons)
        JLabel titleLabel = new JLabel("HOME");
        titleLabel.setFont(customFont);  // Apply custom font
        titleLabel.setForeground(Color.WHITE);  // White text color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        gbc.anchor = GridBagConstraints.NORTH; // Position title above buttons
        add(titleLabel, gbc);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(15, 0, 15, 0); // Consistent padding between buttons

        // Add the Battleship button
        buttonGbc.gridy = 0;
        HomeButtons battleshipButton = new HomeButtons("Battleship");
        battleshipButton.setPreferredSize(new Dimension(300, 120)); // Adjust dimensions
        battleshipButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll(); // Remove current content
                frame.setContentPane(new Menu()); // Switch to the Menu JPanel
                frame.revalidate(); // Refresh the frame
                frame.repaint(); // Repaint to apply changes
            }
        });
        buttonPanel.add(battleshipButton, buttonGbc);

        // Add the TicTacToe button
        buttonGbc.gridy++;
        HomeButtons ticTacToeButton = new HomeButtons("TicTacToe");
        ticTacToeButton.setPreferredSize(new Dimension(300, 80)); // Adjust dimensions
        ticTacToeButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Choose a name:");
            if (username != null && !username.trim().isEmpty()) {
                TicTacToe.TicTacToeClient client = new TicTacToe.TicTacToeClient(username);
                client.startGame("127.0.0.1", 7789);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            }
        });
        buttonPanel.add(ticTacToeButton, buttonGbc);

        // Add the Stratego button
        buttonGbc.gridy++;
        HomeButtons strategoButton = new HomeButtons("Stratego");
        strategoButton.setPreferredSize(new Dimension(300, 80)); // Adjust dimensions
        strategoButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll(); // Remove current content
                frame.setContentPane(new StrategoMenu()); // Switch to the StrategoMenu JPanel
                frame.revalidate(); // Refresh the frame
                frame.repaint(); // Repaint to apply changes
            }
        });
        buttonPanel.add(strategoButton, buttonGbc);


        // Add the Exit button
        buttonGbc.gridy++;
        HomeButtons exitButton = new HomeButtons("Exit");
        exitButton.setPreferredSize(new Dimension(300, 80)); // Adjust dimensions
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton, buttonGbc);

        // Add the button panel to the main layout
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Ensure buttons are centered below title
        add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Call superclass method to ensure proper painting
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw background image
    }
}
