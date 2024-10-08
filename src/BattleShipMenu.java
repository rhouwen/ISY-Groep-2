import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BattleShipMenu extends JFrame {

    private BufferedImage backgroundImage;
    private JLabel animatedTitle;
    private JLabel shadowTitle;
    private JLabel subtitleLabel;
    private Timer flickerTimer;
    private boolean isFlickerOn = true;

    public BattleShipMenu() {
        // Laad de achtergrondafbeelding
        try {
            backgroundImage = ImageIO.read(new File("src/Resources/battleship.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stel het frame in
        setTitle("BattleShip Menu");
        setSize(600, 500); // Groter frame voor grotere tekst
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maak een aangepast paneel voor de achtergrond
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Maak een geanimeerde 3D-titel
        animatedTitle = new JLabel("BattleShip Game", SwingConstants.CENTER);
        animatedTitle.setFont(new Font("Arial", Font.BOLD, 60));
        animatedTitle.setForeground(Color.YELLOW); // Heldere gele kleur voor het knippereffect

        // Maak een schaduw-effect voor 3D-font simulatie
        shadowTitle = new JLabel("BattleShip Game", SwingConstants.CENTER);
        shadowTitle.setFont(new Font("Arial", Font.BOLD, 60));
        shadowTitle.setForeground(Color.DARK_GRAY); // Donkergrijze schaduw

        // Ondertitel
        subtitleLabel = new JLabel("Choose Game Mode:", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        subtitleLabel.setForeground(Color.WHITE); // Witte kleur voor contrast

        // Maak knoppen voor PVP, PVE en CPUvCPU
        JButton spelerVsSpeler = new JButton("PVP");
        spelerVsSpeler.setPreferredSize(new Dimension(200, 50));
        spelerVsSpeler.setFont(new Font("Arial", Font.BOLD, 16));
        spelerVsSpeler.setBackground(Color.LIGHT_GRAY);
        spelerVsSpeler.setFocusPainted(false);
        spelerVsSpeler.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

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

        // Voeg hover-effect toe aan de knoppen
        addHoverEffect(spelerVsSpeler);
        addHoverEffect(spelerVsAi);
        addHoverEffect(aiVsAi);

        // Layout configureren
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

        // Plaats de knoppen
        gbc.gridy = 2;
        backgroundPanel.add(spelerVsSpeler, gbc);

        gbc.gridy = 3;
        backgroundPanel.add(spelerVsAi, gbc);

        gbc.gridy = 4;
        backgroundPanel.add(aiVsAi, gbc);

        // Voeg het achtergrondpaneel toe aan het frame
        setContentPane(backgroundPanel);
        setVisible(true);

        // Start het knippereffect voor de titel
        flickerEffect();

        // Voeg actie toe aan de knoppen
        spelerVsSpeler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PVP start");
                // Voeg de bestemming voor PVP hier toe
            }
        });

        spelerVsAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PVE start");
                // Voeg de bestemming voor PVE hier toe
            }
        });

        aiVsAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CPUvCPU start");
                // Voeg de bestemming voor CPU hier toe
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
        SwingUtilities.invokeLater(() -> new BattleShipMenu());
    }
}
