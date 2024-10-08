public class Ship {
    import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Ship {
    private int size; // Grootte van het schip
    private int hits; // Aantal hits op het schip

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
    }

    // Registreert een hit op het schip
    public void hit() {
        hits++;
    }

    // Controleert of het schip gezonken is
    public boolean isSunk() {
        return hits >= size;
    }
}

public class Board extends JPanel {
    private int size = 8; // Grootte van het bord (8x8)
    private JButton[][] buttons; // GUI weergave van het bord
    private Ship[] ships; // Array om de schepen op te slaan

    public Board() {
        this.buttons = new JButton[size][size];
        this.ships = new Ship[2]; // Voorbeeld: maak ruimte voor 2 schepen
        ships[0] = new Ship(3); // Scheep van grootte 3
        ships[1] = new Ship(4); // Scheep van grootte 4
        setLayout(new GridLayout(size, size)); // GridLayout voor het 8x8 bord

        // Initialiseer elke knop
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();

                // Voeg een actie toe aan elke knop
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));

                // Stijl van de knoppen aanpassen (optioneel)
                buttons[i][j].setPreferredSize(new Dimension(50, 50));

                // Voeg de knop toe aan het paneel
                add(buttons[i][j]);
            }
        }
    }

    // Interne klasse voor het verwerken van knoppenklikken
    private class ButtonClickListener implements ActionListener {
        private int x, y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buttons[x][y].setBackground(Color.BLUE); // Verander kleur naar blauw bij klikken
            buttons[x][y].setText("X"); // Markeer het als geraakt

            // Registreer hit op een schip (voorbeeld logica)
            for (Ship ship : ships) {
                ship.hit(); // Registreer hit (voor demo-doeleinden; je moet logica toevoegen om specifieke hits bij te houden)
                if (ship.isSunk()) {
                    System.out.println("Een schip is gezonken!");
                }
            }
        }
    }

    // Start een eenvoudige GUI met een 8x8 bord
    public static void main(String[] args) {
        // Maak het hoofdvenster (JFrame)
        JFrame frame = new JFrame("Zeeslagje ISY - TEAM 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Maak het bord en voeg het toe aan het frame
        Board board = new Board();
        frame.add(board);

        // Pas de grootte van het venster aan op basis van de inhoud
        frame.pack();

        // Maak het venster zichtbaar
        frame.setVisible(true);
    }
}

}
