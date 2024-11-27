package stratego.gui;

import stratego.gui.panels.CapturedPiecesPanel;
import stratego.gui.panels.ScorePanel;

import javax.swing.*;
import java.awt.*;

public class StrategoGUI extends JPanel {
    //Abstracte class voor gedeelde GUI functies

    public StrategoGUI() {

        setLayout(new BorderLayout());
        add(new GUI(), BorderLayout.CENTER);

        JPanel leftpanel = new JPanel(new GridLayout(3, 1)); // 3 rijen, 1 kolom
        leftpanel.add(new CapturedPiecesPanel("Geslagen stukken van rood"));
        leftpanel.add(new ScorePanel("Score"));
        leftpanel.add(new CapturedPiecesPanel("Geslagen stukken van blauw"));
        add(leftpanel, BorderLayout.WEST);


        //Water instellen
        GUI.updateCell(4, 2, "", Color.BLUE);
        GUI.updateCell(4, 3, "", Color.BLUE);
        GUI.updateCell(5, 3, "", Color.BLUE);
        GUI.updateCell(5, 2, "", Color.BLUE);

        GUI.updateCell(4, 6, "", Color.BLUE);
        GUI.updateCell(4, 7, "", Color.BLUE);
        GUI.updateCell(5, 7, "", Color.BLUE);
        GUI.updateCell(5, 6, "", Color.BLUE);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stratego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new StrategoGUI());
        frame.setVisible(true);
    }

}
