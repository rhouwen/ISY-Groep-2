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
    private JLabel animatedTitle;
    private JLabel shadowTitle;
    private JLabel subtitleLabel;
    private Timer flickerTimer;
    private boolean isFlickerOn = true;

    public MainMenu() {
        // Laad de achtergrondafbeelding
        try {
            backgroundImage = ImageIO.read(new File("src/Resources/faith-spark-background1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stel het frame in
        setTitle("Main Menu");
        setSize(600, 500); // Groter frame voor grotere tekst
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maak een aangepast paneel voor de achtergrond
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Maak een geanimeerde 3D-titel
        animatedTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        animatedTitle.setFont(new Font("Arial", Font.BOLD, 60));
        animatedTitle.setForeground(Color.YELLOW); // Heldere gele kleur voor het knippereffect

        // Maak een schaduw-effect voor 3D-font simulatie
        shadowTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        shadowTitle.setFont(new Font("Arial", Font.BOLD, 60));
        shadowTitle.setForeground(Color.DARK_GRAY); // Donkergrijze schaduw

        // Ondertitel "Kies een optie"
        subtitleLabel = new JLabel("Choose option:", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        subtitleLabel.setForeground(Color.WHITE); // Witte kleur voor contrast

        // Maak twee nieuwe knoppen voor Battleship en TicTacToe
        JButton battleshipButton = new JButton("Battleship");
        battleshipButton.setPreferredSize(new Dimension(200, 50));
        battleshipButton.setFont(new Font("Arial", Font.BOLD, 16));
        battleshipButton.setBackground(Color.LIGHT_GRAY);
        battleshipButton.setFocusPainted(false);
        battleshipButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JButton ticTacToeButton = new JButton("TicTacToe");
        ticTacToeButton.setPreferredSize(new Dimension(200, 50));
        ticTacToeButton.setFont(new Font("Arial", Font.BOLD, 16));
        ticTacToeButton.setBackground(Color.LIGHT_GRAY);
        ticTacToeButton.setFocusPainted(false);
        ticTacToeButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Voeg hover-effect toe aan de knoppen
        addHoverEffect(battleshipButton);
        addHoverEffect(ticTacToeButton);

        // Maak GridBagConstraints voor layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Plaats de schaduw titel iets verschoven om het 3D-effect te simuleren
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 0, 0); // Voeg marge toe aan de bovenkant
        backgroundPanel.add(shadowTitle, gbc);

        // Plaats de geanimeerde titel bovenop de schaduw, iets verschoven
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 5, 0, 0); // Verschuif de tekst iets voor 3D-effect
        backgroundPanel.add(animatedTitle, gbc);

        // Plaats de ondertitel
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 20, 0);
        backgroundPanel.add(subtitleLabel, gbc);

        // Plaats de Battleship-knop onder de ondertitel
        gbc.gridy = 2;
        backgroundPanel.add(battleshipButton, gbc);

        // Plaats de TicTacToe-knop onder de Battleship-knop
        gbc.gridy = 3;
        backgroundPanel.add(ticTacToeButton, gbc);

        // Voeg het achtergrondpaneel toe aan het frame
        setContentPane(backgroundPanel);
        setVisible(true);

        // Start het knippereffect voor de titel
        flickerEffect();

        // Voeg actie toe aan de Battleship-knop om BattleShipMenu te openen
        battleshipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BattleShipMenu(); // Open BattleShipMenu
                dispose(); // Sluit het huidige venster (MainMenu)
            }
        });

        // Voeg actie toe aan de TicTacToe-knop (geen logica vereist)
        ticTacToeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("TicTacToe start");
            }
        });
    }

    // Aangepast JPanel voor achtergrond
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Voeg hover-effect toe aan knoppen
    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
    }

    // Knippereffect voor de titeltekst
    private void flickerEffect() {
        flickerTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFlickerOn) {
                    animatedTitle.setForeground(Color.YELLOW);
                    subtitleLabel.setForeground(Color.WHITE);
                } else {
                    animatedTitle.setForeground(Color.ORANGE); // Verander naar oranje voor het knippereffect
                    subtitleLabel.setForeground(Color.LIGHT_GRAY);
                }
                isFlickerOn = !isFlickerOn; // Wissel knipperstatus
            }
        });
        flickerTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
