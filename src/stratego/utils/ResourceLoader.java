package stratego.utils;

import javax.swing.*;
import java.awt.*;

public class ResourceLoader {

    /**
     * Creëert een achtergrondpaneel met een afbeelding.
     *
     * @param imagePath Het pad naar de afbeelding binnen de resources map
     * @return Een JPanel dat de achtergrondafbeelding tekent
     */
    public static JPanel createBackgroundPanel(String imagePath) {
        return new JPanel() {
            private final Image backgroundImage = new ImageIcon(
                    getClass().getClassLoader().getResource("images/strategobackground.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }
}
