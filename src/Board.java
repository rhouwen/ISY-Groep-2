import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {
    private int size = 8; // Grote van het bord (8x8)
    private JButton[][] buttons; // GUI weergave van het bord

    public Board() {
        this.buttons = new JButton[size][size];
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
