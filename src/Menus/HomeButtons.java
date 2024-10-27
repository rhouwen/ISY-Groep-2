package Menus;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class HomeButtons extends JButton {
    private Image backgroundImage;
    private boolean isHovering = false;
    private int buttonWidth = 300; // Default width
    private int buttonHeight = 100; // Default height

    public HomeButtons(String text) {
        super(text);

        // Load the image as a background
        try {
            URL imageUrl = getClass().getClassLoader().getResource("images/homebutton.png");
            if (imageUrl != null) {
                BufferedImage img = ImageIO.read(imageUrl);
                backgroundImage = img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFont(new Font("Arial", Font.BOLD, 20)); // Set font similar to MenuButtons
        setForeground(Color.WHITE); // Set text color to white
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(CENTER);

        // Hover Effect: Change background color on hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                isHovering = true;
                repaint(); // Repaint to show hover effect
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                isHovering = false;
                repaint(); // Repaint to remove hover effect
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        // Return the preferred size based on specified width and height
        return new Dimension(buttonWidth, buttonHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            // Draw the background image to fit the button area
            g.drawImage(backgroundImage, 0, 0, buttonWidth, buttonHeight, this);
        }

        // If hovering, apply a darker overlay on the clickable area
        if (isHovering) {
            g.setColor(new Color(0, 0, 0, 100)); // Translucent black
            g.fillRect(0, 0, buttonWidth, buttonHeight);
        }

        super.paintComponent(g); // Ensure the button text is drawn
    }

    // Method to set the size of the button explicitly
    public void setButtonSize(int width, int height) {
        this.buttonWidth = width;
        this.buttonHeight = height;
        setPreferredSize(new Dimension(width, height));
        revalidate();
        repaint();
    }
}
