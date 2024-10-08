import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {

    private BufferedImage backgroundImage;

    public MainMenu() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/Resources/faith-spark-background1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up the frame
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a custom panel for the background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Create buttons with improved styling
        JButton spelerVsSpeler = new JButton("PVP");
        spelerVsSpeler.setPreferredSize(new Dimension(200, 50));
        spelerVsSpeler.setFont(new Font("Arial", Font.BOLD, 16)); // Custom font
        spelerVsSpeler.setBackground(Color.LIGHT_GRAY); // Background color
        spelerVsSpeler.setFocusPainted(false);
        spelerVsSpeler.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); // Border

        JButton spelerVsAi = new JButton("PVE");
        spelerVsAi.setPreferredSize(new Dimension(200, 50));
        spelerVsAi.setFont(new Font("Arial", Font.BOLD, 16));
        spelerVsAi.setBackground(Color.LIGHT_GRAY);
        spelerVsAi.setFocusPainted(false);
        spelerVsAi.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JButton aiVsAi = new JButton("CPUvCPU");
        aiVsAi.setPreferredSize(new Dimension(200, 50));
        aiVsAi.setFont(new Font("Arial", Font.BOLD, 16));
        aiVsAi.setBackground(Color.LIGHT_GRAY);
        aiVsAi.setFocusPainted(false);
        aiVsAi.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Add hover effect
        spelerVsSpeler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                spelerVsSpeler.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                spelerVsSpeler.setBackground(Color.LIGHT_GRAY);
            }
        });

        spelerVsAi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                spelerVsAi.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                spelerVsAi.setBackground(Color.LIGHT_GRAY);
            }
        });

        aiVsAi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aiVsAi.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                aiVsAi.setBackground(Color.LIGHT_GRAY);
            }
        });

        // Create GridBagConstraints for button placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Makes button fill horizontally
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        backgroundPanel.add(spelerVsSpeler, gbc);

        gbc.gridy = 1; // Row 1
        backgroundPanel.add(spelerVsAi, gbc);

        gbc.gridy = 2; // Row 2
        backgroundPanel.add(aiVsAi, gbc);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
        setVisible(true);

        // Add action listeners for buttons
        spelerVsSpeler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PVP start");
                // Add the destination for PVP here
            }
        });

        spelerVsAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PVE start");
                // Add the destination for PVE here
            }
        });

        aiVsAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CPUvCPU start");
                // Add the destination for CPU here
            }
        });
    }

    // Custom JPanel for background
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Call the superclass's paint method
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw the background image
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
