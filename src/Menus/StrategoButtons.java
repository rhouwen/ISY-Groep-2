package Menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StrategoButtons extends JButton {

    public StrategoButtons(String text, String imagePath, ActionListener action) {
        super(text);

        // Stel de knopafbeelding in
        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(300, 100, Image.SCALE_SMOOTH); // Pas grootte aan
            setIcon(new ImageIcon(scaledImage));
        }

        // Standaard stijl voor de knop
        setHorizontalTextPosition(SwingConstants.CENTER); // Centreer tekst over de afbeelding
        setVerticalTextPosition(SwingConstants.CENTER); // Centreer tekst en afbeelding
        setContentAreaFilled(false); // Maak de achtergrond transparant
        setBorderPainted(false); // Geen rand
        setFocusPainted(false); // Geen focusrand
        setFont(new Font("Arial", Font.BOLD, 14)); // Standaard lettertype
        setForeground(Color.WHITE); // Standaard tekstkleur
        setPreferredSize(new Dimension(220, 80)); // Standaard knopgrootte
        setMargin(new Insets(0, 0, 0, 0)); // Geen extra ruimte rondom de knop

        // Voeg de actie toe
        addActionListener(action);

        // Voeg hover-effect toe
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.YELLOW); // Tekstkleur geel bij hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.WHITE); // Herstel tekstkleur naar wit
            }
        });
    }
}
