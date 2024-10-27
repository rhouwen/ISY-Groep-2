package Menus;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButtons extends JButton {

    private Image originalImage;
    private boolean isHovering = false;

    public MenuButtons(String text) {
        // Load the image
        ImageIcon icon = new ImageIcon(MenuButtons.class.getClassLoader().getResource("Resources/images/button.png"));
        originalImage = icon.getImage();

        // Set the button text
        setText(text);
        setFont(new Font("Arial", Font.BOLD, 20));
        setForeground(Color.WHITE);

        // Set the preferred size to ensure the button scales with the image
        int buttonWidth = 300;
        int buttonHeight = 100;
        setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        // Add a MouseListener for hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                repaint();  // Repaint to show the hover effect
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();  // Repaint to remove the hover effect
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isHovering = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isHovering = false;
                repaint();
            }
        });

        // Remove borders and background to ensure the button looks like just the image
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the original image
        g.drawImage(originalImage, 0, 0, getWidth(), getHeight(), this);

        // If hovering, apply a darker overlay on the clickable area (not the entire image)
        if (isHovering) {
            g.setColor(new Color(0, 0, 0, 100));  // Translucent black
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw the text on top
        g.setFont(getFont());
        g.setColor(getForeground());
        g.drawString(getText(), getWidth() / 2 - g.getFontMetrics().stringWidth(getText()) / 2, getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 5);
    }
}
