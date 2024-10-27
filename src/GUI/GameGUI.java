package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JPanel {
    private Board playerBoard;
    private Board aiBoard;
    private Mens player;
    private AI ai;
    private JButton[][] playerButtons;
    private JButton[][] aiButtons;
    private boolean placingShips = true;
    private int shipsPlaced = 0;  // Tellen van het aantal geplaatste schepen
    private Scoreboard scoreboard; // GUI.Scoreboard object
    private JLabel scoreLabel; // Label voor de score

    public GameGUI() {
        playerBoard = new Board();
        aiBoard = new Board();
        player = new Mens(playerBoard);
        ai = new AI(aiBoard);
        scoreboard = new Scoreboard(); // Initialiseer het scoreboard

        // Removed JFrame-specific methods like setTitle, setSize, and setDefaultCloseOperation

        // Set JPanel's layout
        setLayout(new GridLayout(1, 2));

        // Score label
        scoreLabel = new JLabel(scoreboard.toString(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(scoreLabel); // Voeg de score toe aan het frame

        // Panel voor de speler
        JPanel playerPanel = createPlayerPanel();
        add(playerPanel);

        // Panel voor de GUI.AI
        JPanel aiPanel = createAiPanel();
        add(aiPanel);

    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));
        playerButtons = new JButton[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                final int x = i;
                final int y = j;

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (placingShips) {
                            // Plaats schip door de gebruiker in te laten voeren
                            String lengthStr = JOptionPane.showInputDialog("Voer de lengte van het schip (2, 3, 4 of 6):");
                            int length = Integer.parseInt(lengthStr);
                            String direction = JOptionPane.showInputDialog("Plaats schip van lengte " + length + " (h/v):");
                            boolean horizontal = direction.equalsIgnoreCase("h");

                            Ship newShip = new Ship(length);
                            if (playerBoard.placeShip(newShip, x, y, horizontal)) {
                                // Markeer de cellen op het bord
                                for (int k = 0; k < length; k++) {
                                    if (horizontal) {
                                        playerButtons[x][y + k].setBackground(Color.GRAY);
                                    } else {
                                        playerButtons[x + k][y].setBackground(Color.GRAY);
                                    }
                                }
                                shipsPlaced++;  // Verhoog de teller
                                // Start het spel als alle schepen zijn geplaatst
                                if (shipsPlaced == 4) {
                                    startGame();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Ongeldige plaatsing. Probeer opnieuw.");
                            }
                        }
                    }
                });
                playerButtons[i][j] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private JPanel createAiPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));
        aiButtons = new JButton[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                final int x = i;
                final int y = j;

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!placingShips) {
                            // Hier schiet de speler op het bord van de GUI.AI
                            if (aiBoard.hitCell(x, y)) {
                                button.setBackground(Color.RED); // Hit
                                scoreboard.incrementPlayerScore(); // Verhoog de score van de speler
                            } else {
                                button.setBackground(Color.BLUE); // Miss (verander groen naar blauw)
                            }
                            button.setEnabled(false); // De knop wordt uitgeschakeld om herhaalde schoten te voorkomen
                            scoreLabel.setText(scoreboard.toString()); // Update score label
                            checkGameOver(); // Controleer of het spel is afgelopen

                            // GUI.AI beurt na de speler
                            aiTurn();
                        }
                    }
                });
                aiButtons[i][j] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private void startGame() {
        placingShips = false; // Start het spel
        ai.placeShips(); // Plaats de schepen voor de GUI.AI
        JOptionPane.showMessageDialog(this, "Alle schepen zijn geplaatst! Het spel begint nu!");
    }

    private void aiTurn() {
        // GUI.AI maakt zijn zet
        int[] aiMove = ai.makeMove();
        JButton aiButton = playerButtons[aiMove[0]][aiMove[1]]; // GUI.AI schiet op de speler

        if (playerBoard.hitCell(aiMove[0], aiMove[1])) {
            aiButton.setBackground(Color.RED); // Hit GUI.AI
            scoreboard.incrementAIScore(); // Verhoog de score van de GUI.AI
        } else {
            aiButton.setBackground(Color.BLUE); // Miss GUI.AI (verander groen naar blauw)
        }

        aiButton.setEnabled(false); // De knop wordt uitgeschakeld om herhaalde schoten te voorkomen
        scoreLabel.setText(scoreboard.toString()); // Update score label
        checkGameOver(); // Controleer of het spel is afgelopen
    }

    private void checkGameOver() {
        if (!aiBoard.hasShips()) {
            JOptionPane.showMessageDialog(this, "Gefeliciteerd! Je hebt gewonnen!");
            System.exit(0);
        }
        if (!playerBoard.hasShips()) {
            JOptionPane.showMessageDialog(this, "Helaas! De GUI.AI heeft gewonnen!");
            System.exit(0);
        }
    }
}
